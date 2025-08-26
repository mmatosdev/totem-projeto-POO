// src/main/java/br/comtotempoo/totempoo/strategy/DinheiroProcessador.java
package br.comtotempoo.totempoo.strategy;

import org.springframework.stereotype.Component;

import br.comtotempoo.totempoo.model.Pedido;

/**
 * Implementação da estratégia para pagamento em Dinheiro.
 * Fornece as instruções e simula o processamento para esta forma de pagamento.
 */
@Component // Marca como um componente Spring para ser injetado automaticamente
public class DinheiroProcessador implements ProcessadorPagamentoStrategy {

    @Override
    public String getTipoPagamento() {
        return "Dinheiro";
    }

    @Override
    public String getInstrucoesPagamento(Pedido pedido) {
        return "Por favor, dirija-se ao caixa para concluir o pagamento de R$ " +
               String.format("%.2f", pedido.getTotal()) + ".";
    }

    @Override
    public boolean processarPagamento(Pedido pedido) {
        // Lógica real para pagamento em dinheiro (ex: notificar caixa, aguardar confirmação)
        // Por enquanto, apenas simulamos o sucesso.
        System.out.println("Processando pagamento em dinheiro para o pedido #" + pedido.getNumero());
        return true; // Sempre "sucesso" na simulação
    }
}
