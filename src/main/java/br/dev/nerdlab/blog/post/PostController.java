package br.dev.nerdlab.blog.post;

import br.dev.nerdlab.blog.post.dto.PostCreateDTO;
import br.dev.nerdlab.blog.post.dto.PostResponseDTO;
import br.dev.nerdlab.blog.post.dto.PostUpdateDTO;
import br.dev.nerdlab.blog.security.user.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ){
        return ResponseEntity.ok(postService.getAllPublishedPosts(page, size, search));
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
                .path("/{slug}")
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
