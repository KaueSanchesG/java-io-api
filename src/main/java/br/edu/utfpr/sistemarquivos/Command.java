package br.edu.utfpr.sistemarquivos;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

import static br.edu.utfpr.sistemarquivos.Application.ROOT;

public enum Command {

    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {

            // TODO implementar conforme enunciado
            try (DirectoryStream<Path> dir = Files.newDirectoryStream(path)) {
                System.out.println("Contents of " + path.toAbsolutePath());
                for (Path p : dir) {
                    System.out.println(" " + p.getFileName());
                }
            }
            return path;
        }
    },
    SHOW() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
        }

        @Override
        Path execute(Path path) throws IOException {

            // TODO implementar conforme enunciado
            if (parameters.length < 2) {
                throw new UnsupportedOperationException("this command needs a param");
            }

            String nomeArq = parameters[1];
            Path concat = path.resolve(nomeArq);

            if (!Files.exists(concat)) {
                throw new UnsupportedOperationException("file not found");
            }
            if (Files.isDirectory(concat)) {
                throw new UnsupportedOperationException("this command should be used with files only");
            }
            if (!nomeArq.toLowerCase().endsWith(".txt")) {
                throw new UnsupportedOperationException("Extension not supported");
            }


            new FileReader().read(concat);
            return path;
        }
    },
    BACK() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
        }

        @Override
        Path execute(Path path) {

            // TODO implementar conforme enunciado
            Path raiz = Paths.get(ROOT).toAbsolutePath().normalize();

            Path dirPai = path.getParent();

            if (dirPai == null) {
                return path;
            }
            if (!dirPai.startsWith(raiz)) {
                return path;
            }

            dirPai = dirPai.normalize();


            return dirPai;
        }
    },
    OPEN() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
        }

        @Override
        Path execute(Path path) {

            // TODO implementar conforme enunciado
            if (parameters.length < 2) {
                throw new UnsupportedOperationException("this command needs a param");
            }

            String dir = parameters[1].trim();
            Path reDir = path.resolve(dir).normalize();

            if (!Files.exists(reDir)) {
                throw new UnsupportedOperationException("path not found");
            }
            if (!Files.isDirectory(reDir)) {
                throw new UnsupportedOperationException("path is not a directory");
            }

            return reDir;
        }
    },
    DETAIL() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
        }

        @Override
        Path execute(Path path) throws IOException {

            // TODO implementar conforme enunciado
            if (parameters.length < 2) {
                throw new UnsupportedOperationException("this command needs a param");
            }

            String arq = parameters[1].trim();
            Path dir = path.resolve(arq).normalize();

            if (!Files.exists(dir)) {
                throw new UnsupportedOperationException("file not found");
            }
            BasicFileAttributeView view = Files.getFileAttributeView(dir, BasicFileAttributeView.class);
            BasicFileAttributes attrs = view.readAttributes();

            System.out.println("Is directry " + attrs.isDirectory());
            System.out.println("Size " + attrs.size());
            System.out.println("Created on " + attrs.creationTime());
            System.out.println("last access time " + attrs.lastAccessTime());

            return path;
        }
    },
    EXIT() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
        }

        @Override
        Path execute(Path path) {
            System.out.print("Saindo...");
            return path;
        }

        @Override
        boolean shouldStop() {
            return true;
        }
    };

    abstract Path execute(Path path) throws IOException;

    abstract boolean accept(String command);

    void setParameters(String[] parameters) {
    }

    boolean shouldStop() {
        return false;
    }

    public static Command parseCommand(String commandToParse) {

        if (commandToParse.isBlank()) {
            throw new UnsupportedOperationException("Type something...");
        }

        final var possibleCommands = values();

        for (Command possibleCommand : possibleCommands) {
            if (possibleCommand.accept(commandToParse)) {
                possibleCommand.setParameters(commandToParse.split(" "));
                return possibleCommand;
            }
        }

        throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
    }
}
