package com.example.cocktail.controller;

import com.example.cocktail.MainApplication;
import com.example.cocktail.dao.UserEntityDao;
import com.example.cocktail.model.UserEntity;
import com.example.cocktail.request.DataProvider;
import com.example.cocktail.task.TaskFindUserByMailAndPass;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable, EventHandler<ActionEvent> {

    public Button btnLogin;
    public ImageView imgLogin;
    public TextField txtMail;
    public PasswordField txtPass;
    public Hyperlink linkRegister;
    public VBox vBoxParent;

    private UserEntityDao userEntityDao;
    private ProgressIndicator progressIndicator;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userEntityDao = new UserEntityDao();
        progressIndicator = new ProgressIndicator(0);
        progressIndicator.setVisible(false);
        btnLogin.setOnAction(this);
        setImgLogin();
        linkRegister.setOnAction(this);
    }

    private void setImgLogin(){
        imgLogin.setImage(new Image(getClass().getResource("/img_login.jpg").toExternalForm()));
        imgLogin.fitHeightProperty();
        imgLogin.fitWidthProperty();
    }


    @Override
    public void handle(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        // AL PULSAR LOGIN
        if (node ==btnLogin){
            showProgressIndicator(); //Muestra el indicador de progreso
            String mail = txtMail.getText();
            String password =txtPass.getText();
            // Lanza tarea de búsqueda de usuario
            TaskFindUserByMailAndPass taskFindUserByMailAndPass = new TaskFindUserByMailAndPass(mail, password, userEntityDao);
            Platform.runLater(()->{
                progressIndicator.progressProperty().bind(taskFindUserByMailAndPass.progressProperty());
            });
            // Al terminar la tarea
            taskFindUserByMailAndPass.setOnSucceeded(event->{
                Optional<UserEntity> userEntity = taskFindUserByMailAndPass.getValue();
                Platform.runLater(()->{
                    if (userEntity.isPresent()) {
                        try {
                            Stage actualStage = (Stage) btnLogin.getScene().getWindow();
                            actualStage.close(); //Cierra ventana del login
                            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("view-main-cocktail.fxml"));
                            Parent root=loader.load(); // Carga nueva vista
                            Scene sceneMainCocktail = new Scene(root); // Crea nueva Scene
                            MainCocktailController mainCocktailController = loader.getController(); //Obtiene la controladora para pasar parámetros
                            mainCocktailController.getUser(userEntity.get()); // Pasa a la nueva ventana los datos del usuario
                            Stage newStage = new Stage(); //Crea nueva ventana
                            newStage.setScene(sceneMainCocktail);
                            newStage.setResizable(true);
                            newStage.setFullScreen(true);
                            newStage.setTitle("App Cocktail");
                            newStage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        //Lanzar al main de la app
                    } else {
                        Alert alertInfo = new Alert(Alert.AlertType.ERROR, "Correo o mail no encontrado");
                        alertInfo.showAndWait();
                        vBoxParent.getChildren().remove(progressIndicator);
                    }
                });
            });
            new Thread(taskFindUserByMailAndPass).start();

        // AL PULSAR REGISTRO
        } else if (node ==linkRegister) {
            //Abre pantalla de registro
            Scene sceneRegister = DataProvider.getScene("view-register.fxml");
            Stage stage = (Stage) linkRegister.getScene().getWindow();
            stage.setScene(sceneRegister);
            stage.setResizable(false);
            stage.setTitle("Registro");
        }
    }

    private void showProgressIndicator(){
        if (!vBoxParent.getChildren().contains(progressIndicator)){
            vBoxParent.getChildren().add(progressIndicator);
            progressIndicator.setVisible(true);
        }
    }
}
