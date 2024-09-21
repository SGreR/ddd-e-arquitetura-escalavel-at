package com.infnet.entregaservice.controller;

import com.infnet.entregaservice.model.Entrega;
import com.infnet.entregaservice.service.EntregaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/entregas")
public class EntregaController {
    
    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @GetMapping
    public ResponseEntity<List<Entrega>> getAll() {
        List<Entrega> entregas = entregaService.getAll();
        if(entregas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entregas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Entrega> entrega = entregaService.getById(id);
        if(entrega.isPresent()){
            return ResponseEntity.ok(entrega.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Entrega entrega) {
        Entrega entregaSalvo = entregaService.save(entrega);
        if(entregaSalvo == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entregaSalvo);
    }

    @PatchMapping("/enviar/{id}")
    public ResponseEntity<?> fecharEntrega(@PathVariable Long id) {
        Entrega entrega = entregaService.enviarEntrega(id);
        if(entrega == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entrega);
    }

    @PatchMapping("/confirmar/{id}")
    public ResponseEntity<?> cancelarEntrega(@PathVariable Long id) {
        Entrega entrega = entregaService.confirmarEntrega(id);
        if(entrega == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entrega);
    }

    @PatchMapping("/estraviar/{id}")
    public ResponseEntity<?> adicionarItem(@PathVariable Long id) {
        Entrega entrega = entregaService.entregaEstraviada(id);
        if(entrega == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entrega);
    }

    @PutMapping
    public ResponseEntity<?> update(Long id, @RequestBody Entrega entrega) {
        Entrega entregaSalvo = entregaService.update(id, entrega);
        if(entregaSalvo == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entregaSalvo);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(Long id) {
        entregaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
