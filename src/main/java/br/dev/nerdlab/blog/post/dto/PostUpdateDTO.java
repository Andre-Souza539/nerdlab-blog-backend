package br.dev.nerdlab.blog.post.dto;

public record PostUpdateDTO(
        String title,
        String content,
        String summary,
        Boolean published
) {
}
