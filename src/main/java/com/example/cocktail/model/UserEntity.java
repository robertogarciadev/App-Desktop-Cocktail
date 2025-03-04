package com.example.cocktail.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String mail;
    @Column
    private String gender;
    @Column
    private String origin;
    @Column
    private String password;

    @ManyToMany(targetEntity = CocktailEntity.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_cocktail",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "cocktail_id")
    )
    private Set<CocktailEntity> cocktailsList = new HashSet<>();

    public UserEntity(String name, String secondName, String gender, String origin, String password) {
        this.name = name;
        this.mail = secondName;
        this.gender = gender;
        this.origin = origin;
        this.password = password;
    }
}
