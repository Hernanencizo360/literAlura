package com.alura.literAlura.services;

import com.alura.literAlura.models.Autor;
import com.alura.literAlura.models.Libro;
import com.alura.literAlura.util.PresentationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

@Service
public class MenuService {

    @Autowired
    private ApiService apiService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutorService autorService;

    private final Scanner leer = new Scanner(System.in).useDelimiter("\n");

    private final String URL_BASE = "https://gutendex.com/books/?";

    //Case: 1
    public void buscarLibroEnLaApi() {
        System.out.println("Ingrese el título del libro: ");
        String titulo = leer.next();
        if (titulo.trim().isEmpty() || titulo.length() < 4) {
            System.out.println("El título no puede estar vacío o tener menos de 4 caracteres.");
            return;
        }
        titulo = titulo.replace(" ", "%20");
        String url = URL_BASE + "search=" + titulo;

        try {
            String json = apiService.buscarLibro(url);

            // Pasar el json al libroService para la deserialización y recibir la lista de libros
            List<Libro> libros = libroService.procesarJson(json);

            //Mostrar al usuarios los libros y hacer que seleccione uno para persisirtlo en la BBDD.
            mostrarLibrosYSeleccionar(libros);

        } catch (RuntimeException e) {
            System.out.println("Ha ocurrido una excepción: ");
            System.out.println(e.getMessage());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ha ocurrido una excepcion...");
            System.out.println(e.getMessage());
        }
    }

    //Case 2:
    public void listarLibrosRegistrados() {
        List<Libro> libros = libroService.obtenerTodosLosLibros();
        PresentationUtils.mostrarLibros(libros);
    }

    //Case 3:
    public void listarAutores() {
        List<Autor> autores = autorService.obtenerTodosLosAutores();
        PresentationUtils.mostrarAutores(autores);
    }

    //Case 4:
    public void listarAutoresVivosEnAnio() {
        int anio;
        boolean anioValido = false;
        while (!anioValido) {
            try {
                System.out.println("Ingrese el año para listar autores vivos: ");
                anio = leer.nextInt();
                if (anioValido(anio)) {
                    anioValido = true;
                    List<Autor> autoresVivos = autorService.obtenerAutoresVivosEnAnio(anio);
                    PresentationUtils.mostrarAutores(autoresVivos);
                } else {
                    System.out.println("Año no válido. Por favor ingrese un año positivo y no mayor al año actual.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: se espera un número entero.");
                leer.nextLine();  // Consumir la entrada inválida
            }
        }
    }

    private boolean anioValido(int anio) {
        int anioActual = LocalDate.now().getYear();
        return anio > 0 && anio <= anioActual;
    }

    private void mostrarLibrosYSeleccionar(List<Libro> libros) {
        PresentationUtils.mostrarLibros(libros);
        Libro libroSeleccionado = seleccionarLibro(libros);
        libroService.guardarLibro(libroSeleccionado);
    }

    private Libro seleccionarLibro(List<Libro> libros) {
        Libro libro = null;
        while (libro == null) {
            try {
                System.out.println("Seleccione el INDICE del libro que desea guardar:");
                int seleccion = leer.nextInt();

                if (seleccion < 1 || seleccion > libros.size()) {
                    System.out.println("Indice fuera de rango.");
                } else {
                    libro = libros.get(seleccion - 1);
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: se espera un numero entero.");
                leer.next(); // Limpiar el buffer de entrada
            }
        }
        return libro;
    }

    //Case 5:
    public void listarLibrosPorIdioma() {
        boolean inputValido = false;
        String idioma = "";

        while (!inputValido) {
            System.out.println("Ingrese el idioma (por ejemplo, 'en' para inglés, 'es' para español): ");
            idioma = leer.next();

            if (validarCodigoIdioma(idioma)) {
                inputValido = true;
                idioma = idioma.toLowerCase();
            } else {
                System.out.println("Código de idioma no válido. Asegúrese de ingresar un código de dos letras.");
            }
        }

        List<Libro> librosPorIdioma = libroService.obtenerLibrosPorIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros para el idioma especificado.");
        } else {
            PresentationUtils.mostrarLibros(librosPorIdioma);
        }
    }

    private boolean validarCodigoIdioma(String codigo) {
        return codigo != null && codigo.matches("[a-zA-Z]{2}");
    }

    //Case 6:
    public void buscarTop10Descargas() {
        try {
            String json = apiService.buscarLibro(URL_BASE);

            // Pasar el json al libroService para la deserialización y limitar al top libros
            List<Libro> libros = libroService.procesarJson(json).stream().limit(10).toList();

            IntStream.range(0, libros.size())
                    .forEach(i -> {
                        System.out.println("Puesto: " + (i + 1));
                        Libro libro = libros.get(i);
                        System.out.println("Título: " + libro.getTitle());

                        String autor = libro.getAuthors().stream()
                                .findFirst()
                                .map(Autor::getName)
                                .orElse("No disponible");
                        System.out.println("Autor: " + autor);

                        System.out.println("Descargas: " + libro.getDownload_count());
                        System.out.println();
                    });
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    //Case 7:
    public void buscarAutorPorNombre() {
        String nombre = null;
        boolean nombreValido = false;

        while (!nombreValido) {
            System.out.println("Ingrese el nombre del autor que desea buscar (mínimo 3 letras, solo letras): ");
            nombre = leer.next();

            if (nombre == null || nombre.trim().isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
            }  else if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                System.out.println("El nombre solo puede contener letras, NO números.");
            } else if (nombre.length() < 3) {
                System.out.println("El nombre debe contener al menos 3 letras.");
            } else {
                nombreValido = true;
            }
        }

        // Realizar búsqueda en el servicio de autor
        List<Autor> autores = autorService.buscarAutorPorNombre(nombre);

        // Mostrar resultados
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores con el nombre proporcionado.");
        } else {
            PresentationUtils.mostrarAutores(autores);
        }
    }
}



