package com.karlson.crudapi;

import com.karlson.crudapi.config.CrudAPIProperties;
import com.karlson.crudapi.config.RsaKeyProperties;
import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.model.User;
import com.karlson.crudapi.repository.BookRepository;
import com.karlson.crudapi.repository.UserRepository;
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
    CommandLineRunner commandLineRunner(BookRepository bookRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByName("usr").isEmpty()) {
                userRepository.save(new User("usr", passwordEncoder.encode("password")));
            }
            if (userRepository.findByName("admin").isEmpty()) {
                userRepository.save(new User("admin", passwordEncoder.encode("password")));
            }
            if (bookRepository.findById(1).isEmpty()) {
                bookRepository.save(new Book("Douglas Adams", "Dirk Gently's Holistic Detective Agency"));
            }
        };
    }
}
