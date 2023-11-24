package com.karlson.crudapi.controller;

import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

//    private final BookRepository bookService;

    private final BookService bookService;


    public BookController(BookService repository) {
        this.bookService = repository;
    }

    //    C - Insert
    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Book book) {
        if (bookService.save(book).equals(book)) {
            return ResponseEntity.accepted().body("Book saved");
        } else return ResponseEntity.badRequest().build();
    }

    //    R - SelectOne
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        var book = bookService.findBook(id);
        if (book.getAuthor() != null) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }

    //    U - Update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Book book, @PathVariable int id) {

        if (book.getAuthor() != null && book.getId() == id) {
//
                var updatedBook = bookService.updateBook(book);

                if (updatedBook != null) {
                    return ResponseEntity.ok(updatedBook);
                }
//            }
        }
        return ResponseEntity.badRequest().build();
    }


    //    D - Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        boolean result = bookService.deleteById(id);

        if (result) return ResponseEntity.accepted().build();

        return ResponseEntity.badRequest().body("Delete request missing id.");
    }


    //    L - SelectAll
    @GetMapping("/")
    public List<Book> findAll() {
        return bookService.findAll();
    }
}

