package com.infnet.pedidosservice.events;

import com.infnet.pedidosservice.enums.StatusPedido;
import com.infnet.pedidosservice.model.Item;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StatusPedidoMudou implements Serializable {
    private Long pedidoId;
    private StatusPedido statusPedido;
    private List<Item> itens;
    private LocalDateTime timestamp;

    public StatusPedidoMudou(){
    }

    public StatusPedidoMudou(Long pedidoId, StatusPedido statusPedido){
        this.pedidoId = pedidoId;
        this.statusPedido = statusPedido;
        this.timestamp = LocalDateTime.now();
        this.itens = new ArrayList<Item>();
    }

    public StatusPedidoMudou(Long pedidoId, StatusPedido statusPedido, List<Item> itens){
        this.pedidoId = pedidoId;
        this.statusPedido = statusPedido;
        this.itens = itens;
        this.timestamp = LocalDateTime.now();
    }

    public StatusPedidoMudou(Long pedidoId, StatusPedido statusPedido, LocalDateTime timestamp){
        this.pedidoId = pedidoId;
        this.statusPedido = statusPedido;
        this.timestamp = timestamp;
        this.itens = new ArrayList<>();
    }

    public StatusPedidoMudou(Long pedidoId, StatusPedido statusPedido, LocalDateTime timestampm, List<Item> itens){
        this.pedidoId = pedidoId;
        this.statusPedido = statusPedido;
        this.timestamp = timestamp;
        this.itens = itens;
    }
}
