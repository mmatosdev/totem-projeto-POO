package br.comtotempoo.totempoo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.comtotempoo.totempoo.model.Cliente;
import br.comtotempoo.totempoo.model.ItemPedido;
import br.comtotempoo.totempoo.model.Pedido;
import br.comtotempoo.totempoo.model.Produto;
import br.comtotempoo.totempoo.model.TipoPagamento;
import br.comtotempoo.totempoo.repository.ProdutoRepository;

/**
 * Classe de teste para o serviço PedidoArquivoService.
 * Utiliza JUnit 5 e Mockito para testar a lógica de manipulação de pedidos em arquivo.
 */
class PedidoArquivoServiceTest {

    // Cria um diretório temporário para os testes de arquivo
    @TempDir
    Path tempDir;

    // Simula a dependência ProdutoRepository
    @Mock
    private ProdutoRepository produtoRepository;

    // Injeta os mocks na classe de serviço que está sendo testada
    @InjectMocks
    private PedidoArquivoService pedidoArquivoService;

    // Referência para o arquivo de pedidos temporário
    private File tempPedidosFile;

    /**
     * Configuração inicial para cada teste.
     * Inicializa os mocks e define o arquivo temporário para o serviço.
     */
    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        // Define o caminho do arquivo de pedidos dentro do diretório temporário
        tempPedidosFile = tempDir.resolve("pedidos.txt").toFile();
        pedidoArquivoService.setArquivo(tempPedidosFile);

        // Garante que o arquivo está limpo antes de cada teste
        if (tempPedidosFile.exists()) {
            Files.write(tempPedidosFile.toPath(), new byte[0]);
        }

        // Mocka o comportamento do repositório para retornar produtos específicos
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(new Produto(1L, "Pão", "INGREDIENTE", 2.0)));
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(new Produto(2L, "Carne", "INGREDIENTE", 10.0)));
        when(produtoRepository.findById(3L)).thenReturn(Optional.of(new Produto(3L, "Coca-Cola", "BEBIDA", 5.0)));
    }

    /**
     * Testa a criação de um novo pedido.
     * Verifica se o pedido é criado com informações válidas e se é salvo corretamente.
     */
    @Test
    void testCriarPedido() {
        // Cria um pedido de teste
        Pedido pedido = new Pedido();
        pedido.setCliente(new Cliente("Teste Cliente", "11122233344"));
        pedido.adicionarItem(new ItemPedido(new Produto(1L, "Pão", "INGREDIENTE", 2.0), 1));
        pedido.adicionarItem(new ItemPedido(new Produto(2L, "Carne", "INGREDIENTE", 10.0), 1));
        pedido.setTotal(12.0);

        // Chama o método que queremos testar
        Pedido novoPedido = pedidoArquivoService.criarPedido(pedido);

        // Valida se as informações do novo pedido estão corretas
        assertNotNull(novoPedido.getId());
        assertTrue(novoPedido.getNumero() > 0);
        assertNotNull(novoPedido.getDataHora());
        assertEquals("Teste Cliente", novoPedido.getCliente().getNome());

        // Valida se o pedido foi realmente salvo no arquivo
        List<Pedido> pedidosSalvos = pedidoArquivoService.listarTodos();
        assertFalse(pedidosSalvos.isEmpty());
        assertEquals(1, pedidosSalvos.size());
        assertEquals(novoPedido.getId(), pedidosSalvos.get(0).getId());
    }

    /**
     * Testa a listagem de todos os pedidos salvos.
     * Verifica se a lista retornada contém todos os pedidos criados.
     */
    @Test
    void testListarTodosPedidos() {
        // Cria e salva dois pedidos
        Pedido pedido1 = new Pedido();
        pedido1.setCliente(new Cliente("Cliente Um", "111"));
        pedido1.adicionarItem(new ItemPedido(new Produto(1L, "Pão", "INGREDIENTE", 2.0), 1));
        pedido1.setTotal(2.0);
        pedidoArquivoService.criarPedido(pedido1);

        Pedido pedido2 = new Pedido();
        pedido2.setCliente(new Cliente("Cliente Dois", "222"));
        pedido2.adicionarItem(new ItemPedido(new Produto(3L, "Coca-Cola", "BEBIDA", 5.0), 2));
        pedido2.setTotal(10.0);
        pedidoArquivoService.criarPedido(pedido2);

        // Lista todos os pedidos e verifica se o resultado é o esperado
        List<Pedido> pedidos = pedidoArquivoService.listarTodos();
        assertNotNull(pedidos);
        assertEquals(2, pedidos.size());
        assertEquals("Cliente Um", pedidos.get(0).getCliente().getNome());
        assertEquals("Cliente Dois", pedidos.get(1).getCliente().getNome());
    }

    /**
     * Testa a edição de um pedido existente.
     * Verifica se as informações do pedido são atualizadas corretamente.
     */
    @Test
    void testEditarPedido() {
        // Cria e salva um pedido para ser editado
        Pedido pedidoOriginal = new Pedido();
        pedidoOriginal.setCliente(new Cliente("Cliente Original", "123"));
        pedidoOriginal.adicionarItem(new ItemPedido(new Produto(1L, "Pão", "INGREDIENTE", 2.0), 1));
        pedidoOriginal.setTotal(2.0);
        Pedido pedidoSalvo = pedidoArquivoService.criarPedido(pedidoOriginal);

        // Cria um objeto de pedido com os novos dados
        Pedido pedidoAtualizado = new Pedido();
        pedidoAtualizado.setCliente(new Cliente("Cliente Editado", "456"));
        pedidoAtualizado.adicionarItem(new ItemPedido(new Produto(2L, "Carne", "INGREDIENTE", 10.0), 2));
        pedidoAtualizado.setTotal(20.0);
        pedidoAtualizado.setPagamento(TipoPagamento.PIX);

        // Chama o método de edição
        boolean sucesso = pedidoArquivoService.editarPedido(pedidoSalvo.getId(), pedidoAtualizado);

        // Valida se a edição foi bem-sucedida e se os dados foram atualizados
        assertTrue(sucesso);
        List<Pedido> pedidos = pedidoArquivoService.listarTodos();
        assertEquals(1, pedidos.size());
        Pedido pedidoVerificado = pedidos.get(0);

        assertEquals("Cliente Editado", pedidoVerificado.getCliente().getNome());
        assertEquals("456", pedidoVerificado.getCliente().getCpf());
        assertEquals(20.0, pedidoVerificado.getTotal());
        assertEquals(TipoPagamento.PIX, pedidoVerificado.getPagamento());
        assertEquals(1, pedidoVerificado.getItens().size());
        assertEquals("Carne", pedidoVerificado.getItens().get(0).getProduto().getNome());
        assertEquals(2, pedidoVerificado.getItens().get(0).getQuantidade());
    }

    /**
     * Testa a tentativa de edição de um pedido que não existe.
     * Verifica se o método retorna 'false' e se a lista de pedidos permanece inalterada.
     */
    @Test
    void testEditarPedidoNaoExistente() {
        // Cria um pedido fictício que não será salvo
        Pedido pedidoFicticio = new Pedido();
        pedidoFicticio.setCliente(new Cliente("Inexistente", "000"));
        pedidoFicticio.setTotal(1.0);

        // Tenta editar um ID que não existe
        boolean sucesso = pedidoArquivoService.editarPedido(999L, pedidoFicticio);

        // Valida que a operação falhou e que a lista de pedidos está vazia
        assertFalse(sucesso);
        assertTrue(pedidoArquivoService.listarTodos().isEmpty());
    }
}
