package com.infnet.pedidosservice.infra.message;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.infnet.pedidosservice.events.StatusPedidoMudou;
import com.infnet.pedidosservice.model.Item;

import java.io.IOException;
import java.util.List;

public class StatusPedidoMudouSerializer extends StdSerializer<StatusPedidoMudou> {

    public StatusPedidoMudouSerializer(){
        super(StatusPedidoMudou.class);
    }

    @Override
    public void serialize(StatusPedidoMudou event, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("pedidoId", event.getPedidoId());
        jsonGenerator.writeStringField("status", event.getStatusPedido().toString());
        jsonGenerator.writeArrayFieldStart("itens");
        List<Item> itens = event.getItens();
        for (Item item : itens) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("produtoId", item.getProdutoId());
            jsonGenerator.writeNumberField("quantidade", item.getQuantidade());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeStringField("timestamp", event.getTimestamp().toString());
    }
}
