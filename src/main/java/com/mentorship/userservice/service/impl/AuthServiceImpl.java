package com.mentorship.userservice.service.impl;

import com.mentorship.userservice.controllers.exeptions.UserValidationException;
import com.mentorship.userservice.domain.User;
import com.mentorship.userservice.dto.LoginDto;
import com.mentorship.userservice.dto.RegistrationDto;
import com.mentorship.userservice.dto.enums.UserRole;
import com.mentorship.userservice.mapper.AuthMapper;
import com.mentorship.userservice.repositories.UserRepository;
import com.mentorship.userservice.security.JwtTokenProvider;
import com.mentorship.userservice.service.AuthService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {


    private static final String USER_WITH_EMAIL_ALREADY_EXIST_MESSAGE = "User with email '%s' already exist";
    private static final String USER_ALREADY_EXIST_WITH_SUCH_DETAILS_MESSAGE = "User with firstName '%s' and lastName '%s' and phoneNumber '%s' already exist";

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private AuthMapper mapper;


    @Override
    public String login(LoginDto loginDto) {

        User user = mapper.loginDtoToUser(loginDto);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            user.getLoginDetails().getEmail(), user.getLoginDetails().getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);

    }

    @Override
    public void signIn(RegistrationDto dto) {

        verifyIfUserExist(dto);
        User userBody = createUserBody(dto);
        userRepository.save(userBody);

    }

    public Boolean hasPermission(String token, String role) {
        jwtTokenProvider.isTokenValid(token);
        String userEmail = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByLoginDetails_Email(userEmail).get();

        Set<UserRole> userRoles = user.getAuthorities();
        UserRole expectedRole = UserRole.valueOf(role.toUpperCase());

        return userRoles.contains(expectedRole);
    }


    private void verifyIfUserExist(RegistrationDto dto) {

        if (userRepository.existsByLoginDetails_Email(dto.getEmail())) {
            throw new UserValidationException(HttpStatus.BAD_REQUEST,
                String.format(USER_WITH_EMAIL_ALREADY_EXIST_MESSAGE, dto.getEmail()));
        }

        if (userRepository.existsByFirstNameAndLastNameAndPhoneNumber(dto.getFirstName(),
            dto.getLastName(), dto.getPhoneNumber())) {
            throw new UserValidationException(HttpStatus.BAD_REQUEST,
                String.format(USER_ALREADY_EXIST_WITH_SUCH_DETAILS_MESSAGE,
                    dto.getFirstName(), dto.getLastName(), dto.getPhoneNumber()));
        }
    }

    private User createUserBody(RegistrationDto dto) {
        User user = mapper.registrationDtoToUser(dto);
        user.getLoginDetails().setEmail(dto.getEmail());
        user.getLoginDetails().setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAuthorities(Set.of(UserRole.USER));
        return user;
    }
}
