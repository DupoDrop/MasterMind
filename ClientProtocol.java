import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

//TODO : add timeout if the server does not respond. the server does not respond, enter quit to quit.

/**
 * the protocol linked to the client side
 */
public final class ClientProtocol extends Protocol
{
    //this class is not meant to be instantiated
    private ClientProtocol()
    {
    }

    /**
     * Send a new game request to the Server and verify it has been done
     *
     * @param out the OutputStream used to communicate with the server
     * @param in  the InputStream used to communicate with the server
     * @throws IOException     in case of communication problem
     * @throws ServerException in case the server behaves badly
     */
    public static void new_game(OutputStream out, InputStream in) throws IOException, ServerException
    {
        byte[] message = {VERSION, NEW_GAME_REQUEST};
        try_write(out, message);

        if (in.read() != VERSION || in.read() != GAME_STARTED)
            throw new ServerException();
    }

    /**
     * send a request to the server for analysing a given combination and store the response un a byte array
     *
     * @param out         the OutputStream used to communicate with the server
     * @param in          the InputStream used to communicate with the server
     * @param combination the combination to be tested
     * @return a byte array containing the number of well placed and good but wrong place colors (in this order)
     * @throws CombinationLengthException in case the combination has not the length specified in the protocol
     * @throws ColorException             in case of non existing color present in the combination
     * @throws IOException                in case of communication problem
     * @throws ServerException            in case the server behaves badly
     */
    public static byte[] combination_analysis(OutputStream out, InputStream in, ArrayList<String> combination) throws CombinationLengthException, ColorException, IOException, ServerException
    {
        if (combination.size() != COMBINATION_LENGTH)
            throw new CombinationLengthException();

        byte[] message = new byte[2 + COMBINATION_LENGTH];

        message[0] = VERSION;
        message[1] = COMBINATION_ANALYSIS_REQUEST;

        for (int i = 0; i < COMBINATION_LENGTH; i++)
            message[2 + i] = Color.get_associated_color(combination.get(i)).get_code();

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

    /**
     * send a request to the server in order to get the list of already tested combinations and return those as a byte matrix
     *
     * @param out          the OutputStream used to communicate with the server
     * @param in           the InputStream used to communicate with the server
     * @param numberTested the number of tested combinations recorded by the client. This must match the one recorded by the server.
     * @return a byte matrix. the first index indicate the number of the combination (in chronological order).
     * the combinations are represented as byte array with the first bytes holding the Color's code and the 2 at the end the number of well places and good colors.
     * @throws IOException     in case of communication problem
     * @throws ServerException in case the server behaves badly
     */
    public static byte[][] combination_list(OutputStream out, InputStream in, int numberTested) throws IOException, ServerException
    {
        byte[] message = {VERSION, COMBINATION_LIST_REQUEST};
        try_write(out, message);

        if (in.read() != VERSION || in.read() != LIST_RECEIVED || in.read() != numberTested)
            throw new ServerException();

        byte[][] result = new byte[numberTested][COMBINATION_LENGTH + 2];

        int nbRead = 0;
        int toRead = numberTested * (COMBINATION_LENGTH + 2);
        byte[] read = new byte[toRead];

        while (nbRead < toRead)
            nbRead += in.read(read, nbRead, toRead);


        for (int i = 0; i < numberTested; i++)
        {
            for (int j = 0; j < COMBINATION_LENGTH + 2; j++)
                result[i][j] = read[i * (COMBINATION_LENGTH + 2) + j];

            if (result[i][COMBINATION_LENGTH] < 0 || result[i][COMBINATION_LENGTH + 1] < 0 || result[i][COMBINATION_LENGTH] + result[i][COMBINATION_LENGTH + 1] > COMBINATION_LENGTH)
                throw new ServerException();
        }

        return result;
    }

}
