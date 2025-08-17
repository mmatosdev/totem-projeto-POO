package br.comtotempoo.totempoo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Service;

@Service
public class LeitorDeArquivo {

    private static final String ARQUIVO = "dados.txt";

    // Lê todo o conteúdo do arquivo como String
    public String ler() throws IOException {
        Path caminho = Paths.get(ARQUIVO);
        if (!Files.exists(caminho)) {
            return "Arquivo ainda não existe.";
        }
        return Files.readString(caminho);
    }

    // Grava conteúdo no arquivo (acrescentando nova linha)
    public String gravar(String conteudo) {
        try {
            Files.write(Paths.get(ARQUIVO),
                    (conteudo + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            return "Conteúdo salvo com sucesso!";
        } catch (IOException e) {
            return "Erro ao salvar: " + e.getMessage();
        }
    }
}
