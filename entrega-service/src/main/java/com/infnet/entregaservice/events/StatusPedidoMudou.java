package com.infnet.entregaservice.events;

import com.infnet.entregaservice.enums.StatusPedido;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StatusPedidoMudou implements Serializable {
    private Long pedidoId;
    private StatusPedido statusPedido;
    private LocalDateTime timestamp;

    public StatusPedidoMudou(){
    }

    public StatusPedidoMudou(Long pedidoId, StatusPedido statusPedido){
        this.pedidoId = pedidoId;
        this.statusPedido = statusPedido;
        this.timestamp = LocalDateTime.now();
    }

    public StatusPedidoMudou(Long pedidoId, StatusPedido statusPedido, LocalDateTime timestamp){
        this.pedidoId = pedidoId;
        this.statusPedido = statusPedido;
        this.timestamp = timestamp;
    }
}
