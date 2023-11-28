package com.karlson.crudapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncodePasswords {
    static PasswordEncoder passwordEncoder;

    public EncodePasswords(PasswordEncoder passwordEncoder) {
        EncodePasswords.passwordEncoder = passwordEncoder;
    }
}
