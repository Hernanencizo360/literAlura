package com.alura.literAlura.services;

import com.alura.literAlura.models.Autor;
import com.alura.literAlura.repositories.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public Autor guardarAutor(Autor autor) {
        // Verificar si el autor ya está en la base de datos por nombre
        Autor autorExistente = autorRepository.findByName(autor.getName());
        if (autorExistente != null) {
            System.out.println("El autor " + autor.getName() + " ya está en la base de datos.");
            return autorExistente;
        }

        // Guardar autor
        autorRepository.save(autor);
        System.out.println("Autor guardado exitosamente.");
        return autor;
    }

    public Autor buscarPorNombre(String nombre) {
        return autorRepository.findByName(nombre);
    }

    public List<Autor> obtenerTodosLosAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> obtenerAutoresVivosEnAnio(int anio) {
        return autorRepository.findAutoresVivosEnAnio(anio);
    }
}


