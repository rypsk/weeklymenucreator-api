package com.rypsk.weeklymenucreator.api.security;

import com.rypsk.weeklymenucreator.api.model.dto.AuthResponse;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignInRequest;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignUpRequest;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.enumeration.Role;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.EmailService;
import com.rypsk.weeklymenucreator.api.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public UserDetailServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                grantedAuthorities);
    }

    public Authentication getAuthentication(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password.");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public AuthResponse signIn(AuthSignInRequest authSignInRequest) {
        String username = authSignInRequest.username();
        String password = authSignInRequest.password();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        if (!user.isEnabled()) {
            throw new IllegalStateException("Account is not verified. Please check your email.");
        }

        Authentication authentication = getAuthentication(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication);

        return new AuthResponse(username, "User signIn successfully", token, true);
    }

    public AuthResponse signUp(@Valid AuthSignUpRequest authSignUpRequest) {
        String username = authSignUpRequest.username();
        String password = authSignUpRequest.password();
        String email = authSignUpRequest.email();
        String verificationCode = UUID.randomUUID().toString();
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(Role.ROLE_USER)
                .isEnabled(false)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .verificationCode(verificationCode)
                .build();
        user = userRepository.save(user);

//        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
//        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().name()));
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), grantedAuthorities);
//        String token = jwtUtil.generateToken(authentication);
//        return new AuthResponse(user.getUsername(), "User signUp successfully", token, true);

        String subject = "Activate your account";
        String confirmationUrl = "http://localhost:8080/api/v1/auth/verify?code=" + verificationCode;
        String content = "Hi " + username + ",\n\nPlease click the link below to activate your account:\n" + confirmationUrl;

        try {
            emailService.sendEmail(email, subject, content, null);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return new AuthResponse(username, "User registered successfully. Please check your email to activate your account.", null, false);
    }
}
