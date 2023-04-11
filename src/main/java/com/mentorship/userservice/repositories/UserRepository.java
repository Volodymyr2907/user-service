package com.mentorship.userservice.repositories;

import com.mentorship.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByLoginDetails_Email(String email);

    Boolean existsByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber);

    User findByLoginDetails_Email(String email);
}
