package com.karlson.crudapi;

import com.karlson.crudapi.config.CrudAPIProperties;
import com.karlson.crudapi.config.RsaKeyProperties;
import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.model.User;
import com.karlson.crudapi.repository.BookRepository;
import com.karlson.crudapi.repository.UserRepository;
import com.karlson.crudapi.service.JpaUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableConfigurationProperties({CrudAPIProperties.class, RsaKeyProperties.class})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BookRepository bookRepository, JpaUserDetailsService userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByName("usr").isEmpty()) {
                userRepository.save(new User("usr", "password"));
            }

            if (bookRepository.findAll().isEmpty()) {
                bookRepository.save(new Book("Douglas Adams", "Dirk Gently's Holistic Detective Agency"));
                bookRepository.save(new Book("Randall Munroe", "How To"));
                bookRepository.save(new Book("Ulf Ellervik", "Ond Kemi"));
            }
        };
    }
}
