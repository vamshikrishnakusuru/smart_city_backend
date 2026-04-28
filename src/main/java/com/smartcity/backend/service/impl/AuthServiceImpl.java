package com.smartcity.backend.service.impl;

import com.smartcity.backend.dto.auth.AuthResponse;
import com.smartcity.backend.dto.auth.LoginRequest;
import com.smartcity.backend.dto.auth.RegisterRequest;
import com.smartcity.backend.dto.auth.UserSummaryResponse;
import com.smartcity.backend.entity.User;
import com.smartcity.backend.exception.BadRequestException;
import com.smartcity.backend.repository.UserRepository;
import com.smartcity.backend.security.JwtService;
import com.smartcity.backend.service.AuthService;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            ModelMapper modelMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Email is already registered");
        }

        User user = modelMapper.map(request, User.class);
        user.setEmail(normalizedEmail);
        user.setCity(request.getCity().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        return buildAuthResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword())
            );
        } catch (org.springframework.security.core.AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .authorities("ROLE_" + user.getRole().name())
                        .build(),
                Map.of(
                        "role", user.getRole().name(),
                        "city", user.getCity(),
                        "name", user.getName(),
                        "userId", user.getId()
                )
        );

        UserSummaryResponse userResponse = modelMapper.map(user, UserSummaryResponse.class);
        return new AuthResponse(token, "Bearer", userResponse);
    }
}
