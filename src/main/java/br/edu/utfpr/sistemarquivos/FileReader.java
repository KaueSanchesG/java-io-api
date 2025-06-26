package br.edu.utfpr.sistemarquivos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileReader {

    public void read(Path path) throws IOException {
        // TODO implementar a leitura dos arquivos do PATH aqui
        try (Stream<String> linhas = Files.lines(path)){
            linhas.forEach(System.out::println);
        }
    }
}
