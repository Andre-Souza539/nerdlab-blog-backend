package br.dev.nerdlab.blog.authentication.dto;

import br.dev.nerdlab.blog.authentication.Role;

public record UserDTO(
        String firstName,
        String lastName,
        String username,
        String password,
        String email,
        Role role
) {
}
