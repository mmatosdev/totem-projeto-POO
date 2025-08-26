// src/main/java/br/comtotempoo/totempoo/strategy/ProcessadorPagamentoPix.java
package br.comtotempoo.totempoo.strategy;

import org.springframework.stereotype.Component;

import br.comtotempoo.totempoo.model.Pedido;
import br.comtotempoo.totempoo.model.TipoPagamento;

@Component
public class PixProcessador implements ProcessadorPagamentoStrategy {

    @Override
    public String getTipoPagamento() {
        return TipoPagamento.PIX.name(); // ou "PIX"
    }

    @Override
    public String getInstrucoesPagamento(Pedido pedido) {
        return "Use o QR Code para pagar via PIX.";
    }

    @Override
    public boolean processarPagamento(Pedido pedido) {
        // Aqui você pode simular ou validar o pagamento.
        return true;
    }
}
