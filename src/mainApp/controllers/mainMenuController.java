package mainApp.controllers;

import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import mainApp.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainMenuController implements Initializable
{
    public BorderPane mainBorderPane;
    public HBox topHBox;

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        main.makeScreenDraggable(topHBox);
    }

    public void onClickPlayButton() throws IOException
    {
        main.changeScene("choosePlayer");
    }

    public void onClickQuitButton()
    {
        main.close();
    }

    /* Closes window and saves */
    public void closeButton()
    {
        main.close();
    }

    /* Maximizes/restores window */
    public void maxRestoreButton()
    {
        main.maxRestoreWindow();
    }

    /* Minimizes window */
    public void minimizeButton()
    {
        main.window.setIconified(true);
    }


}
