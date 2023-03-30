package com.mentorship.userservice.controllers;

import com.mentorship.userservice.dto.AuthTokenDto;
import com.mentorship.userservice.dto.LoginDto;
import com.mentorship.userservice.dto.RegistrationDto;
import com.mentorship.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);

        AuthTokenDto jwtAuthResponse = new AuthTokenDto();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDto registerDto) {
        String response = authService.registration(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
