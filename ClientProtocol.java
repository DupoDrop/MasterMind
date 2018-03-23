import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

//TODO : add timeout if the server does not respond. the server does not respond, enter quit to quit.

public class ClientProtocol extends Protocol
{
    public static void new_game(OutputStream out, InputStream in) throws IOException, ServerException
    {
        byte[] message = {VERSION, NEW_GAME_REQUEST};
        try_write(out, message);

        if (in.read() != VERSION || in.read() != GAME_STARTED)
            throw new ServerException();
    }

    public static byte[] combination_analysis(OutputStream out, InputStream in, ArrayList<String> combination) throws CombinationLengthException, ColorException, IOException, ServerException
    {
        if (combination.size() != COMBINATION_LENGTH)
            throw new CombinationLengthException();

        byte[] message = new byte[2+COMBINATION_LENGTH];

        message[0] = VERSION;
        message[1] = COMBINATION_ANALYSIS_REQUEST;

        for (int i = 0; i < COMBINATION_LENGTH; i++)
            message[2+i] = Color.get_associated_color(combination.get(i)).get_code();

        try_write(out, message);

        if (in.read() != VERSION || in.read() != COMBINATION_RECEIVED)
            throw new ServerException();

        byte[] result = new byte[2];
        result[0] = (byte) in.read(); // good well placed
        result[1] = (byte) in.read(); // good wrong placed

        if (result[0] < 0 || result[1] < 0 || result[0] + result[1] > COMBINATION_LENGTH)
            throw new ServerException();

        return result;
    }

    public static byte[][] combination_list(OutputStream out, InputStream in, int numberTested) throws IOException, ServerException
    {
        byte[] message = {VERSION, COMBINATION_LIST_REQUEST};
        try_write(out, message);

        if (in.read() != VERSION || in.read() != LIST_RECEIVED || in.read() != numberTested)
            throw new ServerException();

        byte[][] result = new byte[numberTested][COMBINATION_LENGTH + 2];

        int nbRead = 0;
        int toRead = numberTested*(COMBINATION_LENGTH+2);
        byte[] read = new byte[toRead];

        while (nbRead < toRead)
            nbRead += in.read(read, nbRead, toRead);


        for (int i = 0; i < numberTested; i++)
        {
            for (int j = 0; j < COMBINATION_LENGTH + 2; j++)
                result[i][j] = read[i*(COMBINATION_LENGTH+2)+j];

            if (result[i][COMBINATION_LENGTH] < 0 || result[i][COMBINATION_LENGTH + 1] < 0 || result[i][COMBINATION_LENGTH] + result[i][COMBINATION_LENGTH + 1] > COMBINATION_LENGTH)
                throw new ServerException();
        }

        return result;
    }

}
