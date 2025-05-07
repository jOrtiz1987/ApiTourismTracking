package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.Usuario;
import com.example.microserviciosdemo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario not found"));
    }


    @GetMapping("/validarUsuario/{correo}/{password}")
    public ResponseEntity<Usuario> validarUsuario(@PathVariable("correo") String correo, @PathVariable("password") String password) {
        System.out.println("Entre a validar usuario");
        Optional<Usuario> usuarioOptional = repository.findByCorreoAndPassword(correo, password);
        if (usuarioOptional.isPresent()) {
            System.out.println("Si esta");
            return ResponseEntity.ok(usuarioOptional.get());
        } else {
            System.out.println("No esta");
            return ResponseEntity.noContent().build();
            //return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        System.out.println("Voy a crear el usuario");
        return repository.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable int id, @RequestBody Usuario usuarioDetails) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario not found"));

        usuario.setCorreo(usuarioDetails.getCorreo());
        usuario.setPassword(usuarioDetails.getPassword());
        usuario.setCodigoPostal(usuarioDetails.getCodigoPostal());
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setFechaDeNacimiento(usuarioDetails.getFechaDeNacimiento());
        usuario.setGenero(usuarioDetails.getGenero());

        return repository.save(usuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable int id) {
        repository.deleteById(id);
    }
}
