package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.EventObject;


public class MyChatAppClient extends JFrame implements ActionListener {
    //define
    String username;
    PrintWriter pw;
    BufferedReader br;
    JTextArea taMessages; // JFrame
    JTextField tfInput;
    JButton btnSend, btnExit;
    Socket client;

    //title , frame, name
    public MyChatAppClient(String username, String serverName) throws IOException {
        super(username);
        this.username = username;
        client = new Socket(serverName, 9999);
        br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        pw = new PrintWriter(client.getOutputStream(), true);
        pw.println(username);
        buildInterface();//call the interface
    }

    // Interface method
    public void buildInterface() {
        btnSend = new JButton("Send");
        btnExit = new JButton("Exit");
        taMessages = new JTextArea();
        taMessages.setRows(10);
        taMessages.setColumns(50);
        taMessages.setEditable(false);
        tfInput = new JTextField(50);
        JScrollPane sp = new JScrollPane(taMessages, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp, "Center");
        JPanel bp = new JPanel(new FlowLayout());
        bp.add(tfInput);
        bp.add(btnSend);
        bp.add(btnExit);
        add(bp, "South");
        btnSend.addActionListener(this);
        btnExit.addActionListener(this);
        setSize(550, 300);
        setVisible(true);
        pack();
    }


    @Override
    public void actionPerformed(ActionEvent evt) { // method // evt parameter
        if (evt.getSource() == btnExit) {
            pw.println("end");
            System.exit(0);
        } else {
            pw.println(tfInput.getText());
        }
    }

    //main method
    public static void main(String[] args){
        String name = JOptionPane.showInputDialog(null, "Enter your name: ",
                "Username",JOptionPane.PLAIN_MESSAGE);
        String servername = "localhost";
        try{

            //
            new MyChatAppClient(name, servername);


        }catch (Exception exception){

        }
    }
    class MessagesThread extends Thread {
        public void run(){
            String line;
            try{
                while (true){
                    line = br.readLine();
                    taMessages.append(line + "\n");
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
