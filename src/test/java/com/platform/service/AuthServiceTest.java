package com.platform.service;

import com.platform.dto.auth.LoginRequest;
import com.platform.dto.auth.LoginResponse;
import com.platform.dto.auth.RegisterRequest;
import com.platform.entity.User;
import com.platform.exception.BusinessException;
import com.platform.repository.UserRepository;
import com.platform.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    void register_success() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .password("encodedPassword")
                .role(User.UserRole.BUSINESS_ADMIN)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtils.generateToken(savedUser)).thenReturn("jwt-token");

        LoginResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(savedUser.getEmail(), response.getEmail());
        assertEquals("jwt-token", response.getAccessToken());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository).save(any(User.class));
        verify(jwtUtils).generateToken(savedUser);
    }

    @Test
    void register_emailAlreadyExists_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.register(request));
        assertEquals("Email already registered", exception.getMessage());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class));
        verify(jwtUtils, never()).generateToken(any());
    }

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .password("encodedPassword")
                .role(User.UserRole.BUSINESS_ADMIN)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("jwt-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals("jwt-token", response.getAccessToken());

        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(jwtUtils).generateToken(user);
    }

    @Test
    void login_invalidEmail_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", exception.getMessage());

        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtUtils, never()).generateToken(any());
    }

    @Test
    void login_invalidPassword_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .password("encodedPassword")
                .role(User.UserRole.BUSINESS_ADMIN)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", exception.getMessage());

        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(jwtUtils, never()).generateToken(any());
    }
}
