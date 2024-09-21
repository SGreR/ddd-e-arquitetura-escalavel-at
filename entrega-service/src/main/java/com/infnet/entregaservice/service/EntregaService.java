package com.infnet.entregaservice.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import com.infnet.entregaservice.enums.StatusPedido;
import com.infnet.entregaservice.events.StatusPedidoMudou;
import com.infnet.entregaservice.model.Atualizacao;
import com.infnet.entregaservice.model.Entrega;
import com.infnet.entregaservice.repository.EntregaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {
    private static final Logger log = LoggerFactory.getLogger(EntregaService.class);
    private final EntregaRepository entregaRepository;
    private final PubSubTemplate pubSubTemplate;
    private final JacksonPubSubMessageConverter converter;

    public EntregaService(EntregaRepository entregaRepository, PubSubTemplate pubSubTemplate, JacksonPubSubMessageConverter converter) {
        this.entregaRepository = entregaRepository;
        this.pubSubTemplate = pubSubTemplate;
        this.converter = converter;
    }

    public List<Entrega> getAll(){
        return entregaRepository.findAll();
    }

    public Optional<Entrega> getById(Long id){
        return entregaRepository.findById(id);
    }

    public Optional<Entrega> getByOrderId(Long pedidoId){
        return entregaRepository.findByOrderId(pedidoId);
    }

    public Entrega save(Entrega entrega){
        return entregaRepository.save(entrega);
    }

    public Entrega update(Long id, Entrega entrega){
        Optional<Entrega> oldPedido = getById(id);
        if(oldPedido.isPresent()){
            return entregaRepository.save(entrega);
        }
        return null;
    }

    public void delete(Long id){
        entregaRepository.deleteById(id);
    }

    public Entrega enviarEntrega(Long id){
        Optional<Entrega> entrega = getById(id);
        entrega.ifPresent(value -> enviar(new StatusPedidoMudou(value.getPedidoId(), StatusPedido.EM_TRANSITO)));
        return entrega.get();
    }

    public Entrega confirmarEntrega(Long id){
        Optional<Entrega> entrega = getById(id);
        entrega.ifPresent(value -> enviar(new StatusPedidoMudou(value.getPedidoId(), StatusPedido.ENTREGUE)));
        return entrega.get();
    }

    public Entrega entregaEstraviada(Long id){
        Optional<Entrega> entrega = getById(id);
        entrega.ifPresent(value -> enviar(new StatusPedidoMudou(value.getPedidoId(), StatusPedido.ESTRAVIADO)));
        return entrega.get();
    }

    @ServiceActivator(inputChannel = "pedidosMessageChannel")
    public void receberPedido(StatusPedidoMudou payload, @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) ConvertedBasicAcknowledgeablePubsubMessage<StatusPedidoMudou> message) {
        log.info("ENTREGA-SERVICE: Mensagem recebida: {}", payload);
        Entrega entrega = new Entrega();
        if (payload.getStatusPedido() == StatusPedido.NOVO) {
            entrega = new Entrega(payload.getPedidoId());
            entrega.getAtualizacoes().add(new Atualizacao("Aguardando confirmação."));
            save(entrega);
        } else if (payload.getStatusPedido() == StatusPedido.CANCELADO) {
            Optional<Entrega> optionalEntrega = getByOrderId(payload.getPedidoId());
            if (optionalEntrega.isPresent()) {
                entrega = optionalEntrega.get();
                entrega.getAtualizacoes().add(new Atualizacao("Envio cancelado."));
            }
        }
        message.ack();
    }

    public void enviar(StatusPedidoMudou status){
        pubSubTemplate.setMessageConverter(converter);
        pubSubTemplate.publish("Entregas-Topic", status);
        log.info("ENTREGA-SERVICE: Mensagem Publicada: {}", status);
    }
}
