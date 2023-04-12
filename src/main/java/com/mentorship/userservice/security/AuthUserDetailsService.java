package com.mentorship.userservice.security;

import com.mentorship.userservice.domain.User;
import com.mentorship.userservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByLoginDetails_Email(userEmail);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }
        return new org.springframework.security.core.userdetails.User(user.getLoginDetails().getEmail(),
            user.getLoginDetails().getPassword(),
            getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getAuthority().forEach(role ->
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        return authorities;
    }
}
