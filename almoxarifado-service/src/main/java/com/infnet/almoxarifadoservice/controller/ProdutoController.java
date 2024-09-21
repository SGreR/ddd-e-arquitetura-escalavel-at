package com.infnet.almoxarifadoservice.controller;

import com.infnet.almoxarifadoservice.model.Produto;
import com.infnet.almoxarifadoservice.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        List<Produto> produtos = produtoService.getAll();
        if(produtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.getById(id);
        if(produto.isPresent()){
            return ResponseEntity.ok(produto.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Produto produto) {
        Produto produtoSalvo = produtoService.save(produto);
        if(produtoSalvo == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produtoSalvo);
    }

    @PutMapping
    public ResponseEntity<?> update(Long id, @RequestBody Produto produto) {
        Produto produtoSalvo = produtoService.update(id, produto);
        if(produtoSalvo == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produtoSalvo);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(Long id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
