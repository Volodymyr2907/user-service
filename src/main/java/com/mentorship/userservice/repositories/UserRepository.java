package com.mentorship.userservice.repositories;

import com.mentorship.userservice.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByLoginDetails_Email(String email);

    Boolean existsByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber);

    Optional<User> findByLoginDetails_Email(String email);
}
