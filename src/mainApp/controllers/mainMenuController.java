package mainApp.controllers;

import javafx.scene.layout.BorderPane;
import mainApp.main;

import java.io.IOException;

public class mainMenuController
{
    public BorderPane mainBorderPane;

    public void onClickPlayButton() throws IOException
    {
        main.changeScene("choosePlayer");
    }

    public void onClickQuitButton()
    {
        main.close();
    }
}
