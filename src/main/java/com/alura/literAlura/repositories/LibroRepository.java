package com.alura.literAlura.repositories;

import com.alura.literAlura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByTitle(String name); // Verificar existencia del autor por nombre

    @Query("SELECT l FROM Libro l WHERE :idioma MEMBER OF l.languages")
    List<Libro> findByIdioma(String idioma);
}

