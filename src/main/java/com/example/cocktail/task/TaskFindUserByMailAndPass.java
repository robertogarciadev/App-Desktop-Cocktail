package com.example.cocktail.task;

import com.example.cocktail.dao.UserEntityDao;
import com.example.cocktail.model.UserEntity;
import javafx.concurrent.Task;

import java.util.Optional;

public class TaskFindUserByMailAndPass extends Task<Optional<UserEntity>> {

    private String mail;
    private String password;
    private UserEntityDao userEntityDao;

    public TaskFindUserByMailAndPass(String mail, String password, UserEntityDao userEntityDao) {
        this.mail = mail;
        this.password = password;
        this.userEntityDao = userEntityDao;
    }

    @Override
    protected Optional<UserEntity> call() throws Exception {

        return userEntityDao.findUserByMailAndPass(mail, password);
    }
}
