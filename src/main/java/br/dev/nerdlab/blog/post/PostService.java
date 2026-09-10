package br.dev.nerdlab.blog.post;

import br.dev.nerdlab.blog.authentication.User;
import br.dev.nerdlab.blog.authentication.UserRepository;
import br.dev.nerdlab.blog.post.dto.PostCreateDTO;
import br.dev.nerdlab.blog.post.dto.PostResponseDTO;
import br.dev.nerdlab.blog.post.dto.PostUpdateDTO;
import br.dev.nerdlab.blog.security.user.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDTO createPost(PostCreateDTO dto, UserPrincipal currentUser){

        User author = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Author não encontrado"));

        Post post = new Post();
        post.setTitle(dto.title());
        post.setSlug(generateUniqueSlug(dto.title()));
        post.setContent(dto.content());
        post.setSummary(dto.summart());
        post.setPublished(dto.published() != null ? dto.published() : true);
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);
        return PostResponseDTO.fromEntity(savedPost);
    }

    public List<PostResponseDTO> getAllPublishedPosts(){
        return postRepository.findByPublishedTrueOrderByCreatedAtDesc()
                .stream()
                .map(PostResponseDTO::fromEntity)
                .toList();
    }

    public PostResponseDTO getPostBySlug(String slug){
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Post Não encontrado com o slug: " + slug));
        return PostResponseDTO.fromEntity(post);
    }

    public PostResponseDTO updatePost(UUID id, PostUpdateDTO dto, UserPrincipal currentUser){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post Not found"));

        validateOwnerShip(post, currentUser);

        if(dto.title() != null && !dto.title().equalsIgnoreCase(post.getTitle())){
            post.setTitle(dto.title());
            post.setSlug(generateUniqueSlug(dto.title()));
        }

        if(dto.content() != null) post.setContent(dto.content());
        if(dto.summary() != null) post.setSummary(dto.summary());
        if(dto.published() != null) post.setPublished(dto.published());

        Post updatedPost = postRepository.save(post);

        return PostResponseDTO.fromEntity(updatedPost);
    }

    public void deletePost(UUID id, UserPrincipal currentUser){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        validateOwnerShip(post, currentUser);

        postRepository.delete(post);
    }

    private void validateOwnerShip(Post post, UserPrincipal currentUser) {
        boolean isAuthor = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getUser().getRole().name().equals("ADMIN");

        if(!isAuthor && !isAdmin){
            throw new IllegalStateException("Acesso Negado: você não tem permissão para alterar");
        }
    }



    private String generateUniqueSlug(String title) {
        String baseSlug = toSlug(title);
        String slug = baseSlug;
        int count = 1;

        while(postRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + count;
            count ++;
        }

        return slug;
    }

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    private String toSlug(String input) {
        if(input == null) return "";
        String noWhiteSpace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace,Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }


}
