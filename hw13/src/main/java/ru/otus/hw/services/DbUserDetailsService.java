package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.SystemUser;
import ru.otus.hw.repositories.SystemUserRepository;

import java.util.Collection;

@Service("userDetailsService")
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {

    private final SystemUserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser user = userRepository.findByUsername(username);
        return new User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(SystemUser user) {
        return user.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .toList();
    }
}
