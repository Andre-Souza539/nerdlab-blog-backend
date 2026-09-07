package br.dev.nerdlab.blog.authentication;

import br.dev.nerdlab.blog.authentication.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tb_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String firstName, String lastName, String username, String password, String email, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = firstName + lastName;
        this.email = email;
        this.role = Role.USER;
    }

    public User(UserDTO dto, String hashedPassword) {
        this.firstName = dto.firstName();
        this.lastName = dto.lastName();
        this.username = dto.username() != null ? dto.username() : dto.firstName() + dto.lastName();
        this.password = hashedPassword;
        this.email = dto.email();
        this.role = dto.role() != null ? dto.role() : Role.USER;
    }
}
