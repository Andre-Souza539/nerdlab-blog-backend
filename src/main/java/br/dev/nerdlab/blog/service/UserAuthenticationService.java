package br.dev.nerdlab.blog.service;

import br.dev.nerdlab.blog.authentication.User;
import br.dev.nerdlab.blog.authentication.UserRepository;
import br.dev.nerdlab.blog.authentication.dto.UserDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserAuthenticationService {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public UserAuthenticationService(BCryptPasswordEncoder encoder, UserRepository repository) {
        this.passwordEncoder = encoder;
        this.repository = repository;
    }

    public User createUser(UserDTO dto){
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

}
