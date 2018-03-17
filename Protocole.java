import java.io.IOException;
import java.io.OutputStream;

public class Protocole
{
    protected static final int VERSION = 1;
    protected static final int COMBINATION_LENGTH = 4;

    protected static final int NEW_GAME_REQUEST = 0;
    protected static final int COMBINATION_ANALYSIS_REQUEST = 1;
    protected static final int COMBINATION_LIST_REQUEST = 2;

    protected static final int GAME_STARTED = 1;
    protected static final int COMBINATION_RECEIVED = 2;
    protected static final int LIST_RECEIVED = 3;
    protected static final int REQUEST_ERROR = 4;

    protected static void tryWrite(OutputStream out, int message) throws IOException
    {
        final int MAXTRY = 100;
        int count = 0;
        boolean succeed = false;

        while(!succeed)
        {
            try
            {
                out.write(message);
                succeed = true;
            }
            catch (IOException e)
            {
                if (count > MAXTRY)
                    throw e;
                count++;

            }
        }
    }
}
