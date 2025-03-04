package com.example.cocktail.dao;

import com.example.cocktail.data.HibernateUtils;
import com.example.cocktail.model.CocktailEntity;
import com.example.cocktail.model.UserEntity;
import org.hibernate.Session;

import java.util.Optional;

public class CocktailEntityDao {

    private Session session;


    public void addCocktailEntity(CocktailEntity cocktailEntity){
        session = new HibernateUtils().getSessionFactory().getCurrentSession(); //Llama al sesion factory marcado como static
        session.beginTransaction(); // Comienza la transacción
        session.persist(cocktailEntity);
        session.getTransaction().commit(); // Asegura la transacción
        session.close(); //Cierra la transacción
    }

    public Optional<CocktailEntity> findCocktailById(String cocktailId){
        session = new HibernateUtils().getSessionFactory().getCurrentSession(); //Llama al sesion factory marcado como static
        session.beginTransaction(); // Comienza la transacción
        Optional<CocktailEntity> cocktailEntity = Optional.ofNullable(session.get(CocktailEntity.class, cocktailId));
        session.getTransaction().commit(); // Asegura la transacción
        session.close(); //Cierra la transacción
        return cocktailEntity;
    }
}
