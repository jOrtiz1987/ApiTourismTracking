package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.Usuario;
import com.example.microserviciosdemo.repository.UsuarioRepository;
import com.example.microserviciosdemo.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Solo administradores (ver SecurityConfig). La contraseña nunca se incluye en la respuesta. */
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return repository.findAll();
    }

    /** El propio usuario o un administrador. */
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable int id) {
        Usuario usuario = buscar(id);
        SecurityUtil.exigirDuenoOAdmin(usuario.getCorreo());
        return usuario;
    }

    // Se eliminó GET /validarUsuario/{correo}/{password}: exponía la contraseña en la URL y
    // permitía iniciar sesión sin JWT. El inicio de sesión es POST /api/auth/login.

    /**
     * Registro público. Siempre crea un usuario nuevo: se ignora cualquier idUsuario enviado
     * (antes permitía sobrescribir cuentas existentes) y la contraseña se guarda cifrada.
     */
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()
                || usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo y contraseña son obligatorios");
        }
        usuario.setCorreo(usuario.getCorreo().trim());
        if (repository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        usuario.setIdUsuario(0);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    /** El propio usuario o un administrador. La contraseña solo cambia si se envía una nueva. */
    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable int id, @RequestBody Usuario usuarioDetails) {
        Usuario usuario = buscar(id);
        SecurityUtil.exigirDuenoOAdmin(usuario.getCorreo());

        if (usuarioDetails.getCorreo() != null && !usuarioDetails.getCorreo().trim().isEmpty()
                && !usuarioDetails.getCorreo().trim().equalsIgnoreCase(usuario.getCorreo())) {
            String nuevoCorreo = usuarioDetails.getCorreo().trim();
            if (repository.findByCorreo(nuevoCorreo).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
            }
            usuario.setCorreo(nuevoCorreo);
        }
        if (usuarioDetails.getPassword() != null && !usuarioDetails.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDetails.getPassword()));
        }
        usuario.setCodigoPostal(usuarioDetails.getCodigoPostal());
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setFechaDeNacimiento(usuarioDetails.getFechaDeNacimiento());
        usuario.setGenero(usuarioDetails.getGenero());

        return repository.save(usuario);
    }

    /** Solo administradores (ver SecurityConfig). */
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable int id) {
        repository.deleteById(id);
    }

    private Usuario buscar(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}
