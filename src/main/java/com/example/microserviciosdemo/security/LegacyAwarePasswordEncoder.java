package com.example.microserviciosdemo.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

/**
 * Cifra con BCrypt, pero acepta las contraseñas antiguas guardadas en texto plano
 * para no bloquear a los usuarios existentes. Al iniciar sesión con una contraseña
 * antigua, AuthController la vuelve a guardar cifrada (ver {@link #esLegada(String)}).
 */
public class LegacyAwarePasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return bcrypt.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        if (!esLegada(encodedPassword)) {
            return bcrypt.matches(rawPassword, encodedPassword);
        }
        // Comparación en tiempo constante para las contraseñas antiguas en texto plano
        return MessageDigest.isEqual(
                rawPassword.toString().getBytes(StandardCharsets.UTF_8),
                encodedPassword.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return esLegada(encodedPassword);
    }

    /** true si la contraseña guardada no es un hash BCrypt (texto plano heredado). */
    public static boolean esLegada(String encodedPassword) {
        return encodedPassword == null
                || !(encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$") || encodedPassword.startsWith("$2y$"));
    }
}
