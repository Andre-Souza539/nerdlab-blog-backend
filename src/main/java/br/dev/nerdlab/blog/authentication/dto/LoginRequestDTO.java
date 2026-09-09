package br.dev.nerdlab.blog.authentication.dto;

public record LoginRequestDTO(
        String email,
        String password
) {
}
