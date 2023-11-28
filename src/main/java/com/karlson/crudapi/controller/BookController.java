package com.karlson.crudapi.controller;

import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;


    public BookController(BookService repository) {
        this.bookService = repository;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Book book) {

        var response = bookService.save(book);

        if (response.equals(book)) {
            return ResponseEntity.accepted().body(response);
        } else return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        var book = bookService.findBook(id);
        if (book.getAuthor() != null) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Book book, @PathVariable int id) {

        if (book.getId() != null && book.getId() == id) {

            var updatedBook = bookService.updateBook(book);

            if (updatedBook != null) {
                return ResponseEntity.ok(updatedBook);
            }

        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        boolean result = bookService.deleteById(id);

        if (result) return ResponseEntity.accepted().build();

        return ResponseEntity.badRequest().body("Delete request missing id.");
    }


    @GetMapping("/")
    public List<Book> findAll() {
        return bookService.findAll();
    }
}

