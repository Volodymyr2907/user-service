package com.mentorship.userservice.service;

import com.mentorship.userservice.controllers.exeptions.UserValidationException;
import com.mentorship.userservice.dto.LoginDto;
import com.mentorship.userservice.dto.RegistrationDto;

public interface AuthService {

    String login(LoginDto loginDto);

    void signIn(RegistrationDto registerDto) throws UserValidationException;

}
