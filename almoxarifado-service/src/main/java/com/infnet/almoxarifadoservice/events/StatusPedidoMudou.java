package com.infnet.almoxarifadoservice.events;

import com.infnet.almoxarifadoservice.enums.StatusPedido;
import com.infnet.almoxarifadoservice.model.Produto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StatusPedidoMudou implements Serializable {
    private Long pedidoId;
    private StatusPedido statusPedido;
    private List<Produto> produtos;
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
