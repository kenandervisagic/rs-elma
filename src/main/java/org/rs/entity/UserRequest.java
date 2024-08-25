package org.rs.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
    public UserRequest(String name, String username, String password, String email, String role) {
        this.fullName = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleName = role;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "role_name")
    private String roleName;

}
