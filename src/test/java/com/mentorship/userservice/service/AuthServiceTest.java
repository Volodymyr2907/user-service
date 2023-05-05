package com.mentorship.userservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mentorship.userservice.controllers.exeptions.UserValidationException;
import com.mentorship.userservice.domain.Auth;
import com.mentorship.userservice.domain.User;
import com.mentorship.userservice.dto.RegistrationDto;
import com.mentorship.userservice.dto.enums.UserRole;
import com.mentorship.userservice.mapper.AuthMapper;
import com.mentorship.userservice.repositories.UserRepository;
import com.mentorship.userservice.security.JwtTokenProvider;
import com.mentorship.userservice.service.impl.AuthServiceImpl;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    AuthMapper mapper;

    @InjectMocks
    AuthServiceImpl authService;

    @Test
    public void shouldThrowUserValidationExceptionWhenRegisterUserIfEmailAlreadyExist() {

        RegistrationDto signInDto = createRegistrationDto();

        when(userRepository.existsByLoginDetails_Email(signInDto.getEmail())).thenReturn(true);

        UserValidationException exception = assertThrows(UserValidationException.class,
            () -> authService.signIn(signInDto));

        assertThat(exception.getMessage()).isEqualTo(
            String.format("User with email '%s' already exist", signInDto.getEmail()));
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void shouldThrowUserValidationExceptionWhenRegisterUserIfFirstNameLastNamePhoneNumberAlreadyExist() {

        RegistrationDto signInDto = createRegistrationDto();

        when(userRepository.existsByLoginDetails_Email(signInDto.getEmail())).thenReturn(false);
        when(
            userRepository.existsByFirstNameAndLastNameAndPhoneNumber(signInDto.getFirstName(), signInDto.getLastName(),
                signInDto.getPhoneNumber())).thenReturn(true);

        UserValidationException exception = assertThrows(UserValidationException.class,
            () -> authService.signIn(signInDto));

        assertThat(exception.getMessage()).isEqualTo(
            String.format("User with firstName '%s' and lastName '%s' and phoneNumber '%s' already exist",
                signInDto.getFirstName(), signInDto.getLastName(),
                signInDto.getPhoneNumber()));
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnTrueIfUserRoleMatched() throws UserValidationException {
        User user = new User();
        user.setAuthority(Set.of(UserRole.USER));

        String userEmail = "example@gmail.com";

        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getUsername(any())).thenReturn(userEmail);
        when(userRepository.findByLoginDetails_Email(userEmail)).thenReturn(user);

        Boolean hasUserPermission = authService.hasPermission("TOKEN", UserRole.USER.name());

        assertThat(hasUserPermission).isTrue();
    }

    @Test
    public void shouldReturnFalseIfUserRoleNotMatched() throws UserValidationException {
        User user = new User();
        user.setAuthority(Set.of(UserRole.ADMIN));

        String userEmail = "example@gmail.com";

        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getUsername(any())).thenReturn(userEmail);
        when(userRepository.findByLoginDetails_Email(userEmail)).thenReturn(user);

        Boolean hasUserPermission = authService.hasPermission("TOKEN", UserRole.USER.name());

        assertThat(hasUserPermission).isFalse();
    }

    @Test
    public void shouldThrowUserValidationExceptionIfTokenIsInvalid() throws UserValidationException {

        String invalidToken = "Invalid token";

        when(jwtTokenProvider.validateToken(invalidToken)).thenThrow(
            new UserValidationException(HttpStatus.UNAUTHORIZED, "Invalid JWT token"));

        UserValidationException exception = assertThrows(UserValidationException.class,
            () -> authService.hasPermission(invalidToken, UserRole.USER.name()));

        assertThat(exception.getMessage()).isEqualTo("Invalid JWT token");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


    private RegistrationDto createRegistrationDto() {
        RegistrationDto signInDto = new RegistrationDto();
        signInDto.setFirstName("User1FirstName");
        signInDto.setLastName("User1LastName");
        signInDto.setPhoneNumber("+23930303");
        signInDto.setEmail("user1@gmail.com");
        signInDto.setPassword("password");
        return signInDto;
    }
}
