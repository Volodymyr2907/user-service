package com.mentorship.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {

    @Email(message = "Email is not valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
        flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotBlank(message = "Email could not be empty or null")
    @Length(max = 254, message = "Email is too long")
    private String email;

    @NotBlank(message = "Password can not be empty or null")
    @Length(max = 255, message = "Password is too long")
    private String password;

    @NotBlank(message = "First name can not be empty or null")
    @Length(max = 255, message = "First name is too long")
    private String firstName;

    @NotBlank(message = "Last name can not be empty or null")
    @Length(max = 255, message = "Last name is too long")
    private String lastName;

    @NotBlank(message = "Phone number can not be empty or null")
    @Length(max = 14, message = "Phone number is too long")
    private String phoneNumber;

}
