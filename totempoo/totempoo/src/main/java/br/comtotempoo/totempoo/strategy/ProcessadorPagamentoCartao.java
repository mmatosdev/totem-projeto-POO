// src/main/java/br/comtotempoo/totempoo/strategy/ProcessadorPagamentoCartao.java
package br.comtotempoo.totempoo.strategy;

import org.springframework.stereotype.Component;

import br.comtotempoo.totempoo.model.Pedido;
import br.comtotempoo.totempoo.model.TipoPagamento;

@Component
public class ProcessadorPagamentoCartao implements ProcessadorPagamentoStrategy {

    @Override
    public String getTipoPagamento() {
        return TipoPagamento.CARTAO.name();
    }

    @Override
    public String getInstrucoesPagamento(Pedido pedido) {
        return "Insira seu cartão e confirme a operação.";
    }

    @Override
    public boolean processarPagamento(Pedido pedido) {
        return true;
    }
}
