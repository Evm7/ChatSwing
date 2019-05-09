package ChatSwing;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServidor {

    //Number of the port we are going to listen to
    private static int portNumber;
    //Limits de capacity of thread pool
    private static final int maxThreads = 50;
    // Create a Collection with al client names to prohibit duplication in names
    private static Set<String> client_names = new HashSet<>();
    // Collecction of all Clients
    private static Set<PrintWriter> clients = new HashSet<>();

    public static void main(String[] args) {

        portNumber = 5000;
        if (args.length == 1 && !args[0].isEmpty()) {
            portNumber = Integer.parseInt(args[0]);
        }
        //Create Executor Service and limit its pool Thread number
        ExecutorService pool = Executors.newFixedThreadPool(maxThreads);

        //Establish ServerSocket in order to listen to portNumber.
        MyServerSocket myserversocket;
        try {
            myserversocket = new MyServerSocket(portNumber);
            System.out.println("Server is listening to port " + portNumber);

            /**
             * Accepts connections from listener continiously Create a handler
             * which manage the connection accepted Executes runnable Handler by
             * pool thread Executor.
             */
            while (true) {
                pool.execute(new Handler(myserversocket.my_accept()));
            }
        } catch (IOException ex) {
            System.out.println("Error while listening to port " + portNumber + ": " + ex);
        }

    }

    /**
     * Thread that handles client's treatment. Class which handles Input/output
     * Reads/writes bytes from/to sockets Server implements Reactor Pattern by
     * adding this active class.
     */
    private static class Handler implements Runnable {

        private MySocket mysocket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(MySocket socket) {
            this.mysocket = socket;
        }

        /**
         * Manage Handler tasks. 1.- Checks client-name is not duplicated.
         * 2.-Managed inputs. 3.- Broadcast outputs.
         */
        public void run() {
            try {
                //Bind socket In and Out to handler
                String name;
                in = new BufferedReader(mysocket.getInputStream());
                out = new PrintWriter(mysocket.getOutputStream(), true);
                System.out.println("ACCEPT Process: Getting name from a client");        //ALERT
                // Get name from the client
                while (true) {
                    mysocket.println("ACCEPT:");
                    name = mysocket.readLine();
                    System.out.println("ACCEPT name: " + name);                           //ALERT
                    if (name == null) {
                        return;
                    }
                    //Synchronize list of names by exclusively allowing 
                    //this handler to modify List.
                    synchronized (client_names) {
                        //Add username if it is not used
                        if (!name.isEmpty() && !client_names.contains(name)) {
                            mysocket.setName(name);
                            client_names.add(name);
                            break;
                        }
                    }
                }

                //Notify new Join in Chat
                mysocket.println("JOIN:" + name);
                System.out.println(name + " has joined");                                //ALERT
                System.out.println("Users: " + client_names.toString());
                mysocket.println("USERS:" + client_names.toString());
                //Broadcast message of join
                for (PrintWriter writer : clients) {
                    writer.println("MESSAGE:" + "Admin" + " --> " + name + " has joined");
                    writer.println("USERS:" + client_names.toString());

                }
                //Add client to easy broadcast. Notify that client 
                //does not receive above message
                clients.add(mysocket.getOutputStream());

                // Listen for new incoming messages from this client
                while (true) {
                    String input = mysocket.readLine();
                    if (input.toLowerCase().startsWith("QUIT:" + name)) {
                        return;
                    }
                    System.out.println(input);                             //ALERT
                    for (PrintWriter writer : clients) {
                        writer.println("MESSAGE:" + input);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (mysocket.getName() != null) {
                    if (mysocket != null) {
                        clients.remove(mysocket.getOutputStream());
                    }
                    if (mysocket.getName() != null) {
                        System.out.println(mysocket.getName() + " is leaving");
                        client_names.remove(mysocket.getName());
                        for (PrintWriter writer : clients) {
                            writer.println("MESSAGE:Admin -->" + mysocket.getName() + " has left");
                            writer.println("USERS:" + client_names.toString());
                        }
                    }
                    mysocket.close();
                }
            }
        }
    }
}
