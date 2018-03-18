import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

//TODO : ajouter des timeout si le serveur ne répond pas après un certain temps. le serveur de réponds pas entrez quit pour quitter.

public class ClientProtocole extends Protocole
{
    public static void newGame(OutputStream out, InputStream in) throws IOException, ServerException
    {
            tryWrite(out, VERSION);
            tryWrite(out, NEW_GAME_REQUEST);

            if(in.read() != VERSION || in.read() != GAME_STARTED)
                throw new ServerException();
    }

    public static byte[] combinationAnalysis(OutputStream out, InputStream in, ArrayList<String> combination) throws CombinationLengthException, ColorException, IOException, ServerException
    {
        if(combination.size() != COMBINATION_LENGTH)
            throw new CombinationLengthException();

        tryWrite(out, VERSION);
        tryWrite(out, COMBINATION_ANALYSIS_REQUEST);

        for (int i = 0; i < COMBINATION_LENGTH; i++)
            tryWrite(out, Color.getAssociatedCode(combination.get(i)));

        if(in.read() != VERSION || in.read() != COMBINATION_RECEIVED)
            throw new ServerException();

        byte[] result = new byte[2];
        result[0] = (byte)in.read();
        result[1] = (byte)in.read();

        if(result[0] < 0 || result[1] < 0 || result[0]+result[1] > COMBINATION_LENGTH)
            throw new ServerException();

        return result;
    }

    public static byte[][] combinationList(OutputStream out, InputStream in, int numberTested) throws IOException, ServerException
    {
        tryWrite(out, VERSION);
        tryWrite(out, COMBINATION_LIST_REQUEST);

        if(in.read() != VERSION || in.read() != numberTested)
            throw new ServerException();

        byte[][] result = new byte[numberTested][COMBINATION_LENGTH+2];

        for(int i = 0; i < numberTested; i++)
        {
            for(int j = 0; j < COMBINATION_LENGTH+2; j++)
                result[i][j] = (byte)in.read();

            if(result[i][COMBINATION_LENGTH] < 0 || result[i][COMBINATION_LENGTH+1] < 0 || result[i][COMBINATION_LENGTH]+result[i][COMBINATION_LENGTH+1] > COMBINATION_LENGTH)
                throw new ServerException();
        }

        return result;
    }

}
