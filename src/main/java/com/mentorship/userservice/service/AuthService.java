package com.mentorship.userservice.service;

import com.mentorship.userservice.dto.LoginDto;
import com.mentorship.userservice.dto.RegistrationDto;

public interface AuthService {

    String login(LoginDto loginDto);

    String registration(RegistrationDto registerDto);

}
