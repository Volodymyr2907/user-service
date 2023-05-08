package com.mentorship.userservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mentorship.userservice.domain.Auth;
import com.mentorship.userservice.domain.User;
import com.mentorship.userservice.dto.enums.UserRole;
import com.mentorship.userservice.repositories.UserRepository;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AuthRepositoryTest {

    public static final String USER_TARAS_EMAIL = "taras@gmail.com";
    public static final String USER_TARAS_LAST_NAME = "Tarasenko";
    public static final String USER_IVAN_EMAIL = "ivan@gmail.com";
    public static final String USER_IVAN_NAME = "Ivan";
    public static final String USER_TARAS_NAME = "Taras";
    public static final String USER_IVAN_LAST_NAME = "Ivanenko";
    public static final String USER_IVAN_PHONE_NUMBER = "+111111111";

    @Autowired
    private UserRepository userRepository;

    private User createdUserIvan;

    @BeforeEach
    public void createUsers() {

        User userIvanDataToSave = createUserObject(USER_IVAN_NAME, USER_IVAN_LAST_NAME, USER_IVAN_PHONE_NUMBER, USER_IVAN_EMAIL);
        User userTarasDataToSave = createUserObject(USER_TARAS_NAME, USER_TARAS_LAST_NAME, USER_IVAN_PHONE_NUMBER,
            USER_TARAS_EMAIL);
        createdUserIvan = userRepository.save(userIvanDataToSave);
        userRepository.save(userTarasDataToSave);
    }

    @Test
    public void shouldReturnTrueIfEmailPresentInDb() {
        Boolean isEmailExist = userRepository.existsByLoginDetails_Email(USER_IVAN_EMAIL);

        assertThat(isEmailExist).isTrue();
    }

    @Test
    public void shouldReturnFalseIfEmailNotPresentInDb() {
        Boolean isEmailExist = userRepository.existsByLoginDetails_Email("notExistingEmail");

        assertThat(isEmailExist).isFalse();
    }

    @Test
    public void shouldReturnTrueIfCombinationFirstNameAndLastNameAndPhoneNumberPresentInDb() {
        Boolean isCombinationExist = userRepository.existsByFirstNameAndLastNameAndPhoneNumber(USER_IVAN_NAME,
            USER_IVAN_LAST_NAME,
            USER_IVAN_PHONE_NUMBER);

        assertThat(isCombinationExist).isTrue();
    }

    @Test
    public void shouldReturnFalseIfCombinationFirstNameAndLastNameAndPhoneNumberNotPresentInDb() {
        Boolean isCombinationExist = userRepository.existsByFirstNameAndLastNameAndPhoneNumber(USER_TARAS_NAME,
            USER_IVAN_LAST_NAME,
            USER_IVAN_PHONE_NUMBER);

        assertThat(isCombinationExist).isFalse();
    }

    @Test
    public void shouldReturnUserByEmail() {
        User actualUser = userRepository.findByLoginDetails_Email(USER_IVAN_EMAIL).get();

        assertThat(actualUser).isEqualTo(createdUserIvan);
    }


    private User createUserObject(String name, String lastName, String phoneNumber, String email) {
        User user = new User();
        user.setAuthorities(Set.of(UserRole.USER));
        user.setFirstName(name);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        Auth auth = new Auth();
        auth.setEmail(email);
        auth.setPassword("password");
        user.setLoginDetails(auth);
        return user;
    }

}
