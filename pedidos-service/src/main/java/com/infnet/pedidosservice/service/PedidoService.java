package com.infnet.pedidosservice.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import com.infnet.pedidosservice.enums.StatusPedido;
import com.infnet.pedidosservice.events.StatusPedidoMudou;
import com.infnet.pedidosservice.model.Item;
import com.infnet.pedidosservice.model.Pedido;
import com.infnet.pedidosservice.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.metrics.data.RepositoryMetricsAutoConfiguration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {


    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;
    private final PubSubTemplate pubSubTemplate;
    private final JacksonPubSubMessageConverter converter;

    PedidoService(PedidoRepository pedidoRepository, PubSubTemplate pubSubTemplate, JacksonPubSubMessageConverter converter){
        this.pedidoRepository = pedidoRepository;
        this.pubSubTemplate = pubSubTemplate;
        this.converter = converter;
    }

    public List<Pedido> getAll(){
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> getById(Long id){
        return pedidoRepository.findById(id);
    }

    public Pedido save(Pedido pedido){
        Pedido novoPedido = pedidoRepository.save(pedido);
        enviar(new StatusPedidoMudou(novoPedido.getPedidoId(), StatusPedido.NOVO, novoPedido.getItens()));
        return novoPedido;
    }

    public Pedido update(Long id, Pedido pedido){
        Optional<Pedido> optionalPedido = getById(id);
        if(optionalPedido.isPresent()){
            pedido.setPedidoId(optionalPedido.get().getPedidoId());
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    public Pedido adicionarItem(Long id, Item item){
        Optional<Pedido> optionalPedido = getById(id);
        if(optionalPedido.isPresent()){
            Pedido pedido = optionalPedido.get();
            pedido.adicionarItem(item);
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    public Pedido fecharPedido(Long id){
        Pedido pedido = pedidoRepository.getReferenceById(id);
        pedido.fecharPedido();
        pedido = pedidoRepository.save(pedido);
        enviar(new StatusPedidoMudou(pedido.getPedidoId(), StatusPedido.FECHADO, pedido.getItens()));
        return pedido;
    }

    public Pedido cancelarPedido(Long id){
        Pedido pedido = pedidoRepository.getReferenceById(id);
        pedido.cancelarPedido();
        pedido = pedidoRepository.save(pedido);
        enviar(new StatusPedidoMudou(pedido.getPedidoId(), StatusPedido.CANCELADO, pedido.getItens()));
        return pedido;
    }

    public void delete(Long id){
        pedidoRepository.deleteById(id);
    }

    public void enviar(StatusPedidoMudou status){
        pubSubTemplate.setMessageConverter(converter);
        pubSubTemplate.publish("Pedidos-Topic", status);
        log.info("PEDIDO-SERVICE: Mensagem Publicada: {}", status);
    }

    @ServiceActivator(inputChannel = "almoxarifadoMessageChannel")
    public void receberAlmoxarifado(StatusPedidoMudou payload, @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE)ConvertedBasicAcknowledgeablePubsubMessage<StatusPedidoMudou> message){
        log.info("PEDIDO-SERVICE: Mensagem Recebida, {} ", payload);
        Optional<Pedido> optionalPedido = getById(payload.getPedidoId());
        if(optionalPedido.isPresent()) {
            Pedido pedido = optionalPedido.get();
            pedido.setStatusPedido(payload.getStatusPedido());
            update(pedido.getPedidoId(), pedido);
        }
        message.ack();
    }

    @ServiceActivator(inputChannel = "entregaMessageChannel")
    public void receberEntrega(StatusPedidoMudou payload, @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE)ConvertedBasicAcknowledgeablePubsubMessage<StatusPedidoMudou> message){
        log.info("PEDIDO-SERVICE: Mensagem Recebida, {} ", payload);
        Optional<Pedido> optionalPedido = getById(payload.getPedidoId());
        if(optionalPedido.isPresent()) {
            Pedido pedido = optionalPedido.get();
            pedido.setStatusPedido(payload.getStatusPedido());
            update(pedido.getPedidoId(), pedido);
        }
        message.ack();
    }

}
