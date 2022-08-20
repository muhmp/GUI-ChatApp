package com.company;


// package
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import static java.lang.System.out;


//Server Side
public class MyChatApp {

    //define users as vectors
    Vector<String> users = new Vector<String>();
    Vector<HandleClient> clients = new Vector<HandleClient>(); //

    public void process() throws Exception {
        //
        ServerSocket server = new ServerSocket(9999, 10); //
        out.print("Server Started: ");
        while(true){
            Socket client = server.accept();//
            HandleClient c = new HandleClient(client);
            clients.add(c); //
        }


    }

    //main method
    public static void main(String[] args) throws Exception{ //
        new MyChatApp().process();
    }

    public void broadcast(String user, String message){
        // send message to all users
        for (HandleClient c : clients){ // :
            if(! c.getUsername().equals(user)){ //equal(user)
                c.sendMessage(user, message);
            }
        }
    }

    class HandleClient extends Thread {
        String name = "";
        BufferedReader input;
        PrintWriter output;
        public HandleClient (Socket client) throws IOException {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(),true);

            name  = input.readLine();
            users.add(name);
            start();
        }
        //method for sendMessage
        public void sendMessage(String uname, String msg){ //parameter
            output.println(uname + " - " + msg);
        }

        //method for get the username
        public String getUsername(){
            return name; // name variable previously defined
        }

        //run
        public void run(){
            String line;
            try{
                while(true){
                    //
                    line = input.readLine();
                    if (line.equals("end")){
                        clients.remove(this);
                        users.remove(name);
                        break;
                    }
                    broadcast(name,line);
                }
            }catch (Exception ex) { //exception ex
                out.println(ex.getMessage());
            }
        }
    }
}



