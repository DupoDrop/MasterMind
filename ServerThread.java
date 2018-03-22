import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread
{
    private Socket sock;
    private Game game;

    ServerThread(Socket sock)
    {
        this.sock = sock;
        game = new Game(ServerProtocol.COMBINATION_LENGTH, ServerProtocol.AVAILABLE_ATTEMPTS);
    }

    public void run()
    {
        try
        {
            OutputStream clientOut = sock.getOutputStream();
            InputStream clientIn = sock.getInputStream();
            byte command;

            try
            {
                while (true)
                {
                    command = ServerProtocol.receive_command(clientIn);

                    switch (command)
                    {
                        case ServerProtocol.NEW_GAME_REQUEST:
                            //no need to reset answers and combinations as nbAttempts will prevent them to be used.
                            game.start_game();
                            ServerProtocol.game_started(clientOut);
                            break;
                        case ServerProtocol.COMBINATION_ANALYSIS_REQUEST:
                            Color[] combination = ServerProtocol.read_combination(clientIn);
                            try
                            {
                                int[] answer = game.analyse_combination(combination);
                                ServerProtocol.combination_answer(clientOut, answer[0], answer[1]);
                            }
                            catch (BadSizeException | NoMoreAttemptsException e)
                            {
                                throw new ClientException();
                            }
                            break;
                        case ServerProtocol.COMBINATION_LIST_REQUEST:
                            ServerProtocol.send_list(clientOut, game.getNbAttempts(), game.get_combinations(), game.get_answer());
                            break;
                        default:
                            ServerProtocol.request_error(clientOut);
                    }
                }
            }
            catch(ClientException e)
            {
                ServerProtocol.request_error(clientOut);
            }
            // sock.close();
        }

        catch(IOException e)
        {

        }
    }
}
