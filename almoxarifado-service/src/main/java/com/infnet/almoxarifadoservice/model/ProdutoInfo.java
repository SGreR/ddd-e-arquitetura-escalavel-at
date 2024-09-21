package com.infnet.almoxarifadoservice.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class ProdutoInfo {
    private String nomeProduto = "Produto";
    private String descricaoProduto = "Descrição";
    private String codigoDeBarras = "0000000000000";
    private double precoUnitario = 0.0;

    public ProdutoInfo(String nomeProduto, String descricaoProduto, String codigoDeBarras, double precoUnitario) {
        if (nomeProduto == null || nomeProduto.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode estar vazio");
        }
        this.nomeProduto = nomeProduto;

        this.descricaoProduto = descricaoProduto != null ? descricaoProduto : "Descrição";

        if (codigoDeBarras == null || codigoDeBarras.isBlank() || codigoDeBarras.length() < 13) {
            throw new IllegalArgumentException("Código de barras deve ter pelo menos 13 caracteres");
        }
        this.codigoDeBarras = codigoDeBarras;

        if (precoUnitario < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo");
        }
        this.precoUnitario = precoUnitario;
    }
}
