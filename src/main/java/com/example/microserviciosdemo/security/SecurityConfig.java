package com.example.microserviciosdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl myUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * BCrypt para contraseñas nuevas; acepta temporalmente las antiguas en texto plano
     * y las vuelve a cifrar en el siguiente inicio de sesión.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LegacyAwarePasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                .headers().frameOptions().sameOrigin()
                .and()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Público: iniciar sesión y registrarse
                .antMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/usuarios", "/api/usuarios/").permitAll()
                // Cualquier usuario con sesión (app móvil). El dueño se valida en cada controlador.
                .antMatchers(HttpMethod.GET, "/api/categorias", "/api/categorias/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/edificios", "/api/edificios/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/coordenadas/validarCoordenadas/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/visitas", "/api/visitas/").authenticated()
                .antMatchers(HttpMethod.POST, "/api/periodos", "/api/periodos/").authenticated()
                .antMatchers(HttpMethod.POST, "/api/lugares-usuario", "/api/lugares-usuario/").authenticated()
                .antMatchers(HttpMethod.GET, "/api/usuarios/*").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/usuarios/*").authenticated()
                // Todo lo demás (listados, catálogo, reportes y modelos) es solo para administradores
                .anyRequest().hasRole("ADMIN")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
