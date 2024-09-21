package com.infnet.entregaservice.infra.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import com.infnet.entregaservice.events.StatusPedidoMudou;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MessageConfig {

    @Bean
    public JacksonPubSubMessageConverter statusMudouConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(StatusPedidoMudou.class, new StatusPedidoMudouSerializer());
        simpleModule.addDeserializer(StatusPedidoMudou.class, new StatusPedidoMudouDeserializer());
        objectMapper.registerModule(simpleModule);
        return new JacksonPubSubMessageConverter(objectMapper);
    }

    @Bean
    public MessageChannel pedidosMessageChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter pedidosChannelAdapter(
            @Qualifier("pedidosMessageChannel") MessageChannel messageChannel, PubSubTemplate pubSubTemplate){
        pubSubTemplate.setMessageConverter(statusMudouConverter());

        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "PetFriends_Pedidos_Entrega");
        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(StatusPedidoMudou.class);
        return adapter;
    }

    @Bean
    public MessageChannel almoxarifadoMessageChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter almoxarifadoChannelAdapter(
            @Qualifier("almoxarifadoMessageChannel") MessageChannel messageChannel, PubSubTemplate pubSubTemplate){
        pubSubTemplate.setMessageConverter(statusMudouConverter());
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "PetFriends_Produtos_Entrega");
        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(StatusPedidoMudou.class);
        return adapter;
    }

}
