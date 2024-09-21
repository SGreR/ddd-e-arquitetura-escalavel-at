package com.infnet.pedidosservice.infra.message;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.infnet.pedidosservice.enums.StatusPedido;
import com.infnet.pedidosservice.events.StatusPedidoMudou;
import com.infnet.pedidosservice.model.Item;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class StatusPedidoMudouDeserializer extends StdDeserializer<StatusPedidoMudou> {

    private final ObjectMapper mapper;

    public StatusPedidoMudouDeserializer(){
        super(StatusPedidoMudou.class);
        this.mapper = new ObjectMapper();
    }

    @Override
    public StatusPedidoMudou deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JacksonException {
        StatusPedidoMudou event = null;
        JsonNode node = jp.getCodec().readTree(jp);
        try {
            Long pedidoId = node.get("pedidoId").asLong();
            StatusPedido status = StatusPedido.valueOf(node.get("status").asText());
            LocalDateTime timestamp = LocalDateTime.parse(node.get("timestamp").asText());

            JsonNode itensNode = node.get("itens");
            List<Item> itens = new ArrayList<>();
            if(itensNode != null){
                itens = mapper.readValue(itensNode.traverse(jp.getCodec()), new TypeReference<List<Item>>() {});
            }

            event = new StatusPedidoMudou();
            event.setPedidoId(pedidoId);
            event.setStatusPedido(status);
            event.setItens(itens);
            event.setTimestamp(timestamp);
        } catch (DateTimeParseException e) {
            throw new IOException("Erro na data", e);
        }
        return event;
    }
}
