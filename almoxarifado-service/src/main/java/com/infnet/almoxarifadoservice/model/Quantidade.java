package com.infnet.almoxarifadoservice.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Quantidade {
    private int quantidadeDisponivel;
    private int quantidadeReservada;

    public Quantidade(){
        this.quantidadeDisponivel = 0;
        this.quantidadeReservada = 0;
    }

    public Quantidade(int quantidadeDisponivel) {
        if (quantidadeDisponivel < 0) {
            throw new IllegalArgumentException("Quantidade do produto não pode ser negativa.");
        }
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public Quantidade(int quantidadeDisponivel, int quantidadeReservada){
        if (quantidadeDisponivel < 0) {
            throw new IllegalArgumentException("Quantidade do produto não pode ser negativa.");
        }
        this.quantidadeDisponivel = quantidadeDisponivel;
        if(quantidadeReservada < 0){
            throw new IllegalArgumentException("Quantidade reservada não pode ser negativa.");
        }
        this.quantidadeReservada = quantidadeReservada;
    }
}
