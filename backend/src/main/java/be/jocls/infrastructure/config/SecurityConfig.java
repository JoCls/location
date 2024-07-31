package be.jocls.infrastructure.config;

import be.jocls.application.service.CustomUserDetailsService;
import be.jocls.domain.model.User;
import be.jocls.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                    .loginProcessingUrl("/api/auth/login")
                    .successHandler(authenticationSuccessHandler())  // Custom success handler
                    .failureHandler(authenticationFailureHandler())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            // Return a JSON response indicating success
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String userRole = user.getUserRole().name();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"userRole\": \""+userRole+"\"}");
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) -> {
            // Return a JSON response indicating failure
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"" + exception.getMessage() + "\"}");
        };
    }

     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow React dev server
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}


