package com.karlson.crudapi.controller;

import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    //    C - Insert
    @PostMapping("/") // todo ad validation
    public void create(@RequestBody Book book) {
        repository.save(book);
    }

    //    R - SelectOne
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @GetMapping("/{id}") // todo return a response body?
    public Optional<Book> findById(@PathVariable Integer id) {
        return repository.findById(id);
    }


    //    U - Update
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/") // todo figure out how this works. ATM @PathVariable is irrelevant to the updated book.
    public void update(@RequestBody Book book) {
        if (!repository.existsById(book.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found!");
        }
        repository.save(book);
    }


    //    D - Delete
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }


    //    L - SelectAll
    @GetMapping("/")
    public List<Book> findAll() {
        return repository.findAll();
    }

}
