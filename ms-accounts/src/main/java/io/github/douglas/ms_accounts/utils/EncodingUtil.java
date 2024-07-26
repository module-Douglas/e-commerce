package io.github.douglas.ms_accounts.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncodingUtil {

    private static final Integer SALT_LENGTH = 16;
    private static final Integer HASH_LENGTH = 32;
    private static final Integer PARALLELISM = 1;
    private static final Integer MEMORY = 4096;
    private static final Integer ITERATIONS = 3;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(
                SALT_LENGTH,
                HASH_LENGTH,
                PARALLELISM,
                MEMORY,
                ITERATIONS
        );
    }

}
