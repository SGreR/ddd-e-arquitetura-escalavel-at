package com.infnet.entregaservice.repository;

import com.infnet.entregaservice.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    @Query("SELECT e FROM Entrega e WHERE e.pedidoId = :pedidoId")
    public Optional<Entrega> findByOrderId(@Param("pedidoId") Long pedidoId);
}
