package com.infnet.almoxarifadoservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@Entity
public class Produto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produtoId;
    private ProdutoInfo produtoInfo = new ProdutoInfo();
    private Quantidade quantidade = new Quantidade();

    public void reservarProduto(int quantidade){
        if(this.quantidade.getQuantidadeDisponivel() >= quantidade){
            this.quantidade.setQuantidadeDisponivel(this.quantidade.getQuantidadeDisponivel() - quantidade);
            this.quantidade.setQuantidadeReservada(this.quantidade.getQuantidadeReservada() + quantidade);
        } else {
            throw new IllegalArgumentException("Quantidade do produto não disponível em estoque.");
        }
    }

    public void removerProdutoReservado(int quantidade){
        this.quantidade.setQuantidadeReservada(this.quantidade.getQuantidadeReservada() - quantidade);
    }

    public void cancelarReserva(int quantidade){
        this.quantidade.setQuantidadeDisponivel(this.quantidade.getQuantidadeDisponivel() + quantidade);
        this.quantidade.setQuantidadeReservada(this.quantidade.getQuantidadeReservada() - quantidade);
    }
}
