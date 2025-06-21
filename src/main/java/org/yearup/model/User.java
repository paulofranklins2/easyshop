package org.yearup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yearup.model.authentication.Authority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    private String username;

    @JsonIgnore
    @Column(name = "hashed_password")
    private String password;

    @Column(name = "role")
    private String role;

    @JsonIgnore
    @Transient
    private boolean activated = true;

    @Transient
    private Set<Authority> authorities = new HashSet<>();

    @PostLoad
    private void fillAuthorities() {
        setRole(this.role);
    }

    public void setRole(String role) {
        this.role = role;
        this.authorities.clear();
        if (role != null && !role.isBlank()) {
            addRole(role);
        }
    }

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.setRole(role);
        this.activated = true;
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.setRole(role);
        this.activated = true;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void setAuthorities(String authorities) {
        String[] roles = authorities.split(",");
        for (String role : roles) {
            addRole(role);
        }
    }

    public void addRole(String role) {
        String authority = role.contains("ROLE_") ? role : "ROLE_" + role;
        this.authorities.add(new Authority(authority));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                activated == user.activated &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(authorities, user.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, activated, authorities);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", activated=" + activated +
                ", authorities=" + authorities +
                '}';
    }

    @JsonIgnore
    public String getRole() {
        if (this.role != null) {
            return this.role.toUpperCase();
        }
        if (authorities.size() > 0) {
            for (Authority role : authorities) {
                return role.getName().toUpperCase();
            }
        }

        return "ROLE_USER";
    }
}
