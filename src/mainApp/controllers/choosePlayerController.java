package mainApp.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import mainApp.application.User;
import mainApp.application.toolbox;
import mainApp.main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
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
    public Label userChoiceErrorLabel, chooseAvatarErrorLabel, userNameErrorLabel, usernameDisplayLabel, nameDisplayLabel;
    public HBox topHBox;

    public ImageView av1_ImageView, av2_ImageView, av3_ImageView, av4_ImageView, av5_ImageView, av6_ImageView, av7_ImageView, av8_ImageView,
            av9_ImageView, av10_ImageView, av11_ImageView, av12_ImageView, av13_ImageView, av14_ImageView, av15_ImageView, avatarPreviewImageview;

    public TextField usernameTextField, firstnameTextField, lastnameTextField;

    private Image[] av_Image = new Image[15];
    private Image[] av_Selected_Image = new Image[15];
    private ImageView[] av_ImageView = new ImageView[15];

    private static HashMap<String, User> userList = new HashMap<>();
    private int avatarID = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        fill_Av_ImageView(); //Fills avatar list for easier access
        fill_Av_Image();
        fill_Av_Selected_Image();

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
            User newUser = new User(csvSplitted[0], csvSplitted[1], csvSplitted[2], Integer.valueOf(csvSplitted[3]), Integer.valueOf(csvSplitted[4]), Integer.valueOf(csvSplitted[5]));

            if (!(csvSplitted[1].equals("") && csvSplitted[2].equals("")))
            {
                userList.put((csvSplitted[1] + " " + csvSplitted[2]), newUser);
            }
            else
            {
                userList.put(csvSplitted[0], newUser);
            }
        }

        return userList;
    }

    /* Fills dropdown menu (choiceBox) with users from csv file, if there are no users button and choicebox is disabled */
    private void fillChoiceBox(HashMap<String, User> inputUserList)
    {
        userChoiceBox.getItems().clear();

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
            main.changeScene("gameMain");
        }
        else
        {
            userChoiceErrorLabel.setDisable(false);
        }
    }

    /* Called when avatar is selected, changes selected avatar and */
    private void avatarSelect(Integer id)
    {
        if(avatarID != -1)
        {
            av_ImageView[avatarID].setImage(av_Image[avatarID]);
        }

        avatarID = id;

        av_ImageView[avatarID].setImage(av_Selected_Image[avatarID]);

        chooseAvatarErrorLabel.setDisable(true);

        updateCreatePreview();
    }

    /* Called when button is pressed */
    public void onCreatePlayerClick()
    {
        if (checkCriteria())
        {
            User newUser = new User(usernameTextField.getText(),firstnameTextField.getText().trim(), lastnameTextField.getText().trim(), 0, 0, avatarID);
            if (!newUser.getName().equals(" "))
            {
                userList.put(newUser.getName(), newUser);
            }
            else
            {
                userList.put(usernameTextField.getText(), newUser);
            }

            gameMainController.setCurrentUser(newUser);
            gameMainController.addUser(newUser);
            main.setUserDataLoaded();
            chooseAvatarErrorLabel.setDisable(true);
            userNameErrorLabel.setDisable(true);

            fillChoiceBox(userList);

            main.changeScene("gameMain");
        }
    }

    /* Checks if criterias are met, called when any variable part of "create player" changes */
    private boolean checkCriteria()
    {
        boolean criteriaMet;

        if ((usernameTextField.getText().equals("")) && (avatarID == -1))
        {
            criteriaMet = false;

            userNameErrorLabel.setText("No Username Chosen!");
            userNameErrorLabel.setDisable(false);

            chooseAvatarErrorLabel.setText("No avatar chosen!");
            chooseAvatarErrorLabel.setDisable(false);
        }
        else if ((usernameTextField.getText().length() > 14) && (avatarID == -1))
        {
            criteriaMet = false;

            userNameErrorLabel.setText("Username cannot be longer than 14 characters!");
            userNameErrorLabel.setDisable(false);

            chooseAvatarErrorLabel.setText("No avatar chosen!");
            chooseAvatarErrorLabel.setDisable(false);
        }
        else if (usernameTextField.getText().length() > 14)
        {
            criteriaMet = false;

            userNameErrorLabel.setText("Username cannot be longer than 14 characters!");
            userNameErrorLabel.setDisable(false);

            chooseAvatarErrorLabel.setDisable(true);
        }
        else if (usernameTextField.getText().equals(""))
        {
            criteriaMet = false;

            userNameErrorLabel.setText("No Username Chosen!");
            userNameErrorLabel.setDisable(false);

            chooseAvatarErrorLabel.setDisable(true);
        }
        else if (avatarID == -1)
        {
            criteriaMet = false;

            chooseAvatarErrorLabel.setText("No avatar chosen!");
            chooseAvatarErrorLabel.setDisable(false);

            userNameErrorLabel.setDisable(true);
        }
        else
        {
            criteriaMet = true;
            chooseAvatarErrorLabel.setDisable(true);
            userNameErrorLabel.setDisable(true);
            userChoiceErrorLabel.setDisable(true);
        }

        return criteriaMet;
    }

    /* Updates preview */
    public void updateCreatePreview()
    {
        avatarPreviewImageview.setImage(av_Image[avatarID]);

        usernameDisplayLabel.setText(usernameTextField.getText());
        nameDisplayLabel.setText(firstnameTextField.getText().trim() + " " + lastnameTextField.getText().trim());
    }


    /* Fills lists for later use */
    private void fill_Av_ImageView()
    {
        av_ImageView[0] = av1_ImageView;
        av_ImageView[1] = av2_ImageView;
        av_ImageView[2] = av3_ImageView;
        av_ImageView[3] = av4_ImageView;
        av_ImageView[4] = av5_ImageView;
        av_ImageView[5] = av6_ImageView;
        av_ImageView[6] = av7_ImageView;
        av_ImageView[7] = av8_ImageView;
        av_ImageView[8] = av9_ImageView;
        av_ImageView[9] = av10_ImageView;
        av_ImageView[10] = av11_ImageView;
        av_ImageView[11] = av12_ImageView;
        av_ImageView[12] = av13_ImageView;
        av_ImageView[13] = av14_ImageView;
        av_ImageView[14] = av15_ImageView;
    }
    private void fill_Av_Image()
    {
        for (int i = 0; i < 15; i++)
        {
            File file = new File(toolbox.getCurrentDirectory() + "\\assetFiles\\avatarIcons\\avatar" + i + ".png");
            av_Image[i] = new Image(file.toURI().toString());
        }
    }
    private void fill_Av_Selected_Image()
    {
        for (int i = 0; i < 15; i++)
        {
            File file = new File(toolbox.getCurrentDirectory() + "\\assetFiles\\avatarIcons\\avatar" + i + "_Selected.png");
            av_Selected_Image[i] = new Image(file.toURI().toString());
        }
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

    /* Returns userlist to caller, used to transfer userlists*/
    public static HashMap<String, User> getUserList()
    {
        return userList;
    }

    /* Handles avatar buttons */
    public void avatarSelect_1()
    {
        avatarSelect(0);
    }
    public void avatarSelect_2()
    {
        avatarSelect(1);
    }
    public void avatarSelect_3()
    {
        avatarSelect(2);
    }
    public void avatarSelect_4()
    {
        avatarSelect(3);
    }
    public void avatarSelect_5()
    {
        avatarSelect(4);
    }
    public void avatarSelect_6()
    {
        avatarSelect(5);
    }
    public void avatarSelect_7()
    {
        avatarSelect(6);
    }
    public void avatarSelect_8()
    {
        avatarSelect(7);
    }
    public void avatarSelect_9()
    {
        avatarSelect(8);
    }
    public void avatarSelect_10()
    {
        avatarSelect(9);
    }
    public void avatarSelect_11()
    {
        avatarSelect(10);
    }
    public void avatarSelect_12()
    {
        avatarSelect(11);
    }
    public void avatarSelect_13()
    {
        avatarSelect(12);
    }
    public void avatarSelect_14()
    {
        avatarSelect(13);
    }
    public void avatarSelect_15()
    {
        avatarSelect(14);
    }
}
