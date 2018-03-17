import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread
{
    private Socket sock;
    private Color[][] combinations = new Color[12][4];
    private int[][] answers = new int[12][2];
    private int nbAttempts = 0;

    ServerThread(Socket sock)
    {
        this.sock = sock;
    }

    public void run()
    {
        try
        {
            OutputStream clientOut = sock.getOutputStream();
            InputStream clientIn = sock.getInputStream();

            sock.close();
        }
        catch (Exception e)
        {

        }
    }
}
