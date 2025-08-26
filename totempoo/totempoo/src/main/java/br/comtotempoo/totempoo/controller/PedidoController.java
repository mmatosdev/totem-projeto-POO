package br.comtotempoo.totempoo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.comtotempoo.totempoo.model.Pedido;
import br.comtotempoo.totempoo.service.PedidoArquivoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoArquivoService pedidoArquivoService;

    public PedidoController(PedidoArquivoService pedidoArquivoService) {
        this.pedidoArquivoService = pedidoArquivoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Pedido novoPedido = pedidoArquivoService.criarPedido(pedido);
        return ResponseEntity.ok(novoPedido);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = pedidoArquivoService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editarPedido(@PathVariable Long id, @RequestBody Pedido pedidoAtualizado) {
        boolean sucesso = pedidoArquivoService.editarPedido(id, pedidoAtualizado);
        if (sucesso) {
            return ResponseEntity.ok("Pedido atualizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }
    }

    @GetMapping("/totem")
    public String inicio() {
        return "Página inicial do totem";
    }
}
