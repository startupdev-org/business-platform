package com.platform.service;

import com.platform.dto.auth.LoginRequest;
import com.platform.dto.auth.LoginResponse;
import com.platform.dto.auth.RegisterRequest;
import com.platform.dto.auth.WhoAmIResponseDTO;
import com.platform.entity.User;
import com.platform.exception.BusinessException;
import com.platform.repository.UserRepository;
import com.platform.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.UserRole.BUSINESS_ADMIN)
                .build();

        user = userRepository.save(user);
        String token = jwtUtils.generateToken(user);

        return LoginResponse.fromUser(user, token);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }

        String token = jwtUtils.generateToken(user);
        return LoginResponse.fromUser(user, token);
    }
}
