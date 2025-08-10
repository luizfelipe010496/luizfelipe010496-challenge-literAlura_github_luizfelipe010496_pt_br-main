package br.com.alura.challenge.luiz.literalura_app.principal;

import br.com.alura.challenge.luiz.literalura_app.model.Autor;
import br.com.alura.challenge.luiz.literalura_app.model.Livro;
import br.com.alura.challenge.luiz.literalura_app.repository.AutorRepository;
import br.com.alura.challenge.luiz.literalura_app.repository.LivroRepository;
import br.com.alura.challenge.luiz.literalura_app.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

    @Autowired
    private GutendexService gutendexService;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n===== Menu LiterAlura =====");
            System.out.println("Escolha o número de sua opção:");
            System.out.println("1 - Buscar livro por título");
            System.out.println("2 - Listar livros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos em determinado ano");
            System.out.println("5 - Listar livros em um determinado idioma");
            System.out.println("0 - Sair");
            System.out.println("------------");

            while (!scanner.hasNextInt()) {
                System.out.println("Digite um número válido:");
                scanner.next();
            }
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> buscarLivroPorTitulo(scanner);
                case 2 -> listarLivros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresPorAno(scanner);
                case 5 -> listarLivrosPorIdioma(scanner);
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private void buscarLivroPorTitulo(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Insira o nome do livro que você deseja procurar:");
        String titulo = scanner.nextLine();

        try {
            Livro livro = gutendexService.buscarELimparRegistrarLivroPorTitulo(titulo);

            if (livro != null) {
                System.out.println("\n----- LIVRO -----");
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + livro.getAutor().getNome());
                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Número de downloads: " + livro.getNumeroDownloads());
                System.out.println("-----------------");
            } else {
                System.out.println("Livro não encontrado na API externa.");
            }

        } catch (RuntimeException e) {
            // Captura erros de conexão, timeout ou retorno inválido
            System.out.println("\n⚠ Erro ao buscar o livro: " + e.getMessage());
            System.out.println("Tente novamente mais tarde.");
        }
    }

    public void listarLivros() {
        List<Livro> livros = livroRepository.findAllWithAutor();

        for (Livro livro : livros) {
            System.out.println("----- LIVRO -----");
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + livro.getAutor().getNome());
            System.out.println("Idioma: " + livro.getIdioma());
            System.out.println("Número de downloads: " + livro.getNumeroDownloads());
            System.out.println("-----------------");
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
            return;
        }
        for (Autor autor : autores) {
            System.out.println("\nAutor: " + autor.getNome());
            System.out.println("Ano de nascimento: " + (autor.getAnoNascimento() != null ? autor.getAnoNascimento() : "Desconhecido"));
            System.out.println("Ano de falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Desconhecido"));

            List<String> titulos = livroRepository.findAll().stream()
                    .filter(l -> l.getAutor().getId().equals(autor.getId()))
                    .map(Livro::getTitulo)
                    .collect(Collectors.toList());

            System.out.println("Livros: " + titulos);
            System.out.println("------------");
        }
    }

    private void listarAutoresPorAno(Scanner scanner) {
        System.out.println("Insira o ano que deseja pesquisar:");
        while (!scanner.hasNextInt()) {
            System.out.println("Digite um ano válido:");
            scanner.next();
        }
        int ano = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autores = autorRepository.findAll().stream()
                .filter(a -> a.getAnoNascimento() != null && a.getAnoNascimento() <= ano &&
                        (a.getAnoFalecimento() == null || a.getAnoFalecimento() >= ano))
                .toList();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado para o ano informado.");
            return;
        }

        for (Autor autor : autores) {
            System.out.println("\nAutor: " + autor.getNome());
            System.out.println("Ano de nascimento: " + autor.getAnoNascimento());
            System.out.println("Ano de falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Desconhecido"));

            List<String> titulos = livroRepository.findAll().stream()
                    .filter(l -> l.getAutor().getId().equals(autor.getId()))
                    .map(Livro::getTitulo)
                    .collect(Collectors.toList());

            System.out.println("Livros: " + titulos);
            System.out.println("------------");
        }
    }

    private void listarLivrosPorIdioma(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Insira o idioma para realizar a busca:");
        System.out.println("es - espanhol");
        System.out.println("en - inglês");
        System.out.println("fr - francês");
        System.out.println("pt - português");
        String idioma = scanner.nextLine();

        List<Livro> livros = livroRepository.findAll().stream()
                .filter(l -> l.getIdioma() != null && l.getIdioma().equalsIgnoreCase(idioma))
                .toList();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma informado.");
            return;
        }

        for (Livro livro : livros) {
            System.out.println("\n----- LIVRO -----");
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + livro.getAutor().getNome());
            System.out.println("Idioma: " + livro.getIdioma());
            System.out.println("Número de downloads: " + livro.getNumeroDownloads());
            System.out.println("-----------------");
        }
    }
}