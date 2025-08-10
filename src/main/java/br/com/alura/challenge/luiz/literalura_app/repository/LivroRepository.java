package br.com.alura.challenge.luiz.literalura_app.repository;

import br.com.alura.challenge.luiz.literalura_app.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor WHERE l.titulo = :titulo")
    Livro findByTituloWithAutor(@Param("titulo") String titulo);

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor")
    List<Livro> findAllWithAutor();
}