package br.dev.nerdlab.blog.post.dto;

import br.dev.nerdlab.blog.post.Post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponseDTO(
        UUID id,
        String title,
        String slug,
        String content,
        String summary,
        boolean published,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AuthorSummaryDTO author
) {
    public record AuthorSummaryDTO(
            UUID id,
            String firstName,
            String lastName,
            String username
    ){}

    public static PostResponseDTO fromEntity(Post post){
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getSlug(),
                post.getContent(),
                post.getSummary(),
                post.isPublished(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                new AuthorSummaryDTO(
                        post.getAuthor().getId(),
                        post.getAuthor().getFirstName(),
                        post.getAuthor().getLastName(),
                        post.getAuthor().getUsername()
                        )
                );
    }
}

