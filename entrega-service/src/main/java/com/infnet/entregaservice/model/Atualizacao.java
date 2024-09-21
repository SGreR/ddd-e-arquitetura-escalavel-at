package com.infnet.entregaservice.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@Embeddable
public class Atualizacao {
    private LocalDateTime timestamp;
    private String mensagem;

    public Atualizacao(){
        this.timestamp = LocalDateTime.now();
        this.mensagem = "";
    }

    public Atualizacao(String mensagem){
        this.timestamp = LocalDateTime.now();
        this.mensagem = mensagem;
    }
}
