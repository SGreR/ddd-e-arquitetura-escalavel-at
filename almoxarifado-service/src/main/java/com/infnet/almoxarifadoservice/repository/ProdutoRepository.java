package com.infnet.almoxarifadoservice.repository;

import com.infnet.almoxarifadoservice.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
