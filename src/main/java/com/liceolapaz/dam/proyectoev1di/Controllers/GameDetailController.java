package com.liceolapaz.dam.proyectoev1di.Controllers;

import com.liceolapaz.dam.proyectoev1di.Entities.Backlog;
import com.liceolapaz.dam.proyectoev1di.Entities.GamesPlatforms;
import com.liceolapaz.dam.proyectoev1di.Entities.Videogame;
import com.liceolapaz.dam.proyectoev1di.ResourcePaths.Views;
import com.liceolapaz.dam.proyectoev1di.Services.VideogameService;
import com.liceolapaz.dam.proyectoev1di.SessionManager;
import com.liceolapaz.dam.proyectoev1di.ViewHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameDetailController implements Initializable
{
    @FXML
    private Label titleL;

    @FXML
    private Label genreL;

    @FXML
    private Label companyL;

    @FXML
    private Label categoryL;

    @FXML
    private ImageView portadaIV;

    @FXML
    private Button addtobacklogB;

    @FXML
    private FlowPane platformpriceFP;

    @FXML
    private VBox gamereviewsVB;

    @FXML
    private Label resenhasL;

    @FXML
    private Label mediaL;

    private VideogameService vg_service = new VideogameService();

    private int num_resenhas = 0;
    private int num_ratings = 0;
    private float sum_ratings = 0f;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Videogame juego = vg_service.getVideogameDetails(SessionManager.getInstance().getCurrent_game().getTitulo());

        titleL.setText(juego.getTitulo());
        genreL.setText("Género: "+juego.getGenero());
        portadaIV.setImage(new Image(juego.getPortada()));
        categoryL.setText("Categoría: "+juego.getCategory().toString());
        companyL.setText("Desarrolladora: "+juego.getCompanhia().getNombre());

        platformpriceFP.getChildren().clear();
        for(GamesPlatforms gp : juego.getGamesPlatforms())
        {
            Label pprice = new Label(gp.getPlataforma().getNombre()+"  |  "+gp.getPrecio_juego().toString()+"€");
            pprice.setStyle("-fx-border-width: 2px; -fx-border-radius: 50; -fx-padding: 4; -fx-border-color: black");
            platformpriceFP.getChildren().add(pprice);
        }

        for(Backlog b: juego.getBacklog())
        {
            if (b.getValoracion() != null)
            {
                num_ratings++;
                sum_ratings += b.getValoracion();
            }
            if (b.getResenha() != null)
            {
                try
                {
                    FXMLLoader loader = new FXMLLoader(ViewHandler.class.getResource(Views.REVIEWCONTAINER.getFXML()));

                    Node review_container = loader.load();

                    ReviewContainerController review_controller = loader.getController();

                    review_controller.setReviewData(b);

                    gamereviewsVB.getChildren().add(review_container);

                    num_resenhas++;
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

        if (num_ratings == 0) num_ratings++;
        mediaL.setText("Valoración media: "+String.format("%.2f",(sum_ratings/num_ratings))+"/10");
        resenhasL.setText("Reseñas totales: "+num_resenhas);
    }
}
