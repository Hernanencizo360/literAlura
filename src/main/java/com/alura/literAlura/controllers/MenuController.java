package com.alura.literAlura.controllers;

import com.alura.literAlura.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.InputMismatchException;
import java.util.Scanner;

@Service
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private ApplicationContext appContext;

    private Scanner leer = new Scanner(System.in).useDelimiter("\n");

    public void start() {
        boolean continuar = true;
        int opcion;

        while (continuar) {
            try {
                System.out.print("""
                        \033[32m
                        ************************||************************
                        *           BIENVENIDO A LITERALURA              *
                        *       Por favor seleccione una Opción          *
                        **************************************************
                        * 1) Buscar un Libro por Titulo en la API.       *
                        * 2) Listar Libros registrados en la BBDD.       *  
                        * 3) Listar Autores registrados en la BBDD.      *
                        * 4) Listar Autores vivos en un determinado año. *  
                        * 5) Listar Libros por idioma.                   *
                        * 0) Salir.                                      *
                        **************************************************\033[39;49m
                        """);

                opcion = leer.nextInt();

                switch (opcion) {
                    case 1:
                        System.out.println("Buscar un Libro por Titulo en la API: ");
                        menuService.buscarLibroEnLaApi();
                        break;
                    case 2:
                        System.out.println("Listando Libros registrados en la bbdd.");
                        menuService.listarLibrosRegistrados();
                        break;
                    case 3:
                        System.out.println("Listando Autores registrados en la bbdd.");
                        menuService.listarAutores();
                        break;
                    case 4:
                        System.out.println("Listar Autores vivos en un determinado año.");
                        menuService.listarAutoresVivosEnAnio();
                        break;
                    case 5:
                        System.out.println("Listar Libros por idioma.");
                        menuService.listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo...");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Debe ingresar un número entre 0 y 5.");
                }
            } catch (InputMismatchException im) {
                System.out.println("Error en el tipo de dato, se espera un entero.");
                im.getMessage();
                leer.next(); // Limpiar el buffer
            }
        }

        // Finaliza el programa de manera ordenada
        SpringApplication.exit(appContext, () -> 0);
    }
}

