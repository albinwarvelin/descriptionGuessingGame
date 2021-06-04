package mainApp;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import mainApp.controllers.gameMainController;

public class main extends Application
{

    public static Stage window;
    private static Parent choosePlayerPane;
    private static Scene choosePlayerScene;
    private static Parent gameMainPane;
    private static Scene gameMainScene;

    private static boolean userDataIsLoaded = false;

    /* Main method launches application by calling start method */
    public static void main(String[] args)
    {
        launch(args);
    }

    /* Creates stage "window" and sets title and scene */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        window = primaryStage;
        window.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("views/mainMenu.fxml"));
        window.setTitle("Game");
        window.setScene(new Scene(root, 1280, 720));
        window.show();

        /* Loads all scenes for further use, to minimize lag */
        choosePlayerPane = FXMLLoader.load(getClass().getResource("views/choosePlayer.fxml"));
        choosePlayerScene = new Scene(choosePlayerPane, 1280, 720);

        gameMainPane = FXMLLoader.load(getClass().getResource("views/gameMain.fxml"));
        gameMainScene = new Scene(gameMainPane, 1280, 720);

        window.setOnCloseRequest(e -> close());
    }

    /* Changes scene */
    public static void changeScene(String key)
    {
        Scene switchToScene;
        switch (key)
        {
            case "choosePlayer":
                switchToScene = choosePlayerScene;
                break;

            case "gameMain":
                switchToScene = gameMainScene;
                break;

            default:
                throw new NullPointerException("Incorrect key, no scene found.");
        }

        window.setScene(switchToScene);
    }

    /* Changes scene with fade animation */
    public static void fadeChangeScene(Node fromPane, String key, int duration)
    {
        Node toPane;
        Scene switchToScene;
        switch (key)
        {
            case "choosePlayer":
                toPane = choosePlayerPane;
                toPane.setOpacity(0);
                switchToScene = choosePlayerScene;
                break;

            case "gameMain":
                toPane = gameMainPane;
                toPane.setOpacity(0);
                switchToScene = gameMainScene;
                break;

            default:
                throw new NullPointerException("Incorrect key, no scene found.");
        }

        FadeTransition fadeOut = new FadeTransition(Duration.seconds((double) duration / 2), fromPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds((double) duration / 2), toPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeOut.setOnFinished((ActionEvent) ->
        {
            window.setScene(switchToScene);

            window.centerOnScreen();

            fadeIn.play();
        });

        fadeOut.play();
    }

    /* Sets boolean userDataLoaded, used to determine if app should save userdata on close */
    public static void setUserDataLoaded()
    {
        userDataIsLoaded = true;
    }

    /* Closes stage and saves userData */
    public static void close()
    {
        if(userDataIsLoaded)
        {
            gameMainController.saveUserData();
        }
        window.close();
    }
}
