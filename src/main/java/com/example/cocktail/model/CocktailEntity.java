package com.example.cocktail.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cocktails")
public class CocktailEntity implements Serializable {

    @Id
    @Column (name = "id")
    private String idDrink;
    @Column(name = "name")
    private String strDrink;
    @Column(name = "img")
    private String strDrinkThumb;

    @ManyToMany(mappedBy = "cocktailsList")
    private List<UserEntity> userEntityList;
}
