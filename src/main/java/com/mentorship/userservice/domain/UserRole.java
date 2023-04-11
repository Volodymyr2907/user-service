package com.mentorship.userservice.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@NoArgsConstructor
public class UserRole {

    @EmbeddedId
    @EqualsAndHashCode.Include
    @ToString.Include
    private UserRoleId userRoleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

}
