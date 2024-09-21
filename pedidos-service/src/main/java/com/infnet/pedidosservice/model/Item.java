package com.infnet.pedidosservice.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
public class Item implements Serializable {
    private Long produtoId;
    private String nomeProduto = "";
    private String descricaoProduto = "";
    private int quantidade = 1;
    private double precoUnitario = 0.0;
    @Transient
    private double precoTotal = 0.0;

    public Item(String nomeProduto, String descricaoProduto, int quantidade, double precoUnitario, double precoTotal) {
        if (nomeProduto == null || nomeProduto.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode estar vazio");
        }
        this.nomeProduto = nomeProduto;

        this.descricaoProduto = descricaoProduto != null ? descricaoProduto : "";

        if (quantidade < 0){
            throw new IllegalArgumentException("Quantidade do produto não pode ser negativa.");
        }

        if (precoUnitario < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo");
        }
        this.precoUnitario = precoUnitario;

        if (precoTotal < 0) {
            throw new IllegalArgumentException("Preço total não pode ser negativo");
        }
        this.precoTotal = precoTotal;
    }

    public double getPrecoTotal() {
        return precoUnitario * quantidade;
    }
}
