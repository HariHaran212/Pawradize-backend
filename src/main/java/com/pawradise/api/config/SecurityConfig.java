package com.pawradise.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Assuming you have a custom success handler, which we'll create next
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;

    public SecurityConfig(@Autowired OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler, @Autowired JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, @Autowired HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.cookieAuthorizationRequestRepository = cookieAuthorizationRequestRepository;
    }

    // Define which endpoints are public
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/**",      // For login/register
                                "/api/public/**",     // For public info like contact details
                                "/api/pets/**",
                                "/api/products/**",
                                "/api/content/**"
                        ).permitAll()

                        .requestMatchers("/oauth2/**", "/login/oauth2/code/**").permitAll()

                        .requestMatchers("/api/orders").hasRole("USER")
                        .requestMatchers("/api/admin/users/**", "/api/admin/settings/**", "/api/admin/content/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/api/admin/pets/**").hasAnyRole("SUPER_ADMIN", "ADOPTION_COORDINATOR")
                        .requestMatchers("/api/admin/products/**", "/api/admin/orders/**").hasAnyRole("SUPER_ADMIN", "STORE_MANAGER")

                        // Secure all other API requests (must be logged in)
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                // ADD THIS PART TO ENABLE GOOGLE LOGIN
                .oauth2Login(oauth2 -> {
                    oauth2.authorizationEndpoint(endpoint ->
                            endpoint.authorizationRequestRepository(cookieAuthorizationRequestRepository)
                    );
                    oauth2.successHandler(oAuth2LoginSuccessHandler);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}