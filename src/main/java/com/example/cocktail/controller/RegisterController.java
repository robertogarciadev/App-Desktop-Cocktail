package com.example.cocktail.controller;

import com.example.cocktail.MainApplication;
import com.example.cocktail.dao.UserEntityDao;
import com.example.cocktail.interfaces.ICheckFieldData;
import com.example.cocktail.interfaces.ICheckFieldMail;
import com.example.cocktail.interfaces.ICheckFieldName;
import com.example.cocktail.interfaces.ICheckFieldPassword;
import com.example.cocktail.model.UserEntity;
import com.example.cocktail.request.ApiRequest;
import com.example.cocktail.request.DataProvider;
import com.example.cocktail.task.TaskFindByMail;
import com.example.cocktail.task.TaskRegisterUser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.*;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegisterController implements Initializable,
        EventHandler<ActionEvent>,
        ICheckFieldName,
        ICheckFieldMail,
        ICheckFieldData,
        ICheckFieldPassword {


    @FXML
    public ComboBox<String> comboProvinces;
    @FXML
    public VBox vBoxRegister;
    @FXML
    private Button btnRegister, btnBack;
    @FXML
    private PasswordField passwordOne, passwordTwo;
    @FXML
    private RadioButton radioBtnFemale, radioBtnMale;

    @FXML
    private TextField txtMail, txtName;

    private ObservableList<String> listProvinces = FXCollections.observableArrayList();
    private ToggleGroup toggleGroup = new ToggleGroup();
    private String strPass1, strPass2, origin, gender;
    private UserEntityDao userEntityDao;
    ProgressIndicator progressIndicator;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonBack();
        btnBack.setOnAction(this);
        btnRegister.setOnAction(this);
        userEntityDao = new UserEntityDao();
        progressIndicator = new ProgressIndicator(0); // Inicializa en 0
        progressIndicator.setVisible(false);
        loadComboProvinces();
        initRadioButton();
    }

    /**
     * Customiza la apariencia del botón para volver a la pantalla anterior(Login)
     */
    private void setButtonBack() {
        Image image = new Image(getClass().getResource("/back.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30); // Ajusta el ancho de la imagen
        imageView.setFitHeight(30); // Ajusta la altura de la imagen
        btnBack.setGraphic(imageView); // Establece la imagen en el botón
    }

    /**
     * Setea los valores del combo box y pone uno por defecto
     */
    private void loadComboProvinces(){
        ApiRequest.getNamesProvincesAsync().thenAccept(list->{
            Platform.runLater(()->{
            comboProvinces.setItems(list);
            comboProvinces.setValue(list.get(0));
            });
        });
        comboProvinces.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            this.origin = t1.toString();
        });
    }

    /**
     * Crea grupo con todos los radio button
     */
    private void initRadioButton(){
        toggleGroup.getToggles().addAll(radioBtnMale, radioBtnFemale);
        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();
            this.gender = radioButton.getText();
        });

    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Node node = (Node)actionEvent.getSource();
        // PULSAR VOLVER
        if ( node == btnBack){
            Scene scene = DataProvider.getScene("main-login.fxml");
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Login");

        // PULSAR REGISTRARSE
        } else if (node == btnRegister) {
            boolean nameRight = fieldNameChecked(txtName.getText()); //comprueba nombre
            boolean mailRight = fieldMailChecked(txtMail.getText()); // comprueba email
            boolean genderRight = fieldDataChecked(this.gender); //comprueba género
            boolean originRight = fieldDataChecked(this.origin); // comprueba procedencia
            boolean pass1Right = fieldPassChecked(this.passwordOne.getText()); //comprueba password1
            boolean pass2Right = fieldPassChecked(this.passwordTwo.getText()); //comprueba password2

            //Si todos los campos están correctamente rellenos
            if (nameRight && mailRight && genderRight && originRight && pass1Right && pass2Right) {
                this.strPass1 = passwordOne.getText();
                this.strPass2 = passwordTwo.getText();
                //Si las contraseñas del regstro son iguales
                if (Objects.equals(strPass1, strPass2)) {

                    drawProgressIndicator(); //Pinta el indicator progress
                    // Lanza tarea para comprobar si existe el mail
                    TaskFindByMail taskFindByMail = new TaskFindByMail(userEntityDao, txtMail.getText());
                    //Actualiza UI
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(true);
                        progressIndicator.progressProperty().bind(taskFindByMail.progressProperty());
                    });

                    taskFindByMail.setOnSucceeded(eventCheckMail -> {
                        Optional<UserEntity> userEntity = taskFindByMail.getValue(); //Valor que retorna la tarea
                        // Si ya existe usuario con ese correo ->Lanza error
                        if (userEntity.isPresent()) {
                            Platform.runLater(()->{
                                vBoxRegister.getChildren().remove(progressIndicator); // Eliminar de la UI si no es necesario más
                                Alert alertError = new Alert(
                                        Alert.AlertType.ERROR,
                                        "Ya existe un usuario con ese correo. Elige otro");
                                alertError.showAndWait();
                            });
                        // Si no existe un usuario con el correo, inicia el registro
                        } else {
                            // Pinta el indicator Progress
                            drawProgressIndicator();
                            //Guarda nuevo usuario
                            UserEntity newUserEntity = UserEntity.builder()
                                    .name(txtName.getText())
                                    .mail(txtMail.getText())
                                    .gender(this.gender)
                                    .origin(this.origin)
                                    .password(passwordOne.getText())
                                    .build();
                            // Lanza tarea para guardar usuario en base de datos
                            TaskRegisterUser taskRegisterUser = new TaskRegisterUser(userEntityDao, newUserEntity);

                            // Actualizar UI antes de iniciar la tarea. Platforma.runLater es el hilo principal de JavaFx
                            Platform.runLater(() -> {
                                progressIndicator.setVisible(true);
                                progressIndicator.progressProperty().bind(taskRegisterUser.progressProperty());
                            });

                            // Cuando la tarea termine, ocultar el ProgressIndicator y mostrar el Alert
                            taskRegisterUser.setOnSucceeded(event -> {
                                Platform.runLater(() -> {
                                    vBoxRegister.getChildren().remove(progressIndicator); // Eliminar de la UI si no es necesario más
                                    Alert alertInfo = new Alert(Alert.AlertType.INFORMATION, "Usuario Registrado con éxito");
                                    alertInfo.showAndWait();
                                    //Regresa  al login
                                    Scene sceneLogin = DataProvider.getScene("main-login.fxml");
                                    Stage stage = (Stage) vBoxRegister.getScene().getWindow();
                                    stage.setScene(sceneLogin);
                                    stage.setTitle("login");
                                    stage.show();
                                });
                            });
                            new Thread(taskRegisterUser).start();
                        }
                    });
                    new Thread(taskFindByMail).start();
                } else {
                    Alert alertError = new Alert(
                            Alert.AlertType.ERROR,
                            "Las contraseñas no coinciden. Vuelve a intentarlo");
                    alertError.showAndWait();
                }

            } else {
                Alert alertError = new Alert(
                        Alert.AlertType.ERROR,
                        "Uno o varios campos son incorrectos");
                alertError.showAndWait();
            }


        }
    }

    /**
     * Comprueba si el campo no es nulo
     * @param value
     * @return boolean
     */
    @Override
    public boolean fieldDataChecked(String value) {
        return value != null;
    }

    /**
     * Comprueba que el mail contenga @ y que tenga al menos de 8 caracteres de longitud
     * @param value
     * @return boolean
     */
    @Override
    public boolean fieldMailChecked(String value) {
        return value.contains("@") &&  value.length()>=8;
    }

    /**
     * Comprueba que el nombre tenga al menos tre letras
     * @param value
     * @return boolean
     */
    @Override
    public boolean fieldNameChecked(String value) {
        return value.length()>=3;
    }

    /**
     * Comprueba que la contraseña tenga al menos 8 caracteres
     * @param value
     * @return boolean
     */
    @Override
    public boolean fieldPassChecked(String value) {
        return value.length()>=8;
    }

    private void drawProgressIndicator(){
        // Añade el progressIndicator al view Parent (VBOX)
        if (!vBoxRegister.getChildren().contains(progressIndicator)) {
            vBoxRegister.getChildren().add(progressIndicator);
        }
    }
}
