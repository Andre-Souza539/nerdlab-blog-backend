package br.dev.nerdlab.blog.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    Optional<Post> findBySlug(String slug);

    List<Post> findByPublishedTrueOrderByCreatedAtDesc();

    boolean existsBySlug(String slug);

}
