package com.example.microserviciosdemo.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

/** Utilidades para conocer al usuario autenticado de la petición actual. */
public final class SecurityUtil {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private SecurityUtil() {
    }

    /** Correo del usuario autenticado, o null si no hay sesión. */
    public static String correoActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return auth.getName();
    }

    public static boolean esAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> ROLE_ADMIN.equals(a.getAuthority()));
    }

    /** Lanza 403 si el usuario actual no es el dueño del recurso ni administrador. */
    public static void exigirDuenoOAdmin(String correoDueno) {
        if (esAdmin()) {
            return;
        }
        String actual = correoActual();
        if (actual == null || correoDueno == null || !actual.equalsIgnoreCase(correoDueno)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
    }
}
