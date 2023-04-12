package com.mentorship.userservice.controllers;

import com.mentorship.userservice.controllers.exeptions.UserValidationException;
import com.mentorship.userservice.dto.AuthTokenDto;
import com.mentorship.userservice.dto.LoginDto;
import com.mentorship.userservice.dto.RegistrationDto;
import com.mentorship.userservice.service.AuthService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth-service")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        AuthTokenDto jwtAuthResponse = new AuthTokenDto();
        jwtAuthResponse.setAccessToken(authService.login(loginDto));
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/user")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationDto registerDto) throws UserValidationException {
        authService.signIn(registerDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/permission/{token}/{role}")
    public ResponseEntity<Boolean> checkUserPermission(@PathVariable String token, @PathVariable String role)
        throws UserValidationException {
        Boolean hasPermission = authService.hasPermission(token,role);
        return new ResponseEntity<>(hasPermission, HttpStatus.OK);
    }
}
