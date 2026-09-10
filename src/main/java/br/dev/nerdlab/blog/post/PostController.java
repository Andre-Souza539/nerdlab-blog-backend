package br.dev.nerdlab.blog.post;

import br.dev.nerdlab.blog.authentication.User;
import br.dev.nerdlab.blog.post.dto.PostCreateDTO;
import br.dev.nerdlab.blog.post.dto.PostResponseDTO;
import br.dev.nerdlab.blog.post.dto.PostUpdateDTO;
import br.dev.nerdlab.blog.security.user.UserPrincipal;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPublishedPosts());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PostResponseDTO> getPostBySlug(@PathVariable String slug){
        return ResponseEntity.ok(postService.getPostBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestBody PostCreateDTO dto,
            @AuthenticationPrincipal UserPrincipal currentUser
            ){
        PostResponseDTO response = postService.createPost(dto, currentUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{slug}")
                .buildAndExpand(response.slug())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable UUID id,
            @RequestBody PostUpdateDTO dto,
            @AuthenticationPrincipal UserPrincipal currentUser){

        return ResponseEntity.ok(postService.updatePost(id,dto,currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal currentUser){
        postService.deletePost(id,currentUser);
        return ResponseEntity.noContent().build();
    }

}
