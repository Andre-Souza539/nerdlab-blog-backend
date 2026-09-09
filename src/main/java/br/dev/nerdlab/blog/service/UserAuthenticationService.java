package br.dev.nerdlab.blog.service;

import br.dev.nerdlab.blog.authentication.User;
import br.dev.nerdlab.blog.authentication.UserRepository;
import br.dev.nerdlab.blog.authentication.dto.LoginRequestDTO;
import br.dev.nerdlab.blog.authentication.dto.LoginResponseDTO;
import br.dev.nerdlab.blog.authentication.dto.UserDTO;
import br.dev.nerdlab.blog.security.jwt.JwtTokenProvider;
import br.dev.nerdlab.blog.security.user.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserAuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public User createUser(UserDTO dto){

        if(repository.existsByEmail(dto.email())){
            throw new IllegalArgumentException("Error: E-mail Already Exists");
        }

        String hashedPassword = passwordEncoder.encode(dto.password());
        User user = new User(dto, hashedPassword);
        return repository.save(user);
    }

    public List<User> listAll(){
        return repository.findAll();
    }

    public Optional<User> findById(UUID id){
        return repository.findById(id);
    }

    public LoginResponseDTO authenticateUser(LoginRequestDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(),loginDTO.password())
        );

        String token = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return new LoginResponseDTO(
                token,
                tokenProvider.getJwtExpirationMs(),
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getUser().getRole()
        );
    }



}
