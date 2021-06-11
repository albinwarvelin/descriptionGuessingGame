package mainApp.controllers;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mainApp.application.User;
import mainApp.application.toolbox;
import mainApp.main;

import java.io.*;
import java.net.URL;
import java.util.*;

public class gameMainController implements Initializable
{
    public BorderPane mainBorderPane;
    public ImageView mainControlImage;
    public StackPane controlImageStackPane;
    public HBox topHBox;
    public VBox highscoreNameVBox, highscoreScoreVBox;

    public Button button1 , button2, button3, button4, button5, tryAgainButton, startGameButton;

    public ProgressBar countdownBar;

    public Label scoreLabel, confirmationLabel, countdownLabel;

    public ListView<String> highScoreNameListView;
    public ListView<Integer> highScoreScoreListView;

    /* Description image variables */
    private HashMap<Integer, String> imageDescriptions;
    private Image[] controlImageList;
    private int imageAmount = 0;

    /* Choice variables */
    private String correctControlText;
    private int score = 0;
    private boolean blockKeyInput = true;
    private boolean choiceKeyIsPressed = false;

    /* User variables */
    private static HashMap<String, User> userList;
    private static User currentUser;

    /* Countdown timer variables */
    private static final int STARTTIME = 1500;
    private Timeline timeline;
    private Integer timeTicks;
    private Integer timeSeconds;


    /* Sets up scene when first run, sets highscore lists and button options. Also initiates key listener for button choices */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try                                                                     //Catches exception for FileReader when file isn't found
        {
            imageDescriptions = readImageDescription();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        controlImageList = loadImageList();

        setNewGameContent(imageDescriptions, imageRandomizer(controlImageList));
        setHighScores();

        mainBorderPane.setOnKeyPressed(keyEvent -> //Enables keypress to choose answer
        {
            if(!blockKeyInput)
            {
                switch (keyEvent.getCode())
                {
                    case DIGIT1 -> choiceButtonPressed(1);
                    case DIGIT2 -> choiceButtonPressed(2);
                    case DIGIT3 -> choiceButtonPressed(3);
                    case DIGIT4 -> choiceButtonPressed(4);
                    case DIGIT5 -> choiceButtonPressed(5);
                }
            }
            choiceKeyIsPressed = true;
        });
        mainBorderPane.setOnKeyReleased(keyEvent ->
        {
            choiceKeyIsPressed = false;
        });

        setGameDisable(true);

        main.makeScreenDraggable(topHBox);
    }

    /* Returns list of javafx Images of control description icons, called on initialize */
    private javafx.scene.image.Image[] loadImageList()
    {
        javafx.scene.image.Image[] controlImageList = new javafx.scene.image.Image[134];

        for (int i = 1; i < 135; i++)
        {
            File file = new File(toolbox.getCurrentDirectory() + "\\assetFiles\\descriptionImages\\" + i + ".jpg");
            controlImageList[i - 1] = new javafx.scene.image.Image(file.toURI().toString());
            imageAmount++;
        }
        return controlImageList;
    }

    /* Returns HashMap of image descriptions with Integer as key, Integer is the same as id of Images, , called on initialize */
    private HashMap<Integer, String> readImageDescription() throws IOException
    {
        HashMap<Integer, String> imageDescriptionHM = new HashMap<>();
        String csvRow;

        BufferedReader csvReader = new BufferedReader(new FileReader(toolbox.getCurrentDirectory() + "\\assetFiles\\register_Controls.csv"));

        while ((csvRow = csvReader.readLine()) != null)
        {
            String[] data = csvRow.split(",");
            imageDescriptionHM.put(Integer.valueOf(data[0]), data[1]);
        }

        return imageDescriptionHM;
    }

    private void setGameDisable(boolean trueOrFalse)
    {
        controlImageStackPane.setDisable(trueOrFalse);
        if (trueOrFalse)
        {
            mainControlImage.setOpacity(0);
        }
        else
        {
            mainControlImage.setOpacity(1);
        }

        button1.setDisable(trueOrFalse);
        button2.setDisable(trueOrFalse);
        button3.setDisable(trueOrFalse);
        button4.setDisable(trueOrFalse);
        button5.setDisable(trueOrFalse);

        confirmationLabel.setDisable(trueOrFalse);

        highscoreNameVBox.setDisable(trueOrFalse);
        highscoreScoreVBox.setDisable(trueOrFalse);
    }

    public void startGameButtonPressed()
    {
        setGameDisable(false);
        setNewGameContent(imageDescriptions, imageRandomizer(controlImageList));
        setHighScores();
        countdown();
        blockKeyInput = false;

        startGameButton.setDisable(true);
        button1.requestFocus();
    }

    /* Returns random image from inputList */
    private javafx.scene.image.Image imageRandomizer(javafx.scene.image.Image[] inputList)
    {
        return inputList[(int) (Math.random() * inputList.length)];
    }

    /* Sets new game content */
    private void setNewGameContent(HashMap<Integer, String> inputImageDescription, Image image)
    {
        String imageIDString = image.getUrl();
        String[] randomControlTexts = new String[4];

        ArrayList<Button> buttonList = new ArrayList<>(Arrays.asList(button1, button2, button3, button4, button5));

        int imageID = Integer.valueOf(imageIDString.substring(imageIDString.lastIndexOf('/') + 1, imageIDString.lastIndexOf('.')));
        correctControlText = inputImageDescription.get(imageID);

        for (int i = 0; i < 4; i++)
        {
            boolean setText = true;

            int randomID = (int) ((Math.random() * imageAmount) + 1);

            if (randomID == imageID)
            {
                i--;
                continue;
            }
            for (int a = 0; a < i; a++)
            {
                if (randomControlTexts[a].equals(inputImageDescription.get(randomID)))
                {
                    i--;
                    setText = false;
                }
            }
            if(setText)
            {
                randomControlTexts[i] = inputImageDescription.get(randomID);
            }
        }

        mainControlImage.setImage(image);

        int randomButton = (int) (Math.random() * 5);
        buttonList.get(randomButton).setText(correctControlText);
        buttonList.remove(randomButton);

        for (int i = 0; i < 4; i++)
        {
            buttonList.get(i).setText(randomControlTexts[i]);
        }
    }

    /* Called when any button is pressed, also when key 1-5 is pressed. Checks if choice is correct and proceeds to update */
    private void choiceButtonPressed(int buttonIndex)
    {
        if(!choiceKeyIsPressed) //Checks if key is already pressed, to not choose multiple times per click
        {
            boolean correct = false;

            switch (buttonIndex) //Checks if text of chosen button matches correct answer
            {
                case 1:
                    if (button1.getText().equals(correctControlText))
                    {
                        correct = true;
                    }
                    break;
                case 2:
                    if (button2.getText().equals(correctControlText))
                    {
                        correct = true;
                    }
                    break;
                case 3:
                    if (button3.getText().equals(correctControlText))
                    {
                        correct = true;
                    }
                    break;
                case 4:
                    if (button4.getText().equals(correctControlText))
                    {
                        correct = true;
                    }
                    break;
                case 5:
                    if (button5.getText().equals(correctControlText))
                    {
                        correct = true;
                    }
                    break;
            }

            if (correct)
            {
                score++;
                scoreLabel.setText("Score: " + score);

                updateHighScores(score);

                confirmationLabel.setText("Correct!");
                confirmationLabel.setTextFill(Color.rgb(161, 181, 108));

                setNewGameContent(imageDescriptions, imageRandomizer(controlImageList));

                countdown();
            }
            else
            {
                gameLost();
            }
        }

    }

    public void gameLost()
    {
        //Confirmation label
        confirmationLabel.setText("Game Over!");
        confirmationLabel.setTextFill(Color.rgb(171, 70, 66));

        //Stops and hides countdown
        timeline.stop();
        countdownLabel.setDisable(true);
        countdownBar.setDisable(true);

        //Turns on option to try again
        tryAgainButton.setVisible(true);

        //Blocks further input by disabling key press and button press
        blockKeyInput = true;
        button1.setDisable(true);
        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);
        button5.setDisable(true);

        //Adds one to times played on current user
        currentUser.addTimesPlayed();


        /* Sets correct button to blue color, to see which option was right */
        for (int i = 0; i < 5; i++)
        {
            switch (i) {
                case 0:
                    if (button1.getText().equals(correctControlText)) {
                        button1.setStyle("-fx-background-color: #7cafc2;");
                    }
                    break;
                case 1:
                    if (button2.getText().equals(correctControlText)) {
                        button2.setStyle("-fx-background-color: #7cafc2;");
                    }
                    break;
                case 2:
                    if (button3.getText().equals(correctControlText)) {
                        button3.setStyle("-fx-background-color: #7cafc2;");
                    }
                    break;
                case 3:
                    if (button4.getText().equals(correctControlText)) {
                        button4.setStyle("-fx-background-color: #7cafc2;");
                    }
                    break;
                case 4:
                    if (button5.getText().equals(correctControlText)) {
                        button5.setStyle("-fx-background-color: #7cafc2;");
                    }
                    break;
            }
        }
    }


    public void countdown()
    {
        //Checks if there already is a timer running
        if (timeline != null)
        {
            timeline.stop();
        }

        //Checks if timer is disabled
        if (countdownLabel.isDisabled())
        {
            countdownLabel.setDisable(false);
        }
        if (countdownBar.isDisabled())
        {
            countdownBar.setDisable(false);
        }

        //Used in timer
        timeTicks = (int) (STARTTIME / (0.15 * score + 1));

        int fulltimeTenthSeconds = timeTicks;


        //Countdown
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10), actionEvent ->
        {
            timeTicks--;

            timeSeconds = (timeTicks / 100) + 1;
            countdownLabel.setText(timeSeconds.toString());

            double progress = (double) timeTicks / fulltimeTenthSeconds;
            countdownBar.setProgress(progress);

            //Sets color of progress bar
            int hue = (int) (76 * progress);
            Color color = Color.web("hsl(" + hue + ",40%,71%)");
            String colorString = "rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ");";

            countdownBar.setStyle("-fx-accent:" + colorString);

            if (timeTicks <= 0)
            {
                timeline.stop();
                gameLost();
            }
        }));

        timeline.playFromStart();
    }

    public void tryAgainButtonPressed()
    {
        score = 0;
        scoreLabel.setText("Score: " + score);

        confirmationLabel.setText("");
        setNewGameContent(imageDescriptions, imageRandomizer(controlImageList));

        blockKeyInput = false;
        tryAgainButton.setVisible(false);

        countdown();

        button1.setDisable(false);
        button2.setDisable(false);
        button3.setDisable(false);
        button4.setDisable(false);
        button5.setDisable(false);

        resetButtons();
    }

    public void button1Pressed()
    {
        choiceButtonPressed(1);
    }
    public void button2Pressed()
    {
        choiceButtonPressed(2);
    }
    public void button3Pressed()
    {
        choiceButtonPressed(3);
    }
    public void button4Pressed()
    {
        choiceButtonPressed(4);
    }
    public void button5Pressed()
    {
        choiceButtonPressed(5);
    }

    /* Sets and sorts highscore list with user HighScores */
    public void setHighScores()
    {
        highScoreScoreListView.getItems().clear();
        highScoreNameListView.getItems().clear();

        userList = choosePlayerController.getUserList();

        ArrayList<String> nameList = new ArrayList<>(userList.keySet());

        for(int i = 0; i < nameList.size(); i++)
        {
            highScoreNameListView.getItems().add(nameList.get(i));
            highScoreScoreListView.getItems().add(userList.get(nameList.get(i)).getHighScore());
        }

        /* Bubble sorting for list */
        for (int outer = (highScoreScoreListView.getItems().size() - 1); outer > 0; outer--)
        {
            for (int inner = 0; inner < outer; inner++)
            {
                if(highScoreScoreListView.getItems().get(inner) < highScoreScoreListView.getItems().get(inner + 1))
                {
                    int tempScore = highScoreScoreListView.getItems().get(inner);
                    String tempName = highScoreNameListView.getItems().get(inner);

                    highScoreScoreListView.getItems().remove(inner);
                    highScoreNameListView.getItems().remove(inner);

                    highScoreScoreListView.getItems().add(inner, highScoreScoreListView.getItems().get(inner));
                    highScoreNameListView.getItems().add(inner, highScoreNameListView.getItems().get(inner));


                    highScoreScoreListView.getItems().remove(inner + 1);
                    highScoreNameListView.getItems().remove(inner + 1);

                    highScoreScoreListView.getItems().add(inner + 1, tempScore);
                    highScoreNameListView.getItems().add(inner + 1, tempName);

                }
            }
        }
    }

    /* Sorts highscoreList */
    private void updateHighScores(int currentScore)
    {
        currentUser.checkAndSetHighScore(currentScore);

        for(int i = 0; i < highScoreNameListView.getItems().size(); i++)
        {
            if(highScoreNameListView.getItems().get(i).equals(currentUser.getName()))
            {
                highScoreScoreListView.getItems().remove(i);
                highScoreScoreListView.getItems().add(i, currentUser.getHighScore());
            }
            if(highScoreNameListView.getItems().get(i).equals(currentUser.getUserName()))
            {
                highScoreScoreListView.getItems().remove(i);
                highScoreScoreListView.getItems().add(i, currentUser.getHighScore());
            }

            if (i > 0)
            {
                if(highScoreScoreListView.getItems().get(i) > highScoreScoreListView.getItems().get(i - 1))
                {
                    int tempScore = highScoreScoreListView.getItems().get(i - 1);
                    String tempName = highScoreNameListView.getItems().get(i - 1);

                    highScoreScoreListView.getItems().remove(i - 1);
                    highScoreNameListView.getItems().remove(i - 1);

                    highScoreScoreListView.getItems().add(i, tempScore);
                    highScoreNameListView.getItems().add(i, tempName);

                }
            }
        }
    }

    public static void setCurrentUser(User inputCurrentUser)
    {
        currentUser = inputCurrentUser;
    }

    public static void addUser(User userAdd)
    {
        if (!userAdd.getName().equals(" "))
        {
            userList.put(userAdd.getName(), userAdd);
        }
        else
        {
            userList.put(userAdd.getUserName(), userAdd);
        }
    }

    public static void saveUserData()
    {
        /* Try-catch Clears csv file */
        try
        {
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter("src/assets/localUserData.csv"));
            csvWriter.write("");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ArrayList<String> nameList = new ArrayList<>(userList.keySet());

        for (String name : nameList)
        {
            userList.get(name).addSaveToCsv(new File("src/assets/localUserData.csv"));
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
        main.maxRestoreWindow();
    }

    /* Minimizes window */
    public void minimizeButton()
    {
        main.window.setIconified(true);
    }

    /* Goes to previous window */
    public void backButton()
    {
        currentUser.addTimesPlayed();
        main.changeScene("choosePlayer");

        score = 0;
        scoreLabel.setText("Score: " + score);

        confirmationLabel.setText("");
        setNewGameContent(imageDescriptions, imageRandomizer(controlImageList));

        timeline.stop();
        setGameDisable(true);
        resetButtons();

        countdownLabel.setDisable(true);
        countdownBar.setDisable(true);
        startGameButton.setDisable(false);
        tryAgainButton.setVisible(false);
    }

    public void resetButtons()
    {
        button1.setStyle(null);
        button2.setStyle(null);
        button3.setStyle(null);
        button4.setStyle(null);
        button5.setStyle(null);
    }
}
