package br.comtotempoo.totempoo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import br.comtotempoo.totempoo.model.Cliente;
import br.comtotempoo.totempoo.model.ItemPedido;
import br.comtotempoo.totempoo.model.Pedido;
import br.comtotempoo.totempoo.model.Produto;
import br.comtotempoo.totempoo.model.TipoPagamento;
import br.comtotempoo.totempoo.repository.ProdutoRepository;
import br.comtotempoo.totempoo.service.PedidoArquivoService;

@Controller
@SessionAttributes("pedidoEmConstrucao") // Guarda o pedido temporariamente na sessão
public class TotemController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoArquivoService pedidoService;

    @ModelAttribute("pedidoEmConstrucao")
    public Pedido setupPedidoForm() {
        return new Pedido();
    }

    @GetMapping("/")
    public String index() { return "redirect:/totem"; }

    @GetMapping("/totem")
    public String inicio(SessionStatus status) {
        status.setComplete(); // Limpa pedido antigo da sessão
        return "totem-inicio";
    }

    @GetMapping("/cadastro-cliente")
    public String cadastroCliente() { return "cadastro-cliente"; }

    @PostMapping("/salvar-cliente")
    public String salvarCliente(@RequestParam String nome,
                                @RequestParam String cpf,
                                @ModelAttribute("pedidoEmConstrucao") Pedido pedido) {
        pedido.setCliente(new Cliente(nome, cpf));
        return "redirect:/selecionar-hamburguer";
    }

    @GetMapping("/selecionar-hamburguer")
    public String selecionarHamburguer(Model model) {
        model.addAttribute("ingredientes", produtoRepository.findByTipo("INGREDIENTE"));
        return "selecionar-hamburguer";
    }

    @PostMapping("/adicionar-hamburguer")
    public String adicionarHamburguer(@RequestParam Map<String, String> quantidades,
                                      @ModelAttribute("pedidoEmConstrucao") Pedido pedido) {
        adicionarItensAoPedido(quantidades, pedido.getItens());
        return "redirect:/selecionar-bebida";
    }

    @GetMapping("/selecionar-bebida")
    public String selecionarBebida(Model model) {
        model.addAttribute("bebidas", produtoRepository.findByTipo("BEBIDA"));
        return "selecionar-bebida";
    }

    @PostMapping("/adicionar-bebida")
    public String adicionarBebida(@RequestParam Map<String, String> quantidades,
                                  @ModelAttribute("pedidoEmConstrucao") Pedido pedido) {
        adicionarItensAoPedido(quantidades, pedido.getItens());
        return "redirect:/selecionar-acompanhamento";
    }

    @GetMapping("/selecionar-acompanhamento")
    public String selecionarAcompanhamento(Model model) {
        model.addAttribute("acompanhamentos", produtoRepository.findByTipo("ACOMPANHAMENTO"));
        return "selecionar-acompanhamento";
    }

    @PostMapping("/adicionar-acompanhamento")
    public String adicionarAcompanhamento(@RequestParam Map<String, String> quantidades,
                                          @ModelAttribute("pedidoEmConstrucao") Pedido pedido) {
        adicionarItensAoPedido(quantidades, pedido.getItens());
        return "redirect:/resumo-pedido";
    }

    @GetMapping("/resumo-pedido")
    public String resumoPedido(@ModelAttribute("pedidoEmConstrucao") Pedido pedido, Model model) {
        double total = pedido.getItens().stream().mapToDouble(ItemPedido::getSubtotal).sum();
        pedido.setTotal(total);
        model.addAttribute("pedido", pedido);
        return "resumo-pedido";
    }

    @GetMapping("/selecionar-pagamento")
    public String selecionarPagamento() { return "selecionar-pagamento"; }

    @PostMapping("/confirmar-pedido")
    public String confirmarPedido(@RequestParam("tipoPagamento") TipoPagamento tipoPagamento,
                                  @ModelAttribute("pedidoEmConstrucao") Pedido pedido,
                                  Model model,
                                  SessionStatus status) {
        pedido.setPagamento(tipoPagamento);
        pedidoService.criarPedido(pedido); // Salva o pedido no arquivo
        model.addAttribute("pedido", pedido);
        status.setComplete(); // Limpa sessão
        return "confirmacao";
    }

    @GetMapping("/cancelar-pedido")
    public String cancelarPedido(SessionStatus status) {
        status.setComplete();
        return "redirect:/totem";
    }

    private void adicionarItensAoPedido(Map<String, String> quantidades, List<ItemPedido> itens) {
        for (Map.Entry<String, String> entry : quantidades.entrySet()) {
            if (entry.getKey().startsWith("quantidade_")) {
                try {
                    int qtd = Integer.parseInt(entry.getValue());
                    if (qtd > 0) {
                        Long produtoId = Long.parseLong(entry.getKey().substring("quantidade_".length()));
                        Produto produto = produtoRepository.findById(produtoId).orElse(null);
                        if (produto != null) {
                            itens.add(new ItemPedido(produto, qtd));
                        }
                    }
                } catch (NumberFormatException e) { /* Ignora se inválido */ }
            }
        }
    }
}
