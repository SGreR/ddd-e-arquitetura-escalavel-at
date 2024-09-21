package com.infnet.entregaservice.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
class Endereco {
    private String observacao = "";
    private String complemento = "";
    private int numero = 1;
    private String rua = "Rua X";
    private String cidade = "Cidade X";
    private String estado = "Estado X";
    private String cep = "00000000";

    public Endereco(String cep, String estado, String cidade, String rua, int numero, String complemento, String observacao) {
        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("CEP não pode estar vazio");
        }
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("Estado não pode estar vazio");
        }
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("Cidade não pode estar vazia");
        }
        if (rua == null || rua.isBlank()) {
            throw new IllegalArgumentException("Rua não pode estar vazia");
        }
        if (numero <= 0) {
            throw new IllegalArgumentException("Número deve ser positivo");
        }
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.observacao = observacao;
    }
}
