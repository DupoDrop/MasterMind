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
        try_write(out, VERSION);
        try_write(out, GAME_STARTED);
    }

    public static void combination_answer(OutputStream out, int wellPlaced, int wrongPlaced) throws IOException
    {
        try_write(out, VERSION);
        try_write(out, COMBINATION_RECEIVED);
        try_write(out, (byte)wellPlaced);
        try_write(out, (byte)wrongPlaced);
    }

    public static void send_list(OutputStream out, int nbCombinations, Color[][] combination, int[][] answer) throws IOException
    {
        try_write(out, VERSION);
        try_write(out, LIST_RECEIVED);
        try_write(out, (byte)nbCombinations);

        for (int i = 0; i < nbCombinations; i++)
        {
            for (int j = 0; j < COMBINATION_LENGTH; j++)
                try_write(out, combination[i][j].get_code());

            for (int j = 0; j < 2; j++)
                try_write(out, (byte) answer[i][j]);

        }
    }

    public static void request_error(OutputStream out) throws IOException
    {
        try_write(out, VERSION);
        try_write(out, REQUEST_ERROR);
    }
}
