package com.karlson.crudapi.controller;

import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;


    public BookController(BookService repository) {
        this.bookService = repository;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Book book) {
        try {
            var response = bookService.save(book);
            return ResponseEntity.accepted().body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            var book = bookService.findBook(id);
            return ResponseEntity.ok(book);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Book book, @PathVariable int id) {

        if (book.getId() != null && book.getId() == id) {
            try {
                var updatedBook = bookService.updateBook(book);
                return ResponseEntity.ok(updatedBook);
            } catch (Exception e) {
                // do some logging here
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


    @GetMapping("/") // todod List<Book>
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(bookService.findAll());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

