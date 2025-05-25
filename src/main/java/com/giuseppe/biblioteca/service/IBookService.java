package com.giuseppe.biblioteca.service;

import com.giuseppe.biblioteca.model.BookDTO;

import java.util.List;

public interface IBookService {
    List<BookDTO> getAllBooks();
    BookDTO getBookById(Long id);
    BookDTO createBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    boolean deleteBook(Long id);
    List<BookDTO> findBooksByAuthor(String author);
    List<BookDTO> findBooksByGenre(String genre);
    List<BookDTO> searchBooksByTitle(String title);
    List<BookDTO> findBooksByAnnoLessThan(int year);
    int countBooksByAuthor(String author);
    List<BookDTO> getBooksSortedByAnnoDesc();
    List<BookDTO> findBooksByTitleOrAuthor(String title, String author);
}
