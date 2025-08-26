package br.comtotempoo.totempoo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Modelo que representa um pedido
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id; // ID único do pedido
    private int numero; // Número sequencial do pedido
    private LocalDateTime dataHora; // Data e hora do pedido
    private Cliente cliente; // Cliente que fez o pedido
    private List<ItemPedido> itens; // Lista de itens (hambúrguer, bebida, etc.)
    private double total; // Total do pedido
    private TipoPagamento pagamento; // Tipo de pagamento escolhido

    public Pedido() {
        this.itens = new ArrayList<>();
        this.dataHora = LocalDateTime.now();
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public TipoPagamento getPagamento() { return pagamento; }
    public void setPagamento(TipoPagamento pagamento) { this.pagamento = pagamento; }

    // Adiciona um item à lista de itens do pedido
    public void adicionarItem(ItemPedido item) { this.itens.add(item); }
}
