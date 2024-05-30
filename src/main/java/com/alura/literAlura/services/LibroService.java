package com.alura.literAlura.services;

import com.alura.literAlura.models.Autor;
import com.alura.literAlura.models.Libro;
import com.alura.literAlura.repositories.LibroRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibroService {

    private final ObjectMapper objectMapper;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorService autorService;

    public LibroService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Libro> procesarJson(String json) {
        try {
            // Convertir el String JSON a un JSONObject
            JSONObject jsonObj = new JSONObject(json);
            // Obtener el JSONArray "results" que contiene los libros
            JSONArray results = jsonObj.getJSONArray("results");
            // Convertir JSONArray a List<Libro>
            return objectMapper.readValue(results.toString(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el JSON", e);
        }
    }

    public void guardarLibro(Libro libro) {
        // Verificar si el libro ya está en la base de datos
        if (libroRepository.existsByTitle(libro.getTitle())) {
            System.out.println("El libro ya está en la base de datos.");
            return;
        }

        // Si el libro tiene autores, procesarlos
        if (libro.getAuthors() != null && !libro.getAuthors().isEmpty()) {
            List<Autor> autoresPersistidos = new ArrayList<>();
            for (Autor autor : libro.getAuthors()) {
                Autor autorExistente = autorService.buscarPorNombre(autor.getName());
                if (autorExistente != null) {
                    autoresPersistidos.add(autorExistente);
                } else {
                    autorService.guardarAutor(autor);
                    autoresPersistidos.add(autor);
                }
            }
            libro.setAuthors(autoresPersistidos);
        } else {
            libro.setAuthors(new ArrayList<>()); // Asegurar que la lista de autores no sea nula
        }

        // Guardar el libro con los autores asociados (si los hay)
        libroRepository.save(libro);
        System.out.println("Libro guardado exitosamente.");
    }

    public List<Libro> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    //Case 5:
    public List<Libro> obtenerLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma);
    }
}
