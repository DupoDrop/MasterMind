import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ServerProtocol extends Protocol
{
    //this class is not meant to be instantiated
    private ServerProtocol()
    {
    }

    /**
     * receive a command from the client
     *
     * @param in the InputStream used to communicate with the client
     * @return a byte corresponding to the code of the command
     * @throws IOException     in case of communication problem
     * @throws ServerException in case the client behaves badly
     */
    public static byte receive_command(InputStream in) throws IOException, ClientException
    {
        if (in.read() != VERSION)
            throw new ClientException();

        return (byte) in.read();
    }

    /**
     * read the combination given by the client on the stream
     *
     * @param in the InputStream used to communicate with the client
     * @return a array of Color containing the combination read
     * @throws IOException     in case of communication problem
     * @throws ServerException in case the client behaves badly
     */
    public static Color[] read_combination(InputStream in) throws ClientException, IOException
    {
        Color[] combination = new Color[COMBINATION_LENGTH];

        int nbRead = 0;
        int toRead = COMBINATION_LENGTH;
        byte[] read = new byte[toRead];

        while (nbRead < toRead)
            nbRead += in.read(read, nbRead, toRead);

        for (int i = 0; i < COMBINATION_LENGTH; i++)
        {
            try
            {
                combination[i] = Color.get_associated_color(read[i]);
            }
            catch (ColorException e)
            {
                throw new ClientException();
            }
        }

        return combination;
    }

    /**
     * send a message to the client signaling him the game has started
     *
     * @param out the OutputStream used to communicate with the client
     * @throws IOException in case of communication problem
     */
    public static void game_started(OutputStream out) throws IOException
    {
        byte[] message = {VERSION, GAME_STARTED};
        try_write(out, message);
    }

    /**
     * answer to a combination analysis request indicating the amount of well placed and good colors
     *
     * @param out the OutputStream used to communicate with the client
     * @param wellPlaced an integer representing the amount of well placed colors
     * @param wrongPlaced an integer representing the amount of good but wrong placed colors
     * @throws IOException in case of communication problem
     */
    public static void combination_answer(OutputStream out, int wellPlaced, int wrongPlaced) throws IOException
    {
        byte[] message = {VERSION, COMBINATION_RECEIVED, (byte) wellPlaced, (byte) wrongPlaced};
        try_write(out, message);
    }

    /**
     * send the list of already tested combinations to the client
     *
     * @param out the OutputStream used to communicate with the client
     * @param nbCombinations an integer representing the amount of tested combinations
     * @param combination a matrix of Colors containing the combinations. the first index indicate the combination number
     * @param answer a matrix of integer containing the answers to combination analyses. the datas with first index i
     *               should correspond to the combination of index i.
     * @throws IOException in case of communication problem
     */
    public static void send_list(OutputStream out, int nbCombinations, Color[][] combination, int[][] answer) throws IOException
    {
        byte[] message = new byte[3 + nbCombinations * (COMBINATION_LENGTH + 2)];

        message[0] = VERSION;
        message[1] = LIST_RECEIVED;
        message[2] = (byte) nbCombinations;

        for (int i = 0; i < nbCombinations; i++)
        {
            for (int j = 0; j < COMBINATION_LENGTH; j++)
                message[3 + (COMBINATION_LENGTH + 2) * i + j] = combination[i][j].get_code();

            for (int j = 0; j < 2; j++)
                message[3 + (COMBINATION_LENGTH + 2) * i + j + COMBINATION_LENGTH] = (byte) answer[i][j];
        }

        try_write(out, message);
    }

    /**
     * send a message to the client indicating an erroneous request has been received
     *
     * @param out the OutputStream used to communicate with the client
     * @throws IOException in case of connection problem
     */
    public static void request_error(OutputStream out) throws IOException
    {
        byte message[] = {VERSION, REQUEST_ERROR};
        try_write(out, message);
    }
}
