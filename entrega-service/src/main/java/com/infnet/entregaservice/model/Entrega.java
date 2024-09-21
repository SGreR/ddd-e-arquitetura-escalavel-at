package com.infnet.entregaservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entregaId;
    private Long pedidoId;
    private Endereco enderecoDeEntrega;
    private String codigoDeRastreio = "";
    @ElementCollection
    private List<Atualizacao> atualizacoes = new ArrayList<>();

    public Entrega(){

    }

    public Entrega(Long pedidoId){
        this.pedidoId = pedidoId;
        this.enderecoDeEntrega = new Endereco();
        this.codigoDeRastreio = "";
        this.atualizacoes = new ArrayList<>();
    }

    public Entrega(Long pedidoId, Endereco enderecoDeEntrega){
        this.pedidoId = pedidoId;
        this.enderecoDeEntrega = enderecoDeEntrega;
        this.codigoDeRastreio = "";
        this.atualizacoes = new ArrayList<>();
    }

    public Entrega(Long pedidoId, Endereco enderecoDeEntrega, String codigoDeRastreio){
        this.pedidoId = pedidoId;
        this.enderecoDeEntrega = enderecoDeEntrega;
        this.codigoDeRastreio = codigoDeRastreio;
        this.atualizacoes = new ArrayList<>();
    }
}
