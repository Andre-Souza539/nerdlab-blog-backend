package br.dev.nerdlab.blog.post.dto;

public record PostCreateDTO(
        String title,
        String content,
        String summart,
        Boolean published
) {
}
