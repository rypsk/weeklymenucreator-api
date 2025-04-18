package com.rypsk.weeklymenucreator.api.security;

import com.rypsk.weeklymenucreator.api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    @Profile("dev")
    public SecurityFilterChain configureDev(HttpSecurity httpSecurity) throws Exception {
        System.out.println("configure DEV");
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/h2-console/**").permitAll();
                    request.requestMatchers(HttpMethod.POST, "/api/v1/auth/sign-up").permitAll();
                    request.requestMatchers(HttpMethod.POST, "/api/v1/auth/sign-in").permitAll();
                    request.requestMatchers(HttpMethod.GET, "/api/v1/auth/verify").permitAll();
                    request.requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html").permitAll();
                    request.anyRequest().authenticated();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtil), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        System.out.println("configure PROD");
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    request.requestMatchers(HttpMethod.GET, "/auth/hello").permitAll();
                    request.requestMatchers(HttpMethod.GET, "/auth/secured").hasRole("ADMIN");
                    request.anyRequest().authenticated();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtil), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
