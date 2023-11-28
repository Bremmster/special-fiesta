package com.karlson.crudapi.service;

import com.karlson.crudapi.model.SecurityUser;
import com.karlson.crudapi.model.User;
import com.karlson.crudapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.karlson.crudapi.service.EncodePasswords.passwordEncoder;


@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Autowired
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByName(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found " + username));
    }

    public User save(User user) {
        String pw = user.getPassword();
        user.setPassword(passwordEncoder.encode(pw));
        return userRepository.save(user);
    }

    public Optional<User> findByName(String usr) {
        return userRepository.findByName(usr);

    }
}
