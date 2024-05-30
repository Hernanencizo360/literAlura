package com.alura.literAlura.util;

import com.alura.literAlura.models.Autor;
import com.alura.literAlura.models.Libro;
import java.util.List;
import java.util.stream.IntStream;

public class PresentationUtils {
    public static void mostrarAutores(List<Autor> autores) {
        System.out.println("Resultados de la busqueda: ");
        System.out.println("\nAutores " + autores.size() + ": ");
        autores.forEach(autor -> {
            System.out.println("Nombre: " + autor.getName());
            if (autor.getBirth_year() != null) {
                System.out.println("Año de nacimiento: " + autor.getBirth_year());
            }
            if (autor.getDeath_year() != null) {
                System.out.println("Año de fallecimiento: " + autor.getDeath_year());
            }
            System.out.println();
        });
    }

    public static void mostrarLibros(List<Libro> libros) {
        System.out.println("Resultados de la busqueda: ");
        System.out.println("\nLibros a mostrar " + libros.size() + ": ");
        IntStream.range(0, libros.size())
                .forEach(i -> {
                    Libro libro = libros.get(i);
                    System.out.println("Índice: " + (i + 1));
                    System.out.println("ID: " + libro.getId());
                    System.out.println("Título: " + libro.getTitle());

                    String autor = libro.getAuthors().stream()
                            .findFirst()
                            .map(Autor::getName)
                            .orElse("No disponible");
                    System.out.println("Autor: " + autor);

                    String idioma = libro.getLanguages().stream()
                            .findFirst()
                            .orElse("No disponible");
                    System.out.println("Idioma: " + idioma);

                    System.out.println();
                });
    }
}
