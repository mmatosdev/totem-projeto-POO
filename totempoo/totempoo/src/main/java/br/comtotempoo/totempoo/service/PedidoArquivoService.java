package br.comtotempoo.totempoo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.comtotempoo.totempoo.model.Cliente;
import br.comtotempoo.totempoo.model.ItemPedido;
import br.comtotempoo.totempoo.model.Pedido;
import br.comtotempoo.totempoo.model.Produto;
import br.comtotempoo.totempoo.model.TipoPagamento;
import br.comtotempoo.totempoo.repository.ProdutoRepository;

/**
 * Serviço responsável por gerenciar a persistência de pedidos em arquivo de texto.
 * Ele funciona como um "banco de dados simples" usando o arquivo "pedidos.txt".
 */
@Service
public class PedidoArquivoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoArquivoService.class);

    @Autowired
    private ProdutoRepository produtoRepository; // Para buscar os produtos ao ler os pedidos

    private File arquivo = new File("pedidos.txt"); // Arquivo onde os pedidos serão salvos
    private final AtomicInteger contador = new AtomicInteger(1); // Para gerar números sequenciais de pedidos
    private long proximoId = 1; // Para gerar IDs únicos

    // Permite alterar o arquivo (útil para testes)
    public void setArquivo(File arquivo) {
        this.arquivo = arquivo;
        this.contador.set(1);
        this.proximoId = 1;
    }

    // Cria um pedido e já salva no arquivo
    public Pedido criarPedido(Pedido pedido) {
        pedido.setId(proximoId++);
        pedido.setNumero(contador.getAndIncrement());
        pedido.setDataHora(LocalDateTime.now());
        salvarPedido(pedido);
        return pedido;
    }

    // Lista todos os pedidos salvos
    public List<Pedido> listarTodos() {
        return lerPedidos();
    }

    // Edita um pedido pelo ID
    public boolean editarPedido(Long id, Pedido pedidoAtualizado) {
        List<Pedido> pedidos = lerPedidos();
        boolean encontrado = false;

        for (Pedido p : pedidos) {
            if (p.getId().equals(id)) {
                p.setCliente(pedidoAtualizado.getCliente());
                p.setItens(pedidoAtualizado.getItens());
                p.setPagamento(pedidoAtualizado.getPagamento());
                p.setTotal(pedidoAtualizado.getTotal());
                p.setDataHora(LocalDateTime.now());
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            salvarTodosPedidos(pedidos);
        }

        return encontrado;
    }

    // Salva um único pedido no arquivo (adiciona no final)
    private void salvarPedido(Pedido pedido) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {

            String itensString = pedido.getItens().stream()
                    .map(item -> item.getProduto().getId() + ":" + item.getQuantidade())
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");

            String linha = pedido.getId() + ";" +
                    pedido.getNumero() + ";" +
                    pedido.getCliente().getNome() + ";" +
                    pedido.getCliente().getCpf() + ";" +
                    itensString + ";" +
                    pedido.getTotal() + ";" +
                    (pedido.getPagamento() != null ? pedido.getPagamento().name() : "") + ";" +
                    pedido.getDataHora();

            writer.write(linha);
            writer.newLine();

        } catch (IOException e) {
            logger.error("Erro ao salvar pedido no arquivo {}. Detalhes: {}", arquivo.getAbsolutePath(), e.getMessage(), e);
        }
    }

    // Salva toda a lista de pedidos (sobrescreve o arquivo)
    private void salvarTodosPedidos(List<Pedido> pedidos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, false))) {
            for (Pedido pedido : pedidos) {
                String itensString = pedido.getItens().stream()
                        .map(item -> item.getProduto().getId() + ":" + item.getQuantidade())
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");

                String linha = pedido.getId() + ";" +
                        pedido.getNumero() + ";" +
                        pedido.getCliente().getNome() + ";" +
                        pedido.getCliente().getCpf() + ";" +
                        itensString + ";" +
                        pedido.getTotal() + ";" +
                        (pedido.getPagamento() != null ? pedido.getPagamento().name() : "") + ";" +
                        pedido.getDataHora();

                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Erro ao salvar todos os pedidos no arquivo {}. Detalhes: {}", arquivo.getAbsolutePath(), e.getMessage(), e);
        }
    }

    // Lê todos os pedidos do arquivo e reconstrói os objetos
    private List<Pedido> lerPedidos() {
        List<Pedido> pedidos = new ArrayList<>();

        if (!arquivo.exists()) {
            return pedidos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] partes = linha.split(";");

                if (partes.length < 8) {
                    logger.warn("Linha inválida no arquivo de pedidos (formato incorreto): {}", linha);
                    continue;
                }

                try {
                    Pedido pedido = new Pedido();
                    pedido.setId(Long.valueOf(partes[0]));
                    pedido.setNumero(Integer.parseInt(partes[1]));

                    Cliente cliente = new Cliente(partes[2], partes[3]);
                    pedido.setCliente(cliente);

                    String itensString = partes[4];
                    List<ItemPedido> itens = new ArrayList<>();
                    if (!itensString.isEmpty()) {
                        for (String itemStr : itensString.split(",")) {
                            String[] itemParts = itemStr.split(":");
                            if (itemParts.length == 2) {
                                Long produtoId = Long.parseLong(itemParts[0]);
                                int quantidade = Integer.parseInt(itemParts[1]);
                                Produto produto = produtoRepository.findById(produtoId).orElse(null);
                                if (produto != null) {
                                    itens.add(new ItemPedido(produto, quantidade));
                                } else {
                                    logger.warn("Produto com ID {} não encontrado ao ler pedido. Item será ignorado.", produtoId);
                                }
                            } else {
                                logger.warn("Formato de item inválido no arquivo de pedidos: {}", itemStr);
                            }
                        }
                    }
                    pedido.setItens(itens);

                    pedido.setTotal(Double.parseDouble(partes[5]));

                    if (!partes[6].isEmpty()) {
                        pedido.setPagamento(TipoPagamento.valueOf(partes[6]));
                    }

                    pedido.setDataHora(LocalDateTime.parse(partes[7]));

                    pedidos.add(pedido);

                    // Atualiza contadores para novos pedidos
                    if (pedido.getId() >= proximoId) proximoId = pedido.getId() + 1;
                    if (pedido.getNumero() >= contador.get()) contador.set(pedido.getNumero() + 1);

                } catch (NumberFormatException e) {
                    logger.warn("Erro de formato numérico ao ler linha do pedido: {}. Detalhes: {}", linha, e.getMessage());
                } catch (IllegalArgumentException e) {
                    logger.warn("Erro ao converter tipo de pagamento ou data/hora na linha: {}. Detalhes: {}", linha, e.getMessage());
                }
            }

        } catch (IOException e) {
            logger.error("Erro ao ler pedidos do arquivo {}. Detalhes: {}", arquivo.getAbsolutePath(), e.getMessage(), e);
        }

        return pedidos;
    }
}
