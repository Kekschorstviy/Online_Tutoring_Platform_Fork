package de.thu.thutorium.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Configures the security filter chain for HTTP requests.
     * Disables CSRF, sets authorization rules for different endpoints based on user roles,
     * enforces stateless session management, and adds a JWT authentication filter.
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/student/**").hasRole("STUDENT")
//                        .requestMatchers("/tutor/**").hasRole("TUTOR")
//                        .requestMatchers("/verifier/**").hasRole("VERIFIER")
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings for the application,
     * allowing specific origins, HTTP methods, and headers to be used in cross-origin requests.
     *
     * - **Allowed Origins**: Only requests from allowed URLs are permitted.
     * TODO: Review allowed URLs
     * - **Allowed Methods**: Limits cross-origin HTTP requests to "GET", "POST", "PUT", and "DELETE".
     * - **Allowed Headers**: Restricts allowed headers to "Authorization" and "Content-Type".
     * - **Allow Credentials**: Enables sending of credentials (such as cookies or authorization headers).
     * - **Configuration Application**: Applies this configuration to all endpoints (`/**`).
     *
     * @return a CORS configuration source with specified settings
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
         configuration.setAllowedOrigins(List.of(
         "http://localhost:3000",
         "http://localhost:80",
         "http://localhost",
         "http://localhost:8080")); // Add http://localhost
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Allow specific HTTP methods
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Allow specific headers
        configuration.setAllowCredentials(true); // Allow credentials for authorization headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply the configuration to all endpoints
        return source;
    }
}
