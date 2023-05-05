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

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private String user1Email;
    private String user1Name;
    private String user1LastName;
    private String user1PhoneNumber;
    private String user2Name;


    @BeforeEach
    public void createUsers() {

        user1Email = "ivan@gmail.com";
        String user2Email = "taras@gmail.com";
        user1Name = "Ivan";
        user2Name = "Taras";
        user1LastName = "Ivanenko";
        String user2LastName = "Tarasenko";
        user1PhoneNumber = "+111111111";
        User user1Object = createUserObject(user1Name, user1LastName, user1PhoneNumber, user1Email);
        User user2Object = createUserObject(user2Name, user2LastName, user1PhoneNumber, user2Email);
        user1 = userRepository.save(user1Object);
        userRepository.save(user2Object);
    }

    @Test
    public void shouldReturnTrueIfEmailPresentInDb() {
        Boolean isEmailExist = userRepository.existsByLoginDetails_Email(user1Email);

        assertThat(isEmailExist).isTrue();
    }

    @Test
    public void shouldReturnFalseIfEmailNotPresentInDb() {
        Boolean isEmailExist = userRepository.existsByLoginDetails_Email("notExistingEmail");

        assertThat(isEmailExist).isFalse();
    }

    @Test
    public void shouldReturnTrueIfCombinationFirstNameAndLastNameAndPhoneNumberPresentInDb() {
        Boolean isCombinationExist = userRepository.existsByFirstNameAndLastNameAndPhoneNumber(user1Name, user1LastName,
            user1PhoneNumber);

        assertThat(isCombinationExist).isTrue();
    }

    @Test
    public void shouldReturnFalseIfCombinationFirstNameAndLastNameAndPhoneNumberNotPresentInDb() {
        Boolean isCombinationExist = userRepository.existsByFirstNameAndLastNameAndPhoneNumber(user2Name, user1LastName,
            user1PhoneNumber);

        assertThat(isCombinationExist).isFalse();
    }

    @Test
    public void shouldReturnUserByEmail() {
        User actualUser = userRepository.findByLoginDetails_Email(user1Email).get();

        assertThat(actualUser).isEqualTo(user1);
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
