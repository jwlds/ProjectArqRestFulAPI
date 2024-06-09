package com.puccampinas.projectarqrestfulapi.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.puccampinas.projectarqrestfulapi.domain.car.Car;
import com.puccampinas.projectarqrestfulapi.dtos.auth.SignUpDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document(collection="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    private String id;
    @Indexed(unique=true)
    @NonNull
    private String login;
    @JsonIgnore
    @NonNull
    private String password;
    @NonNull
    private String fullName;

    @DocumentReference
    private List<Car> cars;

    @JsonFormat(pattern="dd/MM/yyyy 'as' HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern="dd/MM/yyyy 'as' HH:mm:ss")
    private LocalDateTime updatedAt;




    public User(SignUpDTO userDTO, String password) {
        this.login = userDTO.getEmail();
        this.password = password;
        this.fullName = userDTO.getFullName();
        this.cars = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return null;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
