package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.security.JwtUtil;
import com.example.microserviciosdemo.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.microserviciosdemo.entity.Usuario;
import com.example.microserviciosdemo.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getCorreo(),
                            authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getCorreo());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        Usuario usuario = usuarioRepository.findByCorreo(authenticationRequest.getCorreo())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        return ResponseEntity.ok(new AuthenticationResponse(jwt, usuario.getIdUsuario(), usuario.getNombre()));
    }
}

class AuthenticationRequest {
    private String correo;
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String correo, String password) {
        this.correo = correo;
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class AuthenticationResponse {
    private final String jwt;
    private final Integer idUsuario;
    private final String nombre;

    public AuthenticationResponse(String jwt, Integer idUsuario, String nombre) {
        this.jwt = jwt;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
    }

    public String getJwt() {
        return jwt;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }
}
