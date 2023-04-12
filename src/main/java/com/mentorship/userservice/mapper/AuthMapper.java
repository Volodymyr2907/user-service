package com.mentorship.userservice.mapper;

import com.mentorship.userservice.domain.User;
import com.mentorship.userservice.dto.LoginDto;
import com.mentorship.userservice.dto.RegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "loginDetails.email", source = "email")
    @Mapping(target = "loginDetails.password", source = "password")
    User loginDtoToUser(LoginDto loginDto);

    @Mapping(target = "loginDetails.email", source = "email")
    @Mapping(target = "loginDetails.password", source = "password")
    User registrationDtoToUser(RegistrationDto loginDto);


}
