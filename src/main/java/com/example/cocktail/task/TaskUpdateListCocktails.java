package com.example.cocktail.task;

import com.example.cocktail.dao.CocktailEntityDao;
import com.example.cocktail.dao.UserEntityDao;
import com.example.cocktail.model.CocktailEntity;
import com.example.cocktail.model.UserEntity;
import javafx.concurrent.Task;

import java.util.List;
import java.util.Optional;

public class TaskUpdateListCocktails extends Task<Void> {

    private CocktailEntity cocktailEntity;
    private CocktailEntityDao cocktailEntityDao;
    private UserEntity userEntity;
    private UserEntityDao userEntityDao;

    public TaskUpdateListCocktails(CocktailEntity cocktailEntity, CocktailEntityDao cocktailEntityDao, UserEntity userEntity, UserEntityDao userEntityDao) {
        this.cocktailEntity = cocktailEntity;
        this.cocktailEntityDao = cocktailEntityDao;
        this.userEntity = userEntity;
        this.userEntityDao = userEntityDao;
    }

    @Override
    protected Void call() throws Exception {
        Optional<CocktailEntity> optionalCocktail = cocktailEntityDao.findCocktailById(cocktailEntity.getIdDrink());
        //Si el coctail no está en la base de datos. Actualiza los coctail y la lista del usuario
        if (optionalCocktail.isEmpty()){
            cocktailEntityDao.addCocktailEntity(CocktailEntity.builder()
                    .idDrink(this.cocktailEntity.getIdDrink())
                    .strDrink(this.cocktailEntity.getStrDrink())
                    .strDrinkThumb(this.cocktailEntity.getStrDrinkThumb())
                    .build());
            userEntityDao.updateListCocktail(this.userEntity.getId(), List.of(this.cocktailEntity));
            return null;
        // Si no, solo actualiza la lista del usuario
        }else {
            userEntityDao.updateListCocktail(this.userEntity.getId(), List.of(this.cocktailEntity));
        }
        return null;
    }
}
