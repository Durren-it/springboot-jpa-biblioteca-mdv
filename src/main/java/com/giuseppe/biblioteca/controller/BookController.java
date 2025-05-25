package com.giuseppe.biblioteca.controller;

import com.giuseppe.biblioteca.model.BookDTO;
import com.giuseppe.biblioteca.service.IBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller per la gestione dei libri in biblioteca.
 * Espone endpoint per operazioni CRUD, ricerche e ordinamenti.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private IBookService bookService;

    /**
     * Inietta il servizio per la gestione dei libri.
     *
     * @param bookService il servizio da utilizzare
     */
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Recupera tutti i libri presenti.
     *
     * @return una lista di BookDTO contenenti tutti i libri
     */
    @GetMapping
    public List<BookDTO> getAll() {
        return bookService.getAllBooks();
    }

    /**
     * Recupera un libro dato il suo ID.
     *
     * @param id l'ID del libro
     * @return il libro richiesto oppure un messaggio di errore se non trovato
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookService.getBookById(id));
        } catch (NoSuchElementException nseex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuovo libro.
     *
     * @param book il BookDTO da creare; il campo id deve essere null
     * @return il libro creato oppure un messaggio di errore in caso di input non valido
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookDTO book) {
        if (book.id() != null)
            return ResponseEntity.badRequest().body("Non includere campo id, ci pensa il database");

        return ResponseEntity.ok(bookService.createBook(book));
    }

    /**
     * Aggiorna un libro esistente.
     *
     * @param id   l'ID del libro da aggiornare
     * @param book il BookDTO con i nuovi dati
     * @return il libro aggiornato oppure un messaggio di errore se il libro non esiste
     */
    @PutMapping("{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO book) {
        try {
            return ResponseEntity.ok(bookService.updateBook(id, book));
        } catch (NoSuchElementException nseex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un libro dato il suo ID.
     *
     * @param id l'ID del libro da eliminare
     * @return un messaggio che conferma l'eliminazione o un errore se il libro non esiste
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            return ResponseEntity.ok("Libro con id =" + id + " eliminato con successo.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recupera i libri in base all'autore.
     * Verifica che il parametro non sia composto solo da numeri.
     *
     * @param author il nome dell'autore da cercare
     * @return una lista di libri oppure un messaggio di errore se non trovati o input non valido
     */
    @GetMapping("/by-author/{author}")
    public ResponseEntity<?> getBooksByAuthor(@PathVariable String author) {
        if (author.matches("\\d+")) {
            return ResponseEntity.badRequest()
                    .body("Il parametro per autore non può essere composto solo da numeri.");
        }
        try {
            List<BookDTO> books = bookService.findBooksByAuthor(author);
            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nessun libro trovato per l'autore: " + author);
            }
            return ResponseEntity.ok(books);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore inaspettato nella gestione della richiesta, riprovare più tardi.");
        }
    }

    /**
     * Recupera i libri in base al genere.
     * Verifica che il parametro non sia composto solo da numeri.
     *
     * @param genre il genere da cercare
     * @return una lista di libri oppure un messaggio di errore se non trovati o input non valido
     */
    @GetMapping("/by-genre/{genre}")
    public ResponseEntity<?> getBooksByGenre(@PathVariable String genre) {
        if (genre.matches("\\d+")) {
            return ResponseEntity.badRequest()
                    .body("Il parametro per genere non può essere composto solo da numeri.");
        }
        try {
            List<BookDTO> books = bookService.findBooksByGenre(genre);
            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Non sono presenti libri del genere: " + genre);
            }
            return ResponseEntity.ok(books);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore inaspettato nella gestione della richiesta, riprovare più tardi.");
        }
    }

    /**
     * Cerca libri che contengono una determinata stringa nel titolo.
     *
     * @param title la stringa da ricercare nel titolo
     * @return una lista di libri oppure un messaggio di errore se nessun libro è trovato
     */
    @GetMapping("/search/title")
    public ResponseEntity<?> searchBooksByTitle(@RequestParam String title) {
        try {
            List<BookDTO> books = bookService.searchBooksByTitle(title);
            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Non sono presenti libri con il titolo: " + title);
            }
            return ResponseEntity.ok(books);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore inaspettato nella gestione della richiesta, riprovare più tardi.");
        }
    }

    /**
     * Recupera i libri pubblicati prima di un anno specifico.
     * Verifica che il parametro anno sia numerico.
     *
     * @param year l'anno limite (come stringa)
     * @return una lista di libri oppure un messaggio di errore se nessun libro è trovato o input non valido
     */
    @GetMapping("/before/{year}")
    public ResponseEntity<?> getBooksBeforeYear(@PathVariable String year) {
        if (!year.matches("\\d+")) {
            return ResponseEntity.badRequest().body("L'anno deve essere un numero valido.");
        }
        try {
            int yearInt = Integer.parseInt(year);
            List<BookDTO> books = bookService.findBooksByAnnoLessThan(yearInt);
            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Non sono presenti libri pubblicati prima dell'anno: " + year);
            }
            return ResponseEntity.ok(books);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore inaspettato nella gestione della richiesta, riprovare più tardi.");
        }
    }

    /**
     * Conta i libri scritti da un determinato autore.
     * Verifica che il parametro autore non sia composto solo da numeri.
     *
     * @param author l'autore di cui contare i libri
     * @return il numero dei libri oppure un messaggio di errore se non trovati o input non valido
     */
    @GetMapping("/count/author/{author}")
    public ResponseEntity<?> countBooksByAuthor(@PathVariable String author) {
        if (author.matches("\\d+")) {
            return ResponseEntity.badRequest()
                    .body("Il parametro per autore non può essere composto solo da numeri.");
        }
        try {
            int count = bookService.countBooksByAuthor(author);
            if (count == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nessun libro trovato per l'autore: " + author);
            }
            return ResponseEntity.ok(count);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore inaspettato nella gestione della richiesta, riprovare più tardi.");
        }
    }

    /**
     * Recupera i libri ordinati per anno in ordine discendente.
     *
     * @return ResponseEntity con la lista dei BookDTO o un messaggio d'errore.
     */
    @GetMapping("/sorted")
    public ResponseEntity<?> getBooksSortedByAnnoDesc() {
        try {
            List<BookDTO> books = bookService.getBooksSortedByAnnoDesc();
            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Non sono presenti libri ordinabili.");
            }
            return ResponseEntity.ok(books);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore inaspettato nella gestione della richiesta, riprovare più tardi.");
        }
    }

    /**
     * Cerca libri basandosi sul titolo e/o autore.
     * Verifica che entrambi i parametri non siano composti solo da numeri.
     *
     * @param title  la stringa da cercare nel titolo
     * @param author la stringa da cercare nell'autore
     * @return una lista di libri oppure un messaggio di errore se nessun libro è trovato o input non valido
     */
    @GetMapping("/search/title-or-author")
    public ResponseEntity<?> searchBooksByTitleOrAuthor(@RequestParam String title,
                                                        @RequestParam String author) {
        if (author.matches("\\d+")) {
            return ResponseEntity.badRequest()
                    .body("Il parametro per autore non può essere composto solo da numeri.");
        }
        try {
            List<BookDTO> books = bookService.findBooksByTitleOrAuthor(title, author);
            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Non sono presenti libri con il titolo '" + title +
                                "' o l'autore '" + author + "'.");
            }
            return ResponseEntity.ok(books);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore inaspettato nella gestione della richiesta, riprovare più tardi.");
        }
    }
}
