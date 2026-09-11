package br.dev.nerdlab.blog.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    Optional<Post> findBySlug(String slug);

    Page<Post> findByPublishedTrue(Pageable pageable);

    Page<Post> findByPublishedTrueAndTitleContainingIgnoreCase(String title, Pageable pageable);

    boolean existsBySlug(String slug);

}
