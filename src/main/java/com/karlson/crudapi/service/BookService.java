package com.karlson.crudapi.service;

import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public Book findBook(int id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Book updateBook(Book book) {

        if (repository.existsById(book.getId())) {
            return repository.save(book);
        } else {
            return null;
        }
    }

    public boolean deleteById(int id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (IllegalArgumentException il) {
            LOG.info(il.getMessage());
            return false;
        }
    }

}

