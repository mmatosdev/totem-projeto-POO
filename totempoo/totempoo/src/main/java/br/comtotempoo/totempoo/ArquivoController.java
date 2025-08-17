package br.comtotempoo.totempoo;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArquivoController {

    private final LeitorDeArquivo leitor;

    public ArquivoController(LeitorDeArquivo leitor) {
        this.leitor = leitor;
    }

    @GetMapping("/ler-arquivo")
    public String lerArquivo() throws IOException {
        return leitor.ler();
    }

    @PostMapping("/salvar-arquivo")
    public String salvarArquivo(@RequestParam String conteudo) {
        return leitor.gravar(conteudo);
    }
}
