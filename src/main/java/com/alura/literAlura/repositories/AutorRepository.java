package com.alura.literAlura.repositories;

import com.alura.literAlura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByName(String name); // Método para encontrar un autor por nombre

    @Query("SELECT a FROM Autor a WHERE a.birth_year <= :anio AND (a.death_year IS NULL OR a.death_year >= :anio)")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") int anio);
}
