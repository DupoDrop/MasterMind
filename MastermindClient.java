import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;

//ajouter des flush entre chaque étape?
//gérer les ctrl+c voir handling SIGINT, ne pas oublier de close le socket quand ce signal est catch

public class MastermindClient
{
    public static void main(String argv[])
    {
        try
        {
            Socket sock = new Socket("localhost", 2059);

            OutputStream serverOut = sock.getOutputStream();
            InputStream serverIn = sock.getInputStream();
            Scanner userScanner = new Scanner(System.in);

            String msgIn;
            int commandIn;
            boolean correctInput;

            System.out.println("Welcome to the game of Mastermind.");
            System.out.println("The possible colors are: red, blue, yellox, white, green and black.");
            System.out.println("A new game starts.");

            while (true)
            {
                System.out.println("1) Propose a combination");
                System.out.println("2) See already proposed combinations");
                System.out.println("3) Quit");

                correctInput = false;

                while (!correctInput)
                {
                    correctInput = true;

                    msgIn = userScanner.nextLine();
                    System.out.println("Your choice: " + msgIn);

                    try
                    {
                        commandIn = Integer.parseInt(msgIn);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("input must be an integer");
                        correctInput = false;
                        //In order to have commandIn initialized so it can compile even if the switch won't be executed anyway if this command is executed
                        commandIn = 0;
                    }
                    if (correctInput)
                    {
                        serverOut.write(1);

                        switch (commandIn)
                        {
                            case 1:
                                System.out.println("enter the combination:");
                                ArrayList<String> combination = new ArrayList<String>;
                                //I use a do while so that it waits for the user to enter the line and only then stop if there is no token.
                                do
                                    combination.add(userScanner.next());
                                while(userScanner.hasNext());

                                ClientProtocole.combinationAnalysis(serverOut, serverIn, combination);

                                correctInput = true;
                                break;
                            case 2:
                                serverOut.write(2);
                                correctInput = true;
                                break;
                            case 3:
                                serverOut.write(0);
                                System.out.println("A new game starts.");
                                correctInput = true;
                                break;
                            default:
                                System.out.println("input must be 1, 2 or 3");
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {

        }
    }
}
