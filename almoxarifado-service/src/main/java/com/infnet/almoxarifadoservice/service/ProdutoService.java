package com.infnet.almoxarifadoservice.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import com.infnet.almoxarifadoservice.enums.StatusPedido;
import com.infnet.almoxarifadoservice.events.StatusPedidoMudou;
import com.infnet.almoxarifadoservice.model.Produto;
import com.infnet.almoxarifadoservice.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final Logger log = LoggerFactory.getLogger(ProdutoService.class);
    private final ProdutoRepository produtoRepository;
    private final PubSubTemplate pubSubTemplate;
    private final JacksonPubSubMessageConverter converter;
    
    public ProdutoService(ProdutoRepository produtoRepository, PubSubTemplate pubSubTemplate, JacksonPubSubMessageConverter converter) {
        this.produtoRepository = produtoRepository;
        this.pubSubTemplate = pubSubTemplate;
        this.converter = converter;
    }

    public List<Produto> getAll(){
        return produtoRepository.findAll();
    }

    public Optional<Produto> getById(Long id){
        return produtoRepository.findById(id);
    }

    public Produto save(Produto produto){
        return produtoRepository.save(produto);
    }

    public Produto update(Long id, Produto produto){
        Optional<Produto> oldPedido = getById(id);
        if(oldPedido.isPresent()){
            return produtoRepository.save(produto);
        }
        return null;
    }

    public void delete(Long id){
        produtoRepository.deleteById(id);
    }

    @ServiceActivator(inputChannel = "pedidosMessageChannel")
    public void receberPedido(StatusPedidoMudou payload, @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) ConvertedBasicAcknowledgeablePubsubMessage<StatusPedidoMudou> message){
        log.info("PRODUTO-SERVICE: Mensagem recebida: {}", payload);
        for(Produto p : payload.getProdutos()){
            Produto produto = produtoRepository.findById(p.getProdutoId()).get();
            if (payload.getStatusPedido() == StatusPedido.NOVO) {
                produto.reservarProduto(p.getQuantidade().getQuantidadeReservada());
            } else if (payload.getStatusPedido() == StatusPedido.FECHADO) {
                produto.removerProdutoReservado(p.getQuantidade().getQuantidadeReservada());
            } else if (payload.getStatusPedido() == StatusPedido.CANCELADO){
                produto.cancelarReserva(p.getQuantidade().getQuantidadeReservada());
            }
            produtoRepository.save(produto);
        }
        message.ack();
        if (payload.getStatusPedido() == StatusPedido.FECHADO) {
            payload.setStatusPedido(StatusPedido.EM_PREPARACAO);
            enviar(payload);
        } else if(payload.getStatusPedido() == StatusPedido.CANCELADO){
            payload.setStatusPedido(StatusPedido.CANCELADO);
            enviar(payload);
        }

    }

    public void enviar(StatusPedidoMudou status){
        pubSubTemplate.setMessageConverter(converter);
        pubSubTemplate.publish("Produtos-Topic", status);
        log.info("PRODUTO-SERVICE: Mensagem Publicada: {}",  status);
    }
}
