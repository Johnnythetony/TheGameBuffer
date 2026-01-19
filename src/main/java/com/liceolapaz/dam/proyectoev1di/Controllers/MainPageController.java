package com.liceolapaz.dam.proyectoev1di.Controllers;

import com.liceolapaz.dam.proyectoev1di.DTO.GameFilterDTO;
import com.liceolapaz.dam.proyectoev1di.DTO.VideogameDTO;
import com.liceolapaz.dam.proyectoev1di.ResourcePaths.Images;
import com.liceolapaz.dam.proyectoev1di.ResourcePaths.Stylesheets;
import com.liceolapaz.dam.proyectoev1di.Services.GamesPlatformsService;
import com.liceolapaz.dam.proyectoev1di.Services.VideogameService;
import com.liceolapaz.dam.proyectoev1di.SessionManager;
import com.liceolapaz.dam.proyectoev1di.Utils.Debounce;
import com.liceolapaz.dam.proyectoev1di.ViewHandler;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController implements Initializable
{
    @FXML
    private VBox genreVB;

    @FXML
    private VBox companyVB;

    @FXML
    private VBox platformVB;

    @FXML
    private FlowPane listajuegosFP;

    @FXML
    private TextField searchbarTF;

    @FXML
    private Label pmaxL;

    @FXML
    private Label pminL;

    @FXML
    private Slider pmaxS;

    @FXML
    private Slider pminS;

    @FXML
    private RadioButton nopriceshowRB;

    @FXML
    private RadioButton nopricenotshowRB;

    @FXML
    private VBox pmaxVB;

    @FXML
    private VBox pminVB;

    @FXML
    private Label resultsL;

    private GameFilterDTO gameFilters;

    private VideogameService vg_service = new VideogameService();

    private GamesPlatformsService gp_service = new GamesPlatformsService();

    private List<VideogameDTO> juegos = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ToggleGroup unpricedToggleGroup = new ToggleGroup();
        nopriceshowRB.setToggleGroup(unpricedToggleGroup);
        nopricenotshowRB.setToggleGroup(unpricedToggleGroup);

        nopriceshowRB.setSelected(true);

        unpricedToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if(newVal == nopriceshowRB)
                {
                    gameFilters.setNoprice(true);
                    pmaxVB.setDisable(false);
                    pminVB.setDisable(false);
                }
                else
                {
                    gameFilters.setNoprice(false);
                    pmaxVB.setDisable(true);
                    pminVB.setDisable(true);
                }
                applyFilters();
            }
        });

        pmaxS.setMax(gp_service.getPriceLimit(true));
        pminS.setMin(gp_service.getPriceLimit(false));

        pminL.textProperty().bind(
                Bindings.createStringBinding(
                        () -> String.valueOf((int) pminS.getValue()),
                        pminS.valueProperty()
                )
        );

        pmaxL.textProperty().bind(
                Bindings.createStringBinding(
                        () -> String.valueOf((int) pmaxS.getValue()),
                        pmaxS.valueProperty()
                )
        );



        pminS.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > pmaxS.getValue()) {
                pminS.setValue(pmaxS.getValue());
            }
        });

        pmaxS.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < pminS.getValue()) {
                pmaxS.setValue(pminS.getValue());
            }
        });

        pminS.setOnMouseReleased(event -> {
            gameFilters.setPreciomin(pminS.getValue());
            gameFilters.setPreciomax(pmaxS.getValue());
            applyFilters();
        });

        pmaxS.setOnMouseReleased(event -> {
            gameFilters.setPreciomin(pminS.getValue());
            gameFilters.setPreciomax(pmaxS.getValue());
            applyFilters();
        });

        pminS.setValue(pminS.getMin());
        pmaxS.setValue(pmaxS.getMax());

        ArrayList<String> genres = vg_service.getGenres();
        ArrayList<String> companies = vg_service.getCompanies();
        ArrayList<String> platforms = vg_service.getPlatforms();

        gameFilters = new GameFilterDTO("", new HashMap<>(), new HashMap<>(), new HashMap<>(), pmaxS.getMax(), pminS.getMin(), true);

        for(String genre : genres)
        {
            CheckBox cb = new CheckBox(genre);
            cb.setOnAction(event -> addGenreCheckBoxFilter(genre));
            genreVB.getChildren().add(cb);
            gameFilters.getSelectedGenres().put(genre, false);
        }

        for(String company : companies)
        {
            CheckBox cb = new CheckBox(company);
            cb.setOnAction(event -> addCompanyCheckBoxFilter(company));
            companyVB.getChildren().add(cb);
            gameFilters.getSelectedCompanies().put(company, false);
        }

        for(String platform : platforms)
        {
            CheckBox cb = new CheckBox(platform);
            cb.setOnAction(event -> addPlatformCheckBoxFilter(platform));
            platformVB.getChildren().add(cb);
            gameFilters.getSelectedPlatforms().put(platform, false);
        }

        Debounce db = new Debounce();
        db.newKeyframe(e -> {
            gameFilters.setSearchText(db.getNewValue());
            applyFilters();
        });

        searchbarTF.textProperty().addListener((observable, oldValue, newValue) -> {
            db.debounceText(newValue);
        });

        applyFilters();
    }

    @FXML
    public void applyFilters()
    {
        listajuegosFP.getChildren().clear();

        juegos = vg_service.getFiteredGames(gameFilters);

        List<Node> game_cards = new ArrayList<>();
        for(VideogameDTO game : juegos)
        {
            game_cards.add(createGameCard(game));
        }
        resultsL.setText("Resultados: "+juegos.size());

        listajuegosFP.getChildren().addAll(game_cards);
    }

    @FXML
    public void addGenreCheckBoxFilter(String filter)
    {
        gameFilters.getSelectedGenres().put(filter, !gameFilters.getSelectedGenres().get(filter));
        applyFilters();
    }

    @FXML
    public void addCompanyCheckBoxFilter(String filter)
    {
        gameFilters.getSelectedCompanies().put(filter, !gameFilters.getSelectedCompanies().get(filter));
        applyFilters();
    }

    @FXML
    public void addPlatformCheckBoxFilter(String filter)
    {
        gameFilters.getSelectedPlatforms().put(filter, !gameFilters.getSelectedPlatforms().get(filter));
        applyFilters();
    }

    private StackPane createGameCard(VideogameDTO game)
    {
        final double IMAGE_WIDTH = 320;
        final double IMAGE_HEIGHT = 180;

        ImageView imageView = new ImageView();
        try
        {
            Image image;
            if(!URI.create(game.getPortada()).getPath().isEmpty())
            {
                image = new Image(game.getPortada(), IMAGE_WIDTH, IMAGE_HEIGHT, true, true);
            }
            else
            {
                image = new Image(Images.LOGIN_ICON.getResource_path(), IMAGE_WIDTH, IMAGE_HEIGHT, true, true);
            }
            imageView.setImage(image);
            imageView.setFitWidth(IMAGE_WIDTH);
            imageView.setFitHeight(IMAGE_HEIGHT);
        } catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setContentText("No se pudo cargar la imagen");
            alert.getDialogPane().getStylesheets().add(ViewHandler.class.getResource(Stylesheets.DEFAULT.getResource_path()).toExternalForm());
            alert.showAndWait();
        }

        Label titleLabel = new Label(game.getTitulo());

        titleLabel.setWrapText(true);

        titleLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 3px 6px;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: bold;");

        StackPane gameCard = new StackPane();
        gameCard.setPrefSize(IMAGE_WIDTH, IMAGE_HEIGHT);

        StackPane.setAlignment(titleLabel, Pos.BOTTOM_CENTER);

        gameCard.getChildren().addAll(imageView, titleLabel);

        gameCard.setPadding(new Insets(5, 5, 5, 5));

        gameCard.setOnMouseEntered(event -> {
            gameCard.setStyle("-fx-background-color: #4a4a4a; -fx-scale: 1.05;");
        });

        gameCard.setOnMouseExited(event -> {
            gameCard.setStyle("-fx-background-color: #2e2e2e; -fx-scale: 1.0;");
        });

        gameCard.setOnMouseEntered(event -> gameCard.getScene().setCursor(Cursor.HAND));
        gameCard.setOnMouseExited(event -> gameCard.getScene().setCursor(Cursor.DEFAULT));

        gameCard.setOnMouseClicked(event -> {
            SessionManager.getInstance().setCurrent_game(game);
            SessionManager.getInstance().getUserMenuController().setItemDetailView();
        });

        return gameCard;
    }
}
