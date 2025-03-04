package com.example.cocktail.task;

import com.example.cocktail.dao.UserEntityDao;
import com.example.cocktail.model.UserEntity;
import javafx.concurrent.Task;

import java.util.Optional;

public class TaskRegisterUser extends Task<Void> {

    private UserEntityDao userEntityDao;
    private  UserEntity userEntity;

    public TaskRegisterUser(UserEntityDao userEntityDao, UserEntity userEntity) {
        this.userEntityDao = userEntityDao;
        this.userEntity = userEntity;
    }

    @Override
    protected Void call() throws Exception {
        //Progreso que va pintando el indicator progress
        for (int i = 0; i <= 100; i++) {
            Thread.sleep(50);
            updateProgress(i, 100); // Actualiza indicatorPregress
        }
        userEntityDao.addUserEntity(userEntity);
        updateProgress(100, 100);
        return null;
    }
}
