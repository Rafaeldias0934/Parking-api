package com.example.demo_park_api.jwt;

import com.example.demo_park_api.entity.User;
import com.example.demo_park_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) {
            log.warn("Usuário '{}' não encontrado!", username);
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        log.info("Usuário '{}' encontrado", username);
        return new JwtUserDetails(user);
    }

    public  JwtToken getTokenAuthenticated(String username) {
       User.Role role = userService.findRoleByUsername(username);

        if (role != null) {
            String roleName = role.name();
            String roleNameWithoutPrefix = roleName.startsWith("ROLE_ ") ?
                    roleName.substring("ROLE_".length()) : roleName;

            return JwtUtils.createToken(username, roleNameWithoutPrefix);
        } else {
            throw new IllegalArgumentException("Role not found for username: " + username);
        }

    }
}
