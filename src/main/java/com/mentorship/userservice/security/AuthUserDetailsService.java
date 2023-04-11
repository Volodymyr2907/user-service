package com.mentorship.userservice.security;

import com.mentorship.userservice.domain.User;
import com.mentorship.userservice.repositories.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByLoginDetails_Email(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> authorities = user
            .getRole()
            .stream()
            .map((role) -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getLoginDetails().getEmail(),
            user.getLoginDetails().getPassword(), authorities);
    }
}
