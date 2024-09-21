package com.infnet.pedidosservice.controller;

import com.infnet.pedidosservice.model.Item;
import com.infnet.pedidosservice.model.Pedido;
import com.infnet.pedidosservice.repository.PedidoRepository;
import com.infnet.pedidosservice.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getAll() {
        List<Pedido> pedidos = pedidoService.getAll();
        if(pedidos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.getById(id);
        if(pedido.isPresent()){
            return ResponseEntity.ok(pedido.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Pedido pedido) {
        Pedido pedidoSalvo = pedidoService.save(pedido);
        if(pedidoSalvo == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidoSalvo);
    }

    @PatchMapping("/fechar/{id}")
    public ResponseEntity<?> fecharPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.fecharPedido(id);
        if(pedido == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.cancelarPedido(id);
        if(pedido == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("/adicionar/{id}")
    public ResponseEntity<?> adicionarItem(@PathVariable Long id, @RequestBody Item item) {
        Pedido pedido = pedidoService.adicionarItem(id, item);
        if(pedido == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @PutMapping
    public ResponseEntity<?> update(Long id, @RequestBody Pedido pedido) {
        Pedido pedidoSalvo = pedidoService.update(id, pedido);
        if(pedidoSalvo == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedidoSalvo);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(Long id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
