package com.infnet.entregaservice.infra.message;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.infnet.entregaservice.events.StatusPedidoMudou;


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
        jsonGenerator.writeStringField("timestamp", event.getTimestamp().toString());
    }
}
