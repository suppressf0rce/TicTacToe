package control;

import com.sun.corba.se.impl.activation.ServerMain;
import model.Server;

import java.io.IOException;

/**
 * This thread maintains the active threads of the server.<br>
 * If the server have clients that are disconnected it removes from the active threads and stops the thread from the running
 * Created by suppressf0rce on 4/16/17.
 */
public class ServerMaintainer extends Thread {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    private Server server;


    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public ServerMaintainer(Server server) {
        this.server = server;
    }


    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        while (true) {
            System.out.println("Starting maintenance...");
            for (int i = 0; i < server.getActiveThreads().size(); i++) {
                try {
                    if (!server.getActiveThreads().get(i).getSocket().getInetAddress().isReachable(1000)) {
                        server.getActiveThreads().get(i).interrupt();
                        System.out.println("Disconnecting inactive client: " + server.getActiveThreads().get(i).getSocket().getInetAddress());
                        server.getActiveThreads().remove(i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
