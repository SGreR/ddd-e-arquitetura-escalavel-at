package com.infnet.pedidosservice.model;
import com.infnet.pedidosservice.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long pedidoId;
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;
    @ElementCollection
    private List<Item> itens = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido = StatusPedido.NOVO;
    private double valorTotal = 0.0;

    public void adicionarItem(Item item){
        if(this.statusPedido != StatusPedido.NOVO){
            throw new IllegalStateException("Não é possível adicionar itens a um pedido em andamento.");
        }
        this.itens.add(item);
        calcularValortotal();
    }

    public void fecharPedido(){
        if(this.statusPedido != StatusPedido.NOVO){
            throw new IllegalStateException("Não é possível fechar um pedido já em andamento.");
        }
        if(this.itens.isEmpty()){
            throw new IllegalStateException("Não é possível fechar um pedido vazio.");
        }
        this.setStatusPedido(StatusPedido.FECHADO);
    }

    public void cancelarPedido(){
        if(this.statusPedido != StatusPedido.FECHADO){
            throw new IllegalStateException("Não é possível cancelar um pedido que não esteja fechado.");
        }
        this.setStatusPedido(StatusPedido.CANCELADO);
    }

    public void enviarPedido(){
        if(this.statusPedido != StatusPedido.FECHADO){
            throw new IllegalStateException("Não é possível enviar um pedido que não esteja fechado.");
        }
        this.setStatusPedido(StatusPedido.EM_PREPARACAO);
    }

    @PrePersist
    @PreUpdate
    private void calcularValortotal(){
        double valor = 0.0;
        for(Item item : this.getItens()){
            valor += item.getPrecoTotal();
        }
        this.setValorTotal(valor);
    }
}
