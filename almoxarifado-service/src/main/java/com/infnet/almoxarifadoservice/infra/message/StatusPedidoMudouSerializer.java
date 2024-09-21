package com.infnet.almoxarifadoservice.infra.message;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.infnet.almoxarifadoservice.events.StatusPedidoMudou;
import com.infnet.almoxarifadoservice.model.Produto;


import java.io.IOException;
import java.util.ArrayList;
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
        List<Produto> produtos;
        if(!event.getProdutos().isEmpty()){
            produtos = event.getProdutos();
            for (Produto produto : produtos) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("produtoId", produto.getProdutoId());
                jsonGenerator.writeNumberField("quantidade", produto.getQuantidade().getQuantidadeReservada());
                jsonGenerator.writeEndObject();
            }
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeStringField("timestamp", event.getTimestamp().toString());
    }
}
