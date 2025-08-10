package br.com.alura.challenge.luiz.literalura_app.repository;

import br.com.alura.challenge.luiz.literalura_app.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNome(String nome);
}