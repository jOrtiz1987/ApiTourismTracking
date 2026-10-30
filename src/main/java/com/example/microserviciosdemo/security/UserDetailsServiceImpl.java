package com.example.microserviciosdemo.security;

import com.example.microserviciosdemo.entity.Usuario;
import com.example.microserviciosdemo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final Set<String> correosAdmin;

    @Autowired
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository,
                                  @Value("${app.admin.emails:}") String correosAdmin) {
        this.usuarioRepository = usuarioRepository;
        this.correosAdmin = Arrays.stream(correosAdmin.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }

    /** true si el correo está en la lista ADMIN_EMAILS. */
    public boolean esCorreoAdmin(String correo) {
        return correo != null && correosAdmin.contains(correo.toLowerCase(Locale.ROOT));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // En este caso, el username es el correo del usuario
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (esCorreoAdmin(usuario.getCorreo())) {
            roles.add(new SimpleGrantedAuthority(SecurityUtil.ROLE_ADMIN));
        }
        return new User(usuario.getCorreo(), usuario.getPassword() == null ? "" : usuario.getPassword(), roles);
    }
}
