import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;

//TODO : ajouter des flush entre chaque étape?
//TODO : gérer les ctrl+c voir handling SIGINT, ne pas oublier de close le socket quand ce signal est catch

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
            int nbProposition = 0;

            System.out.println("Welcome to the game of Mastermind.");
            System.out.print("The possible colors are: ");
            for(Color col: Color.values())
                System.out.print("col.getName() ");
            System.out.println(".");
            System.out.println("A new game starts.");

            while (true)
            {
                System.out.println("1) Propose a combination");
                System.out.println("2) See already proposed combinations");
                System.out.println("3) Quit");

                correctInput = false;

                while (!correctInput)
                {
                    msgIn = userScanner.nextLine();
                    System.out.println("Your choice: " + msgIn);

                    try
                    {
                        commandIn = Integer.parseInt(msgIn);
                        correctInput = true;
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("input must be an integer");
                        //In order to have commandIn initialized so it can compile even if the switch won't be executed anyway if this command is executed
                        commandIn = 0;
                    }
                    if (correctInput)
                    {
                        boolean succeed = false;
                        while (!succeed)
                        {
                            try
                            {
                                switch (commandIn)
                                {
                                    case 1:
                                        try
                                        {

                                            System.out.println("enter the combination:");

                                            String tmp;
                                            tmp = userScanner.nextLine();
                                            String[]combination = tmp.split(" ");

                                            byte[] analysisResult = ClientProtocol.combination_analysis(serverOut, serverIn, combination);
                                            nbProposition++;

                                            if(analysisResult[0] == ClientProtocol.COMBINATION_LENGTH)
                                            {
                                                System.out.println("Congratulation you won!");
                                                System.out.println("A new game started.");
                                                ClientProtocol.new_game(serverOut, serverIn);
                                                nbProposition = 0;
                                            }

                                            else if(nbProposition == ClientProtocol.AVAILABLE_ATTEMPTS)
                                            {
                                                System.out.println("Sadly you lost, but you can your revenge!");
                                                System.out.println("A new game started.");
                                                ClientProtocol.new_game(serverOut, serverIn);
                                                nbProposition = 0;
                                            }

                                            else
                                                System.out.println("There are " + analysisResult[0] + " well placed colors and " + analysisResult[1] + " good but wrong placed colors");

                                            succeed = true;
                                        }

                                        //if an exception is cached, succeed will not be set to true, the program will thus ask the combination again.
                                        catch (ColorException e)
                                        {
                                            System.out.println("A wrong color has been entered.");
                                            System.out.print("the possible colors are:");
                                            for (Color col : Color.values())
                                            {
                                                System.out.print(" " + col.get_name());
                                            }
                                            System.out.println();
                                        }

                                        catch (CombinationLengthException e)
                                        {
                                            System.out.println("The entered combination has not the expected length. The length of the combination should be " + ClientProtocol.COMBINATION_LENGTH);
                                        }

                                        break;

                                    case 2:

                                        if(nbProposition == 0)
                                        {
                                            System.out.println("There are no tested combination");
                                        }
                                        else
                                        {
                                            byte[][] list = ClientProtocol.combination_list(serverOut, serverIn, nbProposition);

                                            System.out.println("Here is the already tested combinations' list:");
                                            for (int i = 0; i < nbProposition; i++)
                                            {
                                                for (int j = 0; j < ClientProtocol.COMBINATION_LENGTH; j++)
                                                    System.out.print((Color.get_associated_color(list[i][j])).get_name() + ", ");

                                                System.out.print("well placed: " + list[i][ClientProtocol.COMBINATION_LENGTH] + ", ");
                                                System.out.println("good but wrong placed: " + list[i][ClientProtocol.COMBINATION_LENGTH + 1]);
                                            }
                                        }
                                        succeed = true;
                                        break;

                                    case 3:

                                        ClientProtocol.new_game(serverOut, serverIn);
                                        nbProposition = 0;
                                        System.out.println("A new game starts.");

                                        succeed = true;
                                        break;

                                    default:

                                        System.out.println("input must be 1, 2 or 3");
                                        succeed = true;
                                }
                            }
                            catch (ServerException | IOException e)
                            {
                                // in case of serverException or IOExeption, I restart the connection
                                System.out.println("an error occurred, restarting the connection and thus the game");

                                sock.close();
                                sock = new Socket("localhost", 2059);
                                serverOut = sock.getOutputStream();
                                serverIn = sock.getInputStream();

                                succeed = true; //that's not really a success, but something was done and i want to avoid infinite loop on it
                            }
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("input/output problem, ending the program");
        }
        catch (Exception e)
        {
            System.out.println("An unexpected exception occurred, ending the program");
        }
    }
}