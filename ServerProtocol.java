import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ServerProtocol extends Protocol
{
    public static byte receive_command(InputStream in) throws IOException, ClientException
    {
        if (in.read() != VERSION)
            throw new ClientException();

        return (byte) in.read();
    }

    public static Color[] read_combination(InputStream in) throws ClientException, IOException
    {
        Color[] combination = new Color[COMBINATION_LENGTH];
        for(int i = 0; i < COMBINATION_LENGTH; i++)
        {
            try
            {
                combination[i] = Color.get_associated_color((byte) in.read());
            }
            catch (ColorException e)
            {
                throw new ClientException();
            }
        }

        return combination;
    }

    public static void game_started(OutputStream out) throws IOException
    {
        byte[] message = {VERSION, GAME_STARTED};
        try_write(out, message);
    }

    public static void combination_answer(OutputStream out, int wellPlaced, int wrongPlaced) throws IOException
    {
        byte[] message = {VERSION, COMBINATION_RECEIVED, (byte)wellPlaced, (byte)wrongPlaced};
        try_write(out, message);
    }

    public static void send_list(OutputStream out, int nbCombinations, Color[][] combination, int[][] answer) throws IOException
    {
        byte[] message = new byte[3+nbCombinations*(COMBINATION_LENGTH+2)];

        message[0] = VERSION;
        message[1] = LIST_RECEIVED;
        message[2] = (byte)nbCombinations;

        for (int i = 0; i < nbCombinations; i++)
        {
            for (int j = 0; j < COMBINATION_LENGTH; j++)
                message[3+(COMBINATION_LENGTH+2)*i+j] = combination[i][j].get_code();

            for (int j = 0; j < 2; j++)
                message[3+(COMBINATION_LENGTH+2)*i+j+COMBINATION_LENGTH] = (byte) answer[i][j];
        }

        try_write(out, message);
    }

    public static void request_error(OutputStream out) throws IOException
    {
        byte message[] = {VERSION, REQUEST_ERROR};
        try_write(out, message);
    }
}
