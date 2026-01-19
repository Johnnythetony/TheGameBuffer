package com.liceolapaz.dam.proyectoev1di.Controllers;

import com.liceolapaz.dam.proyectoev1di.DTO.UserDTO;
import com.liceolapaz.dam.proyectoev1di.ResourcePaths.Stylesheets;
import com.liceolapaz.dam.proyectoev1di.ResourcePaths.Views;
import com.liceolapaz.dam.proyectoev1di.Services.UserService;
import com.liceolapaz.dam.proyectoev1di.SessionManager;
import com.liceolapaz.dam.proyectoev1di.Utils.Debounce;
import com.liceolapaz.dam.proyectoev1di.ViewHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminUserController implements Initializable
{
    @FXML
    private TableView<UserDTO> userTV;

    @FXML
    private TextField searchbarTF;

    @FXML
    private TableColumn<UserDTO, String> usernameTC;

    @FXML
    private TableColumn<UserDTO, String> mailTC;

    @FXML
    private TableColumn<UserDTO, Void> modifyTC;

    @FXML
    private TableColumn<UserDTO, Void> deleteTC;

    public ObservableList<UserDTO> user_obs;

    private UserService u_service = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        SessionManager.getInstance().setAdmin_user_list(user_obs);

        Debounce db = new Debounce();
        db.newKeyframe(e -> {
            filterUsers(db.getNewValue());
        });

        searchbarTF.textProperty().addListener((observable, oldValue, newValue) -> {
            db.debounceText(newValue);
        });

        filterUsers("");
        userTV.setItems(user_obs);

        usernameTC.setCellValueFactory(new PropertyValueFactory<>("username"));
        mailTC.setCellValueFactory(new PropertyValueFactory<>("mail"));
        modifyTC.setCellFactory(column ->
        {
            Button editButton = new Button("Editar");
            TableCell<UserDTO, Void> cell = new TableCell<UserDTO, Void>() {
                @Override
                public void updateItem(Void user, boolean empty) {
                    super.updateItem(user, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            };

            editButton.setStyle("-fx-alignment: center");
            editButton.setOnAction(e -> goToModifyUser(cell.getTableRow().getItem()));

            return cell;
        });

        deleteTC.setCellFactory(column ->
        {
            Button delete_button = new Button("Eliminar");
            TableCell<UserDTO, Void> cell = new TableCell<UserDTO, Void>() {
                @Override
                public void updateItem(Void user, boolean empty) {
                    super.updateItem(user, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(delete_button);
                    }
                }
            };

            delete_button.setStyle("-fx-alignment: center");
            delete_button.setOnAction(e -> deleteUser(cell.getTableRow().getItem()));

            return cell;
        });
    }

    private void filterUsers(String newValue)
    {
        user_obs = FXCollections.observableList(u_service.retrieveUsers(newValue));
    }

    @FXML
    public void goToCreateUser()
    {
        ViewHandler.modalStage(Views.CREATEUSER.getFXML());
    }

    @FXML
    public void goToModifyUser(UserDTO user)
    {
        SessionManager.getInstance().setUser_mod(user);
        ViewHandler.modalStage(Views.MODIFYUSER.getFXML());
    }

    @FXML
    public void deleteUser(UserDTO user)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setTitle("Eliminar usuario");
        alert.setContentText("Está seguro de querer eliminar al usuario?");
        alert.getDialogPane().getStylesheets().add(ViewHandler.class.getResource(Stylesheets.DEFAULT.getResource_path()).toExternalForm());

        alert.showAndWait();

        if(alert.getResult() == ButtonType.OK)
        {
            u_service.deleteUser(user);
            user_obs.remove(user);

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.initStyle(StageStyle.TRANSPARENT);
            alert2.setTitle("Usuario eliminado");
            alert2.setContentText("El usuario ha sido eliminado correctamente");
            alert2.getDialogPane().getStylesheets().add(ViewHandler.class.getResource(Stylesheets.DEFAULT.getResource_path()).toExternalForm());

            alert2.showAndWait();
        }
    }
}