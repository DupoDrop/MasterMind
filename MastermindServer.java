import java.lang.*;
import java.net.*;

public class MastermindServer
{
    public static void main(String argv[])
    {
        try
        {
            ServerSocket sock = new ServerSocket(2059);
            while (true)
            {
                Socket clientSocket = sock.accept();
                ServerThread client = new ServerThread(clientSocket);
                client.start();
            }
        }
        catch (Exception e)
        {

        }
    }
}

