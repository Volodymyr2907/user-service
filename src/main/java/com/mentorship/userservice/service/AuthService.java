package com.mentorship.userservice.service;

import com.mentorship.userservice.controllers.exeptions.UserValidationException;
import com.mentorship.userservice.dto.LoginDto;
import com.mentorship.userservice.dto.RegistrationDto;

public interface AuthService {

    String login(LoginDto loginDto);

    void registration(RegistrationDto registerDto) throws UserValidationException;

    Boolean hasPermission(String userEmail, String role) throws UserValidationException;

}
