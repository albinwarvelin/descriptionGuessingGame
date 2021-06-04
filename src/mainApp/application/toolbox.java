package mainApp.application;


public class toolbox
{
    public static void blankRow(int rows)
    {
        for (int i = 1; i <= rows; i++)
        {
            System.out.println();
        }
    }

    public static int potens(double bas, double exponent)
    {
        double a = bas;

        for (int i = 0; i < exponent; i++)
        {
            a *= bas;
        }
        return (int) a;
    }

    public static void backslash(int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            System.out.print('\b');
        }
    }

    public static String getCurrentDirectory()
    {
        return System.getProperty("user.dir");
    }

    public static void space(int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            System.out.print(" ");
        }
    }

}

