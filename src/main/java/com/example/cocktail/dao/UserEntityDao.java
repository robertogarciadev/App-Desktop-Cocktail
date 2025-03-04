package com.example.cocktail.dao;

import com.example.cocktail.data.HibernateUtils;
import com.example.cocktail.model.CocktailEntity;
import com.example.cocktail.model.UserEntity;
import com.mysql.cj.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserEntityDao {

    //Acciones contra base de datos de tipo Usuario

    private Session session;

    public void addUserEntity(UserEntity userEntity){
        session = new  HibernateUtils().getSessionFactory().getCurrentSession(); //Llama al sesion factory marcado como static
        session.beginTransaction(); // Comienza la transacción
        session.persist(userEntity);
        session.getTransaction().commit(); // Asegura la transacción
        session.close(); //Cierra la transacción
    }

    public void updateListCocktail(Long idUser, List<CocktailEntity> listCocktail){
        session = new  HibernateUtils().getSessionFactory().getCurrentSession(); //Llama al sesion factory marcado como static
        session.beginTransaction(); // Comienza la transacción
        UserEntity userEntity = session.get(UserEntity.class, idUser); //Encuentra al usuario por id
        Set<CocktailEntity> listCocktailUser = userEntity.getCocktailsList(); //Separa la lista de cocteles del usuario
        listCocktailUser.addAll(listCocktail); //Añade las bebidas pasadas por parámtros
        userEntity.setCocktailsList(listCocktailUser); //Setea lista de bebidas actualizada
        session.persist(userEntity); //Actualiza usuario
        session.getTransaction().commit(); // Asegura la transacción
        session.close(); //Cierra la transacción
    }
    public  boolean existCocktail(Long idUser, String idCocktail){
        session = new  HibernateUtils().getSessionFactory().getCurrentSession(); //Llama al sesion factory marcado como static
        session.beginTransaction(); // Comienza la transacción
        UserEntity userEntity = session.get(UserEntity.class, idUser); //Encuentra al usuario por id
        boolean cocktailExist = userEntity.getCocktailsList().stream() //Comprueba si el usuario ya tiene el coctail en favoritos
                .map(CocktailEntity::getIdDrink)
                .toList().contains(idCocktail);
        session.getTransaction().commit(); // Asegura la transacción
        session.close(); //Cierra la transacción
        return cocktailExist;
    }
    public void deleteCocktailToFavorites(Long idUser, String idCocktail){
        session = new  HibernateUtils().getSessionFactory().getCurrentSession(); //Llama al sesion factory marcado como static
        session.beginTransaction(); // Comienza la transacción
        UserEntity userEntity = session.get(UserEntity.class, idUser); //Encuentra al usuario por id
        Set<CocktailEntity> listCocktailUser = userEntity.getCocktailsList(); //Separa la lista de cocteles del usuario
        Set<CocktailEntity> listCocktailUserUpdated = listCocktailUser.stream() //Crea nueva set sin el cocktail a borar
                        .filter(cocktailEntity -> !cocktailEntity.getIdDrink().equals(idCocktail))
                                .collect(Collectors.toSet());
        userEntity.setCocktailsList(listCocktailUserUpdated);
        session.persist(userEntity);
        session.getTransaction().commit(); // Asegura la transacción
        session.close(); //Cierra la transacción
    }

    public Optional<UserEntity> findUserByMail(String mail){
        session = new  HibernateUtils().getSessionFactory().getCurrentSession(); //Llama al sesion factory marcado como static
        session.beginTransaction(); // Comienza la transacción
        Query<UserEntity> query = session.createQuery("FROM UserEntity u WHERE u.mail =:mail", UserEntity.class);
        query.setParameter("mail", mail);
        Optional<UserEntity> userEntity = query.uniqueResultOptional();
        session.getTransaction().commit(); // Asegura la transacción
        session.close(); //Cierra la transacción
        return userEntity;
    }
    public Optional<UserEntity> findUserByMailAndPass(String mail, String password){
        session = new  HibernateUtils().getSessionFactory().getCurrentSession(); //Llama al sesion factory marcado como static
        session.beginTransaction(); // Comienza la transacción
        Query<UserEntity> query = session.createQuery(
                "FROM UserEntity u WHERE u.mail =:mail AND u.password=:password", UserEntity.class);
        query.setParameter("mail", mail);
        query.setParameter("password", password);
        Optional<UserEntity> userEntity = query.uniqueResultOptional();
        session.getTransaction().commit(); // Asegura la transacción
        session.close(); //Cierra la transacción
        return userEntity;
    }
}
