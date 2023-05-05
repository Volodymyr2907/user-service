package com.mentorship.userservice.service;

import com.mentorship.userservice.dto.LoginDto;
import com.mentorship.userservice.dto.RegistrationDto;

public interface AuthService {

    String login(LoginDto loginDto);

    void signIn(RegistrationDto registerDto);

    Boolean hasPermission(String token, String role);

}
