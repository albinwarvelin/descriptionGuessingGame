package mainApp.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import mainApp.application.User;
import mainApp.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class choosePlayerController implements Initializable
{
    public AnchorPane mainAnchorPane;
    public ChoiceBox<String> userChoiceBox;
    public Button userChoiceButton;
    public Label userChoiceErrorLabel;
    public HBox topHBox;

    private static HashMap<String, User> userList = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            userList = loadUsers();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        fillChoiceBox(userList);

        main.makeScreenDraggable(topHBox);
    }

    /* Loads all saved users from csv file */
    private HashMap<String, User> loadUsers() throws IOException
    {
        HashMap<String, User> userList = new HashMap<>();

        BufferedReader csvReader = new BufferedReader(new FileReader("src/assets/localUserData.csv"));
        String csvRow;

        while ((csvRow = csvReader.readLine()) != null)
        {
            String[] csvSplitted = csvRow.split(",");
            User newUser = new User(csvSplitted[0], csvSplitted[1], Integer.valueOf(csvSplitted[2]), Integer.valueOf(csvSplitted[3]));

            userList.put((csvSplitted[0] + " " + csvSplitted[1]), newUser);
        }

        return userList;
    }

    /* Fills dropdown menu (choiceBox) with users from csv file, if there are no users button and choicebox is disabled */
    private void fillChoiceBox(HashMap<String, User> inputUserList)
    {
        ArrayList<String> nameList = new ArrayList<>(inputUserList.keySet());

        if (nameList.size() != 0)
        {
            userChoiceBox.getItems().addAll(nameList);
        }
        else
        {
            userChoiceBox.setDisable(true);
            userChoiceButton.setDisable(true);
        }
    }

    /* Called gets user choice from ChoiceBox and switches to game scene */
    public void onChoosePlayerClick()
    {
        String userString = String.valueOf(userChoiceBox.getValue());
        if (!userString.equals("null"))
        {
            User currentUser = userList.get(userString);
            gameMainController.setCurrentUser(currentUser);

            userChoiceErrorLabel.setDisable(true);
            main.setUserDataLoaded();
            main.changeScene( "gameMain");
        }
        else
        {
            userChoiceErrorLabel.setDisable(false);
        }
    }

    public static HashMap<String, User> getUserList()
    {
        return userList;
    }

    /* Closes window and saves */
    public void closeButton()
    {
        main.close();
    }

    /* Maximizes/restores window */
    public void maxRestoreButton()
    {
        main.window.setMaximized(!main.window.isMaximized());
    }

    /* Minimizes window */
    public void minimizeButton()
    {
        main.window.setIconified(true);
    }

    /* Goes to previous window */
    public void backButton()
    {
        main.changeScene("mainMenu");
    }
}
