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
import org.springframework.stereotype.Service;

import br.comtotempoo.totempoo.model.Cliente;
import br.comtotempoo.totempoo.model.Pedido;

@Service
public class PedidoArquivoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoArquivoService.class);

    // Arquivo onde os pedidos serão salvos
    private final File arquivo = new File("pedidos.txt");

    // Contador para o número do pedido (visível para o cliente)
    private final AtomicInteger contador = new AtomicInteger(1);

    // Contador para o ID interno (chave única do pedido)
    private long proximoId = 1;

    /**
     * Cria um novo pedido, atribui ID e número sequenciais e salva no arquivo.
     */
    public Pedido criarPedido(Pedido pedido) {
        pedido.setId(proximoId++);
        pedido.setNumero(contador.getAndIncrement());
        pedido.setDataHora(LocalDateTime.now());
        salvarPedido(pedido);
        return pedido;
    }

    /**
     * Lista todos os pedidos salvos no arquivo.
     */
    public List<Pedido> listarTodos() {
        return lerPedidos();
    }

    /**
     * Edita um pedido existente pelo ID.
     * Retorna true se encontrou e atualizou, false se não encontrou.
     */
    public boolean editarPedido(Long id, Pedido pedidoAtualizado) {
        List<Pedido> pedidos = lerPedidos();
        boolean encontrado = false;

        for (int i = 0; i < pedidos.size(); i++) {
            Pedido p = pedidos.get(i);
            if (p.getId().equals(id)) {
                // Atualiza os campos desejados
                p.setCliente(pedidoAtualizado.getCliente());
                p.setIngredientes(pedidoAtualizado.getIngredientes());
                p.setBebida(pedidoAtualizado.getBebida());
                p.setPagamento(pedidoAtualizado.getPagamento());
                p.setDataHora(LocalDateTime.now()); // opcional: atualizar data/hora
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            salvarTodosPedidos(pedidos); // regrava o arquivo inteiro
        }

        return encontrado;
    }

    /**
     * Salva um pedido no arquivo de texto, separando os campos por ponto e vírgula.
     */
    private void salvarPedido(Pedido pedido) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            String linha = pedido.getId() + ";" +
                           pedido.getNumero() + ";" +
                           pedido.getCliente().getNome() + ";" +
                           pedido.getCliente().getCpf() + ";" +
                           String.join(",", pedido.getIngredientes()) + ";" +
                           pedido.getBebida() + ";" +
                           pedido.getDataHora();
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            logger.error("Erro ao salvar pedido no arquivo {}", arquivo.getAbsolutePath(), e);
        }
    }

    /**
     * Sobrescreve o arquivo com a lista completa de pedidos.
     */
    private void salvarTodosPedidos(List<Pedido> pedidos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, false))) {
            for (Pedido pedido : pedidos) {
                String linha = pedido.getId() + ";" +
                               pedido.getNumero() + ";" +
                               pedido.getCliente().getNome() + ";" +
                               pedido.getCliente().getCpf() + ";" +
                               String.join(",", pedido.getIngredientes()) + ";" +
                               pedido.getBebida() + ";" +
                               pedido.getDataHora();
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Erro ao salvar pedidos no arquivo {}", arquivo.getAbsolutePath(), e);
        }
    }

    /**
     * Lê todos os pedidos do arquivo, ignorando linhas vazias ou mal formatadas.
     */
    private List<Pedido> lerPedidos() {
        List<Pedido> pedidos = new ArrayList<>();

        if (!arquivo.exists()) {
            return pedidos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {

                // Ignora linhas em branco
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(";");

                // Valida se a linha tem todos os campos esperados
                if (partes.length < 7 || partes[0].trim().isEmpty()) {
                    logger.warn("Linha inválida no arquivo de pedidos: {}", linha);
                    continue;
                }

                Pedido pedido = new Pedido();
                pedido.setId(Long.parseLong(partes[0]));
                pedido.setNumero(Integer.parseInt(partes[1]));
                Cliente cliente = new Cliente(partes[2], partes[3]);
                pedido.setCliente(cliente);
                pedido.setIngredientes(List.of(partes[4].split(",")));
                pedido.setBebida(partes[5]);
                pedido.setDataHora(LocalDateTime.parse(partes[6]));

                pedidos.add(pedido);

                // Atualiza contadores para não repetir IDs/números
                if (pedido.getId() >= proximoId) {
                    proximoId = pedido.getId() + 1;
                }
                if (pedido.getNumero() >= contador.get()) {
                    contador.set(pedido.getNumero() + 1);
                }
            }
        } catch (IOException e) {
            logger.error("Erro ao ler pedidos do arquivo {}", arquivo.getAbsolutePath(), e);
        }

        return pedidos;
    }
}
