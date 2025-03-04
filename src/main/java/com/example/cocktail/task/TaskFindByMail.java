package com.example.cocktail.task;

import com.example.cocktail.dao.UserEntityDao;
import com.example.cocktail.model.UserEntity;
import javafx.concurrent.Task;

import java.util.Optional;

public class TaskFindByMail extends Task<Optional<UserEntity>> {

    private UserEntityDao userEntityDao;
    private String mail;

    public TaskFindByMail(UserEntityDao userEntityDao, String mail) {
        this.userEntityDao = userEntityDao;
        this.mail = mail;
    }


    @Override
    protected Optional<UserEntity> call() throws Exception {
        //Busca si algún usuario está hacienod uso del correo indicado
        return userEntityDao.findUserByMail(mail);
    }
}
