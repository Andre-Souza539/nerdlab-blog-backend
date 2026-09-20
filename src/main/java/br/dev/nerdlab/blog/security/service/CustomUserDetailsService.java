package br.dev.nerdlab.blog.security.service;

import br.dev.nerdlab.blog.authentication.User;
import br.dev.nerdlab.blog.authentication.UserRepository;
import br.dev.nerdlab.blog.security.user.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Não Encontrado: " + email));
        return new UserPrincipal(user);
    }
}
