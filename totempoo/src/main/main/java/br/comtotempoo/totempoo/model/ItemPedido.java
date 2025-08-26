package br.comtotempoo.totempoo.model;

import java.io.Serializable;
import java.util.List;

public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Produto produto;
    private int quantidade;

    public ItemPedido() {}

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return produto != null ? produto.getPreco() * quantidade : 0.0;
    }

    @Override
    public String toString() {
        return produto.getId() + ":" + quantidade;
    }

    public static ItemPedido fromString(String itemString, List<Produto> produtos) {
        String[] parts = itemString.split(":");
        if (parts.length == 2) {
            try {
                int quantidade = Integer.parseInt(parts[1]);
                Produto produto = produtos.stream()
                                          .filter(p -> p.getId().equals(Long.parseLong(parts[0])))
                                          .findFirst()
                                          .orElse(null);
                if (produto != null) {
                    return new ItemPedido(produto, quantidade);
                }
            } catch (NumberFormatException e) {
                // Ignora erro de formato
            }
        }
        return null;
    }
}
