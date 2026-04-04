package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Spring Security calls this with email during login (AuthenticationManager).
     * JwtAuthFilter calls this with numeric user ID from the token subject.
     * We handle both cases here.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String value) throws UsernameNotFoundException {
        try {
            // Called from JwtAuthFilter — value is a numeric user ID
            Long id = Long.parseLong(value);
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
            return UserPrincipal.from(user);
        } catch (NumberFormatException e) {
            // Called from AuthenticationManager during login — value is an email
            User user = userRepository.findByEmail(value)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + value));
            return UserPrincipal.from(user);
        }
    }
}