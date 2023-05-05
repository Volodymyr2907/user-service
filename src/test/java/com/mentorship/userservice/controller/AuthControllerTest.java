package com.mentorship.userservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorship.userservice.controllers.AuthController;
import com.mentorship.userservice.controllers.exeptions.ErrorResponse;
import com.mentorship.userservice.dto.AuthTokenDto;
import com.mentorship.userservice.dto.LoginDto;
import com.mentorship.userservice.dto.RegistrationDto;
import com.mentorship.userservice.security.JwtAuthenticationFilter;
import com.mentorship.userservice.service.AuthService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {

    private static final String LOGIN_URL = "/api/auth-service/login";
    private static final String SIGN_IN_URL = "/api/auth-service/user";


    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private LoginDto defaultLoginDto;
    private RegistrationDto defaultRegistrationDto;

    @BeforeEach
    public void setupTestData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        defaultLoginDto = new LoginDto();
        defaultLoginDto.setEmail("email@gmail.com");
        defaultLoginDto.setPassword("password");

        defaultRegistrationDto = new RegistrationDto();
        defaultRegistrationDto.setEmail("email@gmail.com");
        defaultRegistrationDto.setPassword("password");
        defaultRegistrationDto.setFirstName("User");
        defaultRegistrationDto.setLastName("User");
        defaultRegistrationDto.setPhoneNumber("+45858494045");
    }


    @Test
    public void shouldLoginWithOkStatusCodeAndReturnToken() throws Exception {

        String randomString = "random token";

        AuthTokenDto expectedObject = new AuthTokenDto();
        expectedObject.setAccessToken(randomString);

        when(authService.login(any(LoginDto.class))).thenReturn(randomString);

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(defaultLoginDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedObject)));
    }

    @Test
    public void shouldNotLoginWithoutEmailAndReturnBadRequest() throws Exception {

        defaultLoginDto.setEmail(null);

        String contextResponse = mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(defaultLoginDto)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(contextResponse)).containsOnly("Email could not be empty or null");

    }

    @Test
    public void shouldNotLoginWithInvalidEmailAndReturnBadRequest() throws Exception {

        defaultLoginDto.setEmail("invalidEmail");

        String contextResponse = mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(defaultLoginDto)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(contextResponse)).containsOnly("Email is not valid");

    }

    @Test
    public void shouldNotLoginWithoutPasswordAndReturnBadRequest() throws Exception {

        defaultLoginDto.setPassword(null);

        String contextResponse = mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(defaultLoginDto)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(contextResponse)).containsOnly("Password can not be empty or null");

    }

    @Test
    public void shouldRegisterUserAndReturnStatusCreated() throws Exception {

        mockMvc.perform(post(SIGN_IN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(defaultRegistrationDto)))
            .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotRegisterUserWithInvalidEmailAndReturnBadRequest() throws Exception {

        defaultRegistrationDto.setEmail("invalidEmail");

        String contextResponse = mockMvc.perform(post(SIGN_IN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(defaultRegistrationDto)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(contextResponse)).containsOnly("Email is not valid");


    }

    @Test
    public void shouldNotRegisterUserWithoutEmailAndReturnBadRequest() throws Exception {

        defaultRegistrationDto.setEmail(null);

        String contextResponse = mockMvc.perform(post(SIGN_IN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(defaultRegistrationDto)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(contextResponse)).containsOnly("Email could not be empty or null");


    }

    private List<String> getErrorListFromResponse(String responseAsString) throws JsonProcessingException {
        return objectMapper.readValue(responseAsString, ErrorResponse.class).getErrors();
    }


}
