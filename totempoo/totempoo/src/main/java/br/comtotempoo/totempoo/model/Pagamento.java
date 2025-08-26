// src/main/java/br/comtotempoo/totempoo/model/Pagamento.java
package br.comtotempoo.totempoo.model;

/**
 * Classe que representa um pagamento em um pedido.
 * Esta classe pode ser utilizada para armazenar informações comuns
 * sobre pagamentos, como ID de transação, status, e outros detalhes.
 */
public class Pagamento {

    private Long id; // ID único do pagamento
    private TipoPagamento tipo; // Tipo de pagamento (DINHEIRO, PIX, CARTAO)
    private String status; // Status do pagamento (PENDENTE, CONCLUÍDO, CANCELADO)
    private String instrucoes; // Instruções específicas para o pagamento

    // Construtor padrão
    public Pagamento() {
    }

    // Construtor com parâmetros
    public Pagamento(Long id, TipoPagamento tipo, String status, String instrucoes) {
        this.id = id;
        this.tipo = tipo;
        this.status = status;
        this.instrucoes = instrucoes;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoPagamento tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstrucoes() {
        return instrucoes;
    }

    public void setInstrucoes(String instrucoes) {
        this.instrucoes = instrucoes;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", status='" + status + '\'' +
                ", instrucoes='" + instrucoes + '\'' +
                '}';
    }
}

