package com.retailpos.authservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.retailpos.authservice.dto.request.LoginRequest;
import com.retailpos.authservice.dto.request.RegisterRequest;
import com.retailpos.authservice.dto.response.LoginResponse;
import com.retailpos.authservice.dto.response.RegisterResponse;
import com.retailpos.authservice.entity.User;
import com.retailpos.authservice.exception.EmailAlreadyExistsException;
import com.retailpos.authservice.exception.EmployeeAlreadyExistsException;
import com.retailpos.authservice.exception.InvalidCredentialsException;
import com.retailpos.authservice.repository.UserRepository;
import com.retailpos.authservice.security.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest request) {

        // Check Employee ID
        if (userRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new EmployeeAlreadyExistsException("Employee ID already exists");
        }

        // Check Email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setEmployeeId(request.getEmployeeId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Encrypt Password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        user.setActive(true);

        user.setCreatedAt(LocalDateTime.now());

        user.setValidTo(request.getValidTo());

        userRepository.save(user);

        return new RegisterResponse(request.getEmployeeId() ,"User Registered Successfully");
        
        
    }
    
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid Employee ID or Password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Employee ID or Password");
        }

        if (!user.getActive()) {
            throw new RuntimeException("User account is inactive");
        }

        if (user.getValidTo().isBefore(LocalDate.now())) {
            throw new RuntimeException("User account has expired");
        }
        String token = jwtUtil.generateToken(user);

        return new LoginResponse(
                user.getEmployeeId(),
                user.getName(),
                user.getRole().name(),
                token
        );
    }

}