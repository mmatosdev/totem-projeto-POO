package br.comtotempoo.totempoo.model;

public class Dinheiro extends Pagamento {
    @Override
    public String getTipo() { return "Dinheiro"; }
    @Override
    public String getInstrucao() { return "Dirija-se ao caixa para pagar."; }
}
