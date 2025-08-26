// src/main/java/br/comtotempoo/totempoo/strategy/ProcessadorPagamentoStrategy.java
package br.comtotempoo.totempoo.strategy;

import br.comtotempoo.totempoo.model.Pedido;

/**
 * Interface Strategy para processar diferentes tipos de pagamento.
 * Define o contrato para como cada forma de pagamento deve ser "processada"
 * ou quais instruções ela deve fornecer ao cliente.
 * Isso nos permite adicionar novas formas de pagamento facilmente,
 * sem modificar o código existente (princípio Open/Closed).
 */
public interface ProcessadorPagamentoStrategy {

    /**
     * Retorna o tipo de pagamento associado a esta estratégia.
     * @return Uma String representando o tipo de pagamento (ex: "Dinheiro", "Pix").
     */
    String getTipoPagamento();

    /**
     * Retorna as instruções específicas para o cliente realizar o pagamento.
     * @param pedido O pedido que está sendo pago, caso as instruções dependam de detalhes do pedido.
     * @return Uma String com as instruções para o cliente.
     */
    String getInstrucoesPagamento(Pedido pedido);

    /**
     * Método para simular o processamento real do pagamento.
     * Em um sistema real, isso envolveria chamadas a APIs de pagamento,
     * validações, etc. Aqui, é apenas um placeholder.
     * @param pedido O pedido a ser processado.
     * @return true se o pagamento foi "processado" com sucesso, false caso contrário.
     */
    boolean processarPagamento(Pedido pedido);
}
