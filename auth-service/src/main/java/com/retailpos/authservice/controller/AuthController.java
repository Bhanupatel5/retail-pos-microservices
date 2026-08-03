package com.retailpos.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.retailpos.authservice.dto.request.LoginRequest;
import com.retailpos.authservice.dto.request.RegisterRequest;
import com.retailpos.authservice.dto.response.LoginResponse;
import com.retailpos.authservice.dto.response.RegisterResponse;
import com.retailpos.authservice.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @GetMapping("/test")
    public String test() {
        return "JWT Working";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {
    	
    	 System.out.println("Inside Controller");

        RegisterResponse response = authService.register(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}