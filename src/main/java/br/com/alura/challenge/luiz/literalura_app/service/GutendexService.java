package br.com.alura.challenge.luiz.literalura_app.service;

import br.com.alura.challenge.luiz.literalura_app.model.Autor;
import br.com.alura.challenge.luiz.literalura_app.model.Livro;
import br.com.alura.challenge.luiz.literalura_app.repository.AutorRepository;
import br.com.alura.challenge.luiz.literalura_app.repository.LivroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class GutendexService {

    @Autowired
    private ConsumoApi consumoApi;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL_BASE = "https://gutendex.com/books/?search=";

    @Transactional
    public Livro buscarELimparRegistrarLivroPorTitulo(String titulo) {
        String url = URL_BASE + titulo.replace(" ", "+");

        String json = consumoApi.obterDados(url);

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode results = root.get("results");

            if (results != null && results.isArray() && results.size() > 0) {
                JsonNode livroJson = results.get(0);

                String tituloLivro = livroJson.get("title").asText();

                // Autor (pegar primeiro)
                JsonNode authorsArray = livroJson.get("authors");
                String nomeAutor;
                Integer anoNascimento;
                Integer anoFalecimento;

                if (authorsArray != null && authorsArray.isArray() && authorsArray.size() > 0) {
                    JsonNode autorJson = authorsArray.get(0);
                    nomeAutor = autorJson.path("name").asText("Autor desconhecido");
                    if (autorJson.hasNonNull("birth_year")) {
                        anoNascimento = autorJson.get("birth_year").asInt();
                    } else {
                        anoNascimento = null;
                    }
                    if (autorJson.hasNonNull("death_year")) {
                        anoFalecimento = autorJson.get("death_year").asInt();
                    } else {
                        anoFalecimento = null;
                    }
                } else {
                    anoNascimento = null;
                    anoFalecimento = null;
                    nomeAutor = "Autor desconhecido";
                }

                // Idioma (pegar primeiro)
                JsonNode languagesArray = livroJson.get("languages");
                String idioma = languagesArray != null && languagesArray.isArray() && languagesArray.size() > 0
                        ? languagesArray.get(0).asText()
                        : "Desconhecido";

                int downloads = livroJson.get("download_count").asInt();

                // Verificar se autor existe, se não, cria
                Optional<Autor> autorOpt = autorRepository.findByNome(nomeAutor);
                Autor autor = autorOpt.orElseGet(() -> {
                    Autor novoAutor = new Autor();
                    novoAutor.setNome(nomeAutor);
                    novoAutor.setAnoNascimento(anoNascimento);
                    novoAutor.setAnoFalecimento(anoFalecimento);
                    return autorRepository.save(novoAutor);
                });

                // Verificar se o livro já existe para este autor
                boolean livroExiste = livroRepository.findAll().stream()
                        .anyMatch(l -> l.getTitulo().equalsIgnoreCase(tituloLivro) &&
                                l.getAutor().getNome().equalsIgnoreCase(nomeAutor));

                if (!livroExiste) {
                    Livro livro = new Livro();
                    livro.setTitulo(tituloLivro);
                    livro.setIdioma(idioma);
                    livro.setNumeroDownloads(downloads);
                    livro.setAutor(autor);

                    livroRepository.save(livro);
                    return livro;
                } else {
                    // Retorna o livro existente
                    return livroRepository.findAll().stream()
                            .filter(l -> l.getTitulo().equalsIgnoreCase(tituloLivro) &&
                                    l.getAutor().getNome().equalsIgnoreCase(nomeAutor))
                            .findFirst()
                            .orElse(null);
                }

            } else {
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta da API Gutendex: " + e.getMessage(), e);
        }
    }
}