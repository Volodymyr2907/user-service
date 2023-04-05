package com.mentorship.userservice.repositories;

import com.mentorship.userservice.domain.UserRole;
import com.mentorship.userservice.domain.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

}
