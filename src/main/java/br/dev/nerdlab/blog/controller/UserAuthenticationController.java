package br.dev.nerdlab.blog.controller;

import br.dev.nerdlab.blog.authentication.User;
import br.dev.nerdlab.blog.authentication.dto.LoginRequestDTO;
import br.dev.nerdlab.blog.authentication.dto.LoginResponseDTO;
import br.dev.nerdlab.blog.authentication.dto.UserDTO;
import br.dev.nerdlab.blog.service.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/auth/")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserAuthenticationController {

    private final UserAuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
        User savedUser = authenticationService.createUser(userDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedUser);
    }

    // NOVO ENDPOINT DE LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginDTO) {
        LoginResponseDTO response = authenticationService.authenticateUser(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<User> saveUser(@RequestBody UserDTO userDTO){

        User savedUser = authenticationService.createUser(userDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> listAll(){
        List<User> usersFound = authenticationService.listAll();
        return ResponseEntity.ok().body(usersFound);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> listById(@PathVariable UUID id){
        return authenticationService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
