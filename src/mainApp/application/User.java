package mainApp.application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class User
{
    private String firstName;
    private String lastName;

    private int highScore = 0;
    private int timesPlayed = 0;

    /* Sets all data variables, used when constructing */
    public User(String inputFirstName, String inputLastName, Integer inputHighScore, Integer inputTimesPlayed)
    {
        firstName = inputFirstName;
        lastName = inputLastName;
        highScore = inputHighScore;
        timesPlayed = inputTimesPlayed;
    }

    /* Sets firstname for mainApp.user, removes blank space before and after */
    public void setFirstName(String inputFirstName)
    {
        firstName = inputFirstName.trim();
    }

    /* Sets lastname for mainApp.user, removes blank space before and after */
    public void setLastName(String inputLastName)
    {
        lastName = inputLastName.trim();
    }

    /* Sets full name for mainApp.user, removes blank space before and after */
    public void setName(String inputFirstName, String inputLastName)
    {
        firstName = inputFirstName.trim();
        lastName = inputLastName.trim();
    }

    /* Checks if current highscore is lower than input */
    public void checkAndSetHighScore(int inputHighscore)
    {
        if (highScore < inputHighscore)
        {
            highScore = inputHighscore;
        }
    }

    /* Adds 1 to timesplayed */
    public void addTimesPlayed()
    {
        timesPlayed++;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getName()
    {
        return (firstName + " " + lastName);
    }

    public int getHighScore()
    {
        return highScore;
    }

    public int getTimesPlayed()
    {
        return timesPlayed;
    }

    public void addSaveToCsv(File csvFile)
    {
        try
        {
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFile, true));
            csvWriter.write(firstName + "," + lastName + "," + highScore + "," + timesPlayed + "\n");
            csvWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
