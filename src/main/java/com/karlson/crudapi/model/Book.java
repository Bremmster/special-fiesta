package com.karlson.crudapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public record Book(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        Integer id,
        String author,
        String title) {
        public Book() {
            this(null, null, null);
        }
}
