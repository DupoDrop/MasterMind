import java.io.IOException;
import java.io.OutputStream;

public class ServerProtocole extends Protocole
{
    public static void game_started(OutputStream out) throws IOException
    {
        try_write(out, VERSION);
        try_write(out, GAME_STARTED);
    }

    public static void combination_answer(OutputStream out, byte good, byte wellPlaced) throws IOException
    {
        try_write(out, VERSION);
        try_write(out, COMBINATION_RECEIVED);
        try_write(out, wellPlaced);
        try_write(out, good);
    }

    public static void list_received(OutputStream out, byte nbCombination, byte[][] combination) throws IOException
    {
        try_write(out, VERSION);
        try_write(out, LIST_RECEIVED);
        try_write(out, nbCombination);

        for (int i = 0; i < nbCombination; i++)
        {
            for (int j = 0; j < COMBINATION_LENGTH + 2; j++)
                try_write(out, combination[i][j]);
        }
    }

    public static void request_error(OutputStream out) throws IOException
    {
        try_write(out, VERSION);
        try_write(out, REQUEST_ERROR);
    }
}
