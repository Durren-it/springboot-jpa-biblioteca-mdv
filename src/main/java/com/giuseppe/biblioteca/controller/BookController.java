package com.giuseppe.biblioteca.controller;

import com.giuseppe.biblioteca.model.BookDTO;
import com.giuseppe.biblioteca.service.IBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private IBookService bookService;

    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDTO> getAll() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookService.getBookById(id));
        } catch (NoSuchElementException nseex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookDTO book) {
        if (book.id() != null)
            return ResponseEntity.badRequest().body("Non includere campo id, ci pensa il database");

        return ResponseEntity.ok(bookService.createBook(book));
    }

    @PutMapping("{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO book) {
        try {
            return ResponseEntity.ok(bookService.updateBook(id, book));
        } catch (NoSuchElementException nseex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            return ResponseEntity.ok("Libro con id =" + id + " eliminato con successo.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
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
