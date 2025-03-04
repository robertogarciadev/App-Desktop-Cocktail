package com.example.cocktail.controller;

import com.example.cocktail.dao.CocktailEntityDao;
import com.example.cocktail.dao.UserEntityDao;
import com.example.cocktail.model.CocktailEntity;
import com.example.cocktail.model.UserEntity;
import com.example.cocktail.model.eumerates.TypeFilter;
import com.example.cocktail.request.ApiRequest;
import com.example.cocktail.task.TaskFindByMail;
import com.example.cocktail.task.TaskUpdateListCocktails;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainCocktailController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    public ComboBox<TypeFilter> comboFilter;
    @FXML
    public ListView<String> listViewItemsFilter;
    @FXML
    public Label labelInitSession;
    @FXML
    public TableView<CocktailEntity> tableViewDrink;
    @FXML
    public TableColumn<CocktailEntity, String> columIdDrink;
    @FXML
    public TableColumn<CocktailEntity, String> columnNameDrink;
    @FXML
    public TextField txtFieldName;
    @FXML
    public TextField txtFieldId;
    @FXML
    public ImageView imgCocktail;
    @FXML
    public Button btnAddFavorite;
    @FXML
    public Button btnConsultFavorites;
    @FXML
    public Button btnDeleteFavorite;

    private ObservableList<CocktailEntity> observableListDrinks = FXCollections.observableArrayList();
    private ObservableList<CocktailEntity> listFavoriteCocktails = FXCollections.observableArrayList();
    private CocktailEntity cocktailEntity;
    private UserEntity userEntity;
    private UserEntityDao userEntityDao;
    private CocktailEntityDao cocktailEntityDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBoxFilter();
        loadListViewAllCategories();
        initListView();
        initTableView();
        cocktailEntity = CocktailEntity.builder().build();
        userEntityDao = new UserEntityDao();
        cocktailEntityDao = new CocktailEntityDao();
        btnAddFavorite.setOnAction(this);
        btnConsultFavorites.setOnAction(this);
        btnDeleteFavorite.setOnAction(this);
    }


    /**
     * Setea la vista de cada cocktail que se va pinchando en la tabla
     */
    private void setViewDrinkEntity() {
        txtFieldName.setText(this.cocktailEntity.getStrDrink());
        txtFieldId.setText(this.cocktailEntity.getIdDrink());
        Image image = new Image(this.cocktailEntity.getStrDrinkThumb());
        ImageView imageView = new ImageView(image);
        imgCocktail.setImage(imageView.getImage());
    }

    /**
     * Setea valores a  comboBox
     */
    private void initComboBoxFilter() {
        ObservableList<TypeFilter> observableListFilters = FXCollections
                .observableArrayList(List.of(TypeFilter.values()));
        comboFilter.setItems(observableListFilters); //Setea al combo box los valores (De tipo Enum)
        comboFilter.setValue(observableListFilters.get(0)); //Deja selecionado uno por defecto

        //Escuchador de cambio al combo Box
        comboFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            switch (t1) {
                case CATEGORIA -> loadListViewAllCategories(); //Muestra en el listView los nombres de todas las categorías
                case TIPO_DE_VASO -> loadListViewAllTypesGlasses(); //Muestra en el listViw los nombres de todas los tipos de vasos
                case INGREDIENTES -> loadListViewAllIngredients(); // Muestra en el listView los nombre de todos los ingredientes
                case GRADUACIÓN -> loadListViewAllAlcoholicGraduation(); //Muestra en el listView los nombres de todas las graduaciones
            }
        });
    }


    private void initListView() {
        // Pone un escuchador de cambio al listView
        listViewItemsFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                String newValueCustom = newValue.replace(" ", "_"); //Retorna el objeto seleccionado
                switch (comboFilter.getValue()) {
                    case CATEGORIA -> loadAllDrinksByCategory(newValueCustom); //Muestra en la tabla todas las bebidas por categoria
                    case TIPO_DE_VASO -> loadAllDrinksByTypeGlass(newValueCustom); //Muestra en la tabla todas las bebidas por tipo de vaso
                    case INGREDIENTES -> loadAllDrinkByIngredient(newValueCustom);//Muestra en la tabla todas las bebidas por ingrediente
                    case GRADUACIÓN -> loadAllDrinkByGraduation(newValueCustom); //Muestra en la tabla todas las bebidas por graduación
                }
            }
        });
    }

    private void initTableView() {
        tableViewDrink.setItems(observableListDrinks); //Setea en la tabla la lista de objetos a mostrar
        columIdDrink.setCellValueFactory(new PropertyValueFactory<>("idDrink")); //Setea valor en columna
        columnNameDrink.setCellValueFactory(new PropertyValueFactory<>("strDrink")); //Setea Valor en columna
        tableViewDrink.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                this.cocktailEntity.setStrDrink(newValue.getStrDrink());
                this.cocktailEntity.setStrDrinkThumb(newValue.getStrDrinkThumb());
                this.cocktailEntity.setIdDrink(newValue.getIdDrink());
                setViewDrinkEntity();
            }
        });
    }

    /**
     * Trae en un peteción asincrona todos los nombre de las categorías.
     * Actualiza listView en hilo principal
     */
    private void loadListViewAllCategories() {
        ApiRequest.getAllNamesCategoriesCocktailsAsync().thenAccept(list -> {
            Platform.runLater(() -> {
                listViewItemsFilter.setItems(list);
            });
        });
    }

    /**
     * Trae en un peteción asincrona todos los nombre de los tipos de vasos
     * Actualiza listView en hilo principal
     */
    private void loadListViewAllTypesGlasses() {
        ApiRequest.getAllNamesTypesGlassCocktailsAsync().thenAccept(listGlasses -> {
            Platform.runLater(() -> {
                listViewItemsFilter.setItems(listGlasses);
            });
        });
    }

    /**
     * Trae en un peteción asincrona todos los nombre de los ingredientes.
     * Actualiza listView en hilo principal
     */
    private void loadListViewAllIngredients() {
        ApiRequest.getAllNamesIngredientsCocktails().thenAccept(listIngredients -> {
            Platform.runLater(() -> {
                listViewItemsFilter.setItems(listIngredients);
            });
        });
    }

    /**
     * Trae en un peteción asincrona todos los nombre de los tipos de graduación.
     * Actualiza listView en hilo principal
     */
    private void loadListViewAllAlcoholicGraduation() {
        ApiRequest.getAllNamesAlcoholicGraduation().thenAccept(lisAlcoholicGraduation -> {
            Platform.runLater(() -> {
                listViewItemsFilter.setItems(lisAlcoholicGraduation);
            });
        });
    }

    /**
     * Trae en un peteción asincrona todos las DrinkEntity por categoría.
     * Actualiza list y tabla en hilo principal
     */
    private void loadAllDrinksByCategory(String category) {
        ApiRequest.getDrinkEntityByCategoryAsync(category).thenAccept(list -> {
            Platform.runLater(() -> {
                observableListDrinks.setAll(list);
                tableViewDrink.setItems(observableListDrinks);
                tableViewDrink.refresh();
            });
        });
    }

    /**
     * Trae en un peteción asincrona todos las DrinkEntity por tipo de vaso.
     * Actualiza list y tabla en hilo principal
     */
    private void loadAllDrinksByTypeGlass(String typeGlass) {
        ApiRequest.getDrinkEntityByTypeGlassAsync(typeGlass).thenAccept(list -> {
            Platform.runLater(() -> {
                observableListDrinks.setAll(list);
                tableViewDrink.setItems(observableListDrinks);
                tableViewDrink.refresh();
            });
        });
    }

    /**
     * Trae en un peteción asincrona todos las DrinkEntity por ingrediente.
     * Actualiza list y tabla en hilo principal
     */
    private void loadAllDrinkByIngredient(String ingredient) {
        ApiRequest.getDrinkEntityByIngredients(ingredient).thenAccept(list -> {
            Platform.runLater(() -> {
                observableListDrinks.setAll(list);
                tableViewDrink.setItems(observableListDrinks);
                tableViewDrink.refresh();
            });
        });
    }

    /**
     * Trae en un peteción asincrona todos las DrinkEntity por graduación.
     * Actualiza list y tabla en hilo principal
     */
    private void loadAllDrinkByGraduation(String graduation) {
        ApiRequest.getDrinkEntityByAlcoholicGraduation(graduation).thenAccept(list -> {
            Platform.runLater(() -> {
                observableListDrinks.setAll(list);
                tableViewDrink.setItems(observableListDrinks);
                tableViewDrink.refresh();
            });
        });
    }

    /**
     * Recupera Usuario del Login
     *
     * @param userEntity
     */
    public void getUser(UserEntity userEntity) {
        this.userEntity = userEntity;
        labelInitSession.setText(String.format("Iniciada sesión con usuario: %s", userEntity.getName()));
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();

        //PULSAR GUARDAR COCKTAIL
        if (node == btnAddFavorite) {

            //Comprueba si el Usuario ya tiene el cocktail en favoritos
            boolean existCocktail =
                    userEntityDao.existCocktail(this.userEntity.getId(), this.cocktailEntity.getIdDrink());
            //Si el usuario ya tiene el cocktail en favoritos
            if (existCocktail){
                Platform.runLater(()->{
                    Stage stage = (Stage) btnAddFavorite.getScene().getWindow();
                    showAlertWarningExistCocktail(stage);
                });
            // Si no lo tiene,  lo guarda en
            }else {
                //Lanza hilo para guardar la bebida
                TaskUpdateListCocktails taskUpdateListCocktails = new TaskUpdateListCocktails(
                        this.cocktailEntity,
                        this.cocktailEntityDao,
                        this.userEntity,
                        this.userEntityDao
                );
                //Cuando se complete la tare mostrará mensaje
                taskUpdateListCocktails.setOnSucceeded(event -> {
                    Platform.runLater(() -> {
                        Stage stage = (Stage) btnAddFavorite.getScene().getWindow();
                        showAlertSuccessAddCocktail(stage);
                    });
                });

                new Thread(taskUpdateListCocktails).start(); //lanza hilo con la tarea
            }


        // AL PULSAR FAVORITOS. SE ACTUALIZA LA TALA CON LSO FAVORITOS DEL USUARIO
        }else if (node == btnConsultFavorites) {

            TaskFindByMail taskFindByMail = new TaskFindByMail(this.userEntityDao, this.userEntity.getMail());
            taskFindByMail.setOnSucceeded(event -> {
            Optional<UserEntity> userEntity1 = taskFindByMail.getValue();//Obtiene usuario de la base de datos
                if (userEntity1.isPresent()){
                    listFavoriteCocktails.setAll(userEntity1.get().getCocktailsList()); //Coge los cocteles del usuario
                    tableViewDrink.setItems(listFavoriteCocktails); //Actualiza la tabla
                    tableViewDrink.refresh();
                }
            });
            new Thread(taskFindByMail).start();

        //BORRA COCKTAIL DE LOS FOVARITOS
        } else if (node==btnDeleteFavorite) {
            //Comprobar si el coctail a borrar existe
            Stage stage = (Stage) btnDeleteFavorite.getScene().getWindow();
            boolean existCocktail =
                    userEntityDao.existCocktail(this.userEntity.getId(), this.cocktailEntity.getIdDrink());
            if (existCocktail){
                userEntityDao.deleteCocktailToFavorites(this.userEntity.getId(), this.cocktailEntity.getIdDrink());
                TaskFindByMail taskFindByMail = new TaskFindByMail(this.userEntityDao, this.userEntity.getMail());
                taskFindByMail.setOnSucceeded(event -> {
                    Optional<UserEntity> userEntity1 = taskFindByMail.getValue();//Obtiene usuario de la base de datos
                    if (userEntity1.isPresent()){
                        listFavoriteCocktails.setAll(userEntity1.get().getCocktailsList()); //Coge los cocteles del usuario
                        tableViewDrink.setItems(listFavoriteCocktails); //Actualiza la tabla
                        tableViewDrink.refresh();
                        showAlertInfoCocktailDelete(stage);
                    }

                });
                new Thread(taskFindByMail).start();
            }else {
                showAlertWarningNotExistInFavorites(stage);

            }
        }
    }


    /**
     * Muestra aler sobre la ventana principal.
     * @param owner
     */
    private void showAlertSuccessAddCocktail(Window owner) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cocktail guardado en favoritos");
            alert.initOwner(owner);  // Mantiene el alert asociado a la ventana principal
            alert.initModality(Modality.NONE); // No bloquea la interacción
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setAlwaysOnTop(true); // Mantiene el Alert sobre todas las ventanas
            alert.show();
        });
    }

    private void showAlertWarningExistCocktail(Window owner) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, "El cocktail ya está en favoritos");
            alert.initOwner(owner);  // Mantiene el alert asociado a la ventana principal
            alert.initModality(Modality.NONE); // No bloquea la interacción
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setAlwaysOnTop(true); // Mantiene el Alert sobre todas las ventanas
            alert.show();
        });
    }

    private void showAlertWarningNotExistInFavorites(Window owner) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, "El cocktail no está en favoritos");
            alert.initOwner(owner);  // Mantiene el alert asociado a la ventana principal
            alert.initModality(Modality.NONE); // No bloquea la interacción
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setAlwaysOnTop(true); // Mantiene el Alert sobre todas las ventanas
            alert.show();
        });
    }

    private void showAlertInfoCocktailDelete(Window owner) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "El cocktail se ha borrado de favoritos");
            alert.initOwner(owner);  // Mantiene el alert asociado a la ventana principal
            alert.initModality(Modality.NONE); // No bloquea la interacción
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setAlwaysOnTop(true); // Mantiene el Alert sobre todas las ventanas
            alert.show();
        });
    }






}