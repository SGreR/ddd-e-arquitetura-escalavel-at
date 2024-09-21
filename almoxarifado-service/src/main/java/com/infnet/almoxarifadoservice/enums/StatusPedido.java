package com.infnet.almoxarifadoservice.enums;

import lombok.Getter;

@Getter
public enum StatusPedido {
    NOVO,
    FECHADO,
    EM_PREPARACAO,
    EM_TRANSITO,
    ENTREGUE,
    ESTRAVIADO,
    RECUSADO,
    CANCELADO
}
