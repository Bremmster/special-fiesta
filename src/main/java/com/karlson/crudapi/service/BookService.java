package com.karlson.crudapi.service;

import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Deprecated //
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book save(Book book) {
        return repository.save(book);
    }
}
