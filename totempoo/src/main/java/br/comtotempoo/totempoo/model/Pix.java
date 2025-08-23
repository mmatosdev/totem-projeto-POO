package br.comtotempoo.totempoo.model;

public class Pix extends Pagamento {
    @Override
    public String getTipo() { return "Pix"; }
    @Override
    public String getInstrucao() { return "Escaneie o QR Code para pagar."; }
}
