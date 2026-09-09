package br.dev.nerdlab.blog.authentication.dto;

import br.dev.nerdlab.blog.authentication.Role;
import java.util.UUID;

public record LoginResponseDTO(
        String accessToken,
        String tokenType,
        long expiresIn,
        UUID userId,
        String email,
        Role role
) {
    public LoginResponseDTO(String accessToken, long expiresIn, UUID userId, String email, Role role){
        this(accessToken, "Bearer", expiresIn, userId, email, role);
    }
}
