import java.io.IOException;
import java.io.OutputStream;

/**
 * Protocol common to both server and client
 */
public abstract class Protocol
{

    protected static final byte VERSION = 1;
    protected static final int COMBINATION_LENGTH = 4;
    protected static final int AVAILABLE_ATTEMPTS = 12;

    protected static final byte NEW_GAME_REQUEST = 0;
    protected static final byte COMBINATION_ANALYSIS_REQUEST = 1;
    protected static final byte COMBINATION_LIST_REQUEST = 2;

    protected static final byte GAME_STARTED = 1;
    protected static final byte COMBINATION_RECEIVED = 2;
    protected static final byte LIST_RECEIVED = 3;
    protected static final byte REQUEST_ERROR = 4;

    /**
     * try several times to write on a stream in case it fail but still has a limit of try to avoid infinite loops
     *
     * @param out     the OutputStream on which to write
     * @param message the message to be written
     * @throws IOException in case it still failed after a given amount of tries.
     */
    protected static void try_write(OutputStream out, byte[] message) throws IOException
    {
        final int MAX_TRY = 100;
        int count = 0;
        boolean succeed = false;

        while (!succeed)
        {
            try
            {
                out.write(message);
                succeed = true;
            }
            catch (IOException e)
            {
                if (count > MAX_TRY)
                    throw e;
                count++;

            }
        }
    }
}
