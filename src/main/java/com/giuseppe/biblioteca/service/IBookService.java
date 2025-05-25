package com.giuseppe.biblioteca.service;

import com.giuseppe.biblioteca.model.BookDTO;

import java.util.List;

/**
 * Definisce il contratto per la gestione dei libri.
 * Espone metodi per operazioni CRUD, ricerche e ordinamenti.
 */
public interface IBookService {

    /**
     * Recupera tutti i libri presenti.
     *
     * @return una lista di BookDTO contenenti tutti i libri
     */
    List<BookDTO> getAllBooks();

    /**
     * Recupera un libro dato il suo ID.
     *
     * @param id l'ID del libro
     * @return il BookDTO corrispondente
     */
    BookDTO getBookById(Long id);

    /**
     * Crea un nuovo libro.
     *
     * @param bookDTO il BookDTO da creare
     * @return il BookDTO creato
     */
    BookDTO createBook(BookDTO bookDTO);

    /**
     * Aggiorna i dati di un libro esistente.
     *
     * @param id l'ID del libro da aggiornare
     * @param bookDTO il BookDTO con i nuovi dati
     * @return il BookDTO aggiornato
     */
    BookDTO updateBook(Long id, BookDTO bookDTO);

    /**
     * Elimina un libro dato il suo ID.
     *
     * @param id l'ID del libro da eliminare
     * @return true se il libro è stato eliminato, false altrimenti
     */
    boolean deleteBook(Long id);

    /**
     * Cerca i libri in base all'autore.
     *
     * @param author il nome dell'autore
     * @return una lista di BookDTO corrispondenti
     */
    List<BookDTO> findBooksByAuthor(String author);

    /**
     * Cerca i libri in base al genere.
     *
     * @param genre il genere da cercare
     * @return una lista di BookDTO corrispondenti
     */
    List<BookDTO> findBooksByGenre(String genre);

    /**
     * Cerca i libri per titolo utilizzando una ricerca parziale (ignorando il case).
     *
     * @param title la stringa da cercare nel titolo
     * @return una lista di BookDTO corrispondenti
     */
    List<BookDTO> searchBooksByTitle(String title);

    /**
     * Cerca i libri pubblicati prima di un certo anno.
     *
     * @param year l'anno limite (il metodo restituisce libri con anno minore)
     * @return una lista di BookDTO corrispondenti
     */
    List<BookDTO> findBooksByAnnoLessThan(int year);

    /**
     * Conta il numero di libri scritti dall'autore specificato.
     *
     * @param author l'autore da cercare
     * @return il numero di libri trovati
     */
    int countBooksByAuthor(String author);

    /**
     * Recupera i libri ordinati per anno in ordine discendente.
     *
     * @return una lista di BookDTO ordinata per anno discendente
     */
    List<BookDTO> getBooksSortedByAnnoDesc();

    /**
     * Cerca i libri che corrispondono al titolo e/o autore specificato.
     *
     * @param title la stringa da cercare nel titolo
     * @param author la stringa da cercare nell'autore
     * @return una lista di BookDTO corrispondenti
     */
    List<BookDTO> findBooksByTitleOrAuthor(String title, String author);
}
