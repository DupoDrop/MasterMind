import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

//TODO : ajouter des timeout si le serveur ne répond pas après un certain temps. le serveur de réponds pas entrez quit pour quitter.

public class ClientProtocole extends Protocole
{


    public static void newGame(OutputStream out, InputStream in) throws IOException
    {
            tryWrite(out, VERSION);
            tryWrite(out, NEW_GAME_REQUEST);

    }

    public static int[] combinationAnalysis(OutputStream out, InputStream in, ArrayList<String> combination) throws CombinationLengthException, ColorException, IOException
    {
        if(combination.size() != COMBINATION_LENGTH)
            throw new CombinationLengthException();

        boolean succeed = false;

        while(!succeed)
        {
            tryWrite(out, VERSION);
            tryWrite(out, COMBINATION_ANALYSIS_REQUEST);

            for (int i = 0; i < COMBINATION_LENGTH; i++)
                tryWrite(out, Color.getAssociatedCode(combination.get(i)));


        }
    }

    public static int[] combinationList()
    {

        resultat[] = new int[2];
        return resultat;
    }

}
