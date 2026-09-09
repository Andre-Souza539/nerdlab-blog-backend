package br.dev.nerdlab.blog.security.user;

import br.dev.nerdlab.blog.authentication.User;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserPrincipal implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    // Usamos o e-mail como o identificador principal de login (username do Spring Security)
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    public UUID getId() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
