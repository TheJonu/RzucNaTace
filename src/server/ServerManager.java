package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;


public class ServerManager implements ActionListener {

    private InetAddress localHost;
    private ServerSocket serverSocket;

    private boolean ready = false;

    private JFrame frame;
    private JTextField socketInputField;
    private JButton actionButton;
    private JLabel stateLabel;
    private JLabel addressLabel;
    private JLabel receivedLabel;


    public ServerManager() {
        open();
    }

    /**
     * Activated when the main button is pressed
     * @param event
     */
    public void actionPerformed(ActionEvent event) {

        // if the server is not ready, do SETUP
        if(!ready){
            setup();
        }
        // if the server is ready, RUN
        else{
            run();
        }
    }

    /**
     * Waits to receive data
     */
    private void run(){
        try {
            Socket socket = serverSocket.accept();
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            String fromClient = br.readLine();
            System.out.println("From client: [" + fromClient + "]");
            pw.println("Echo: " + fromClient);
            br.close();
            socket.close();
            receivedLabel.setText("Received: " + fromClient);
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
        }
    }

    /**
     * Does setup (creates a socket)
     */
    private void setup(){
        String socketStr = socketInputField.getText();
        stateLabel.setText("State: setup...");

        try {
            int socketInt = Integer.parseInt(socketStr);
            serverSocket = new ServerSocket(socketInt);
            serverSocket.setSoTimeout(200);
        }
        catch (Exception ex) {
            System.err.println("Create server socket: " + ex);
            stateLabel.setText("BAD SOCKET");
            return;
        }

        ready = true;
        stateLabel.setText("State: ready");
        addressLabel.setText("Address: " + localHost.getHostAddress() +
                ":" + serverSocket.getLocalPort());
        actionButton.setText("Run");
    }

    /**
     * Opens and creates the server window
     */
    private void open() {

        // LOCAL HOST
        try{
            localHost = InetAddress.getLocalHost();
        } catch (java.net.UnknownHostException ex){
            System.err.println("Create server socket: " + ex);
            return;
        }

        // GUI
        frame = new JFrame("Serwer RnT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel hostAddressLabel = new JLabel("Host Address: " + localHost.getHostAddress());
        hostAddressLabel.setBounds(0, 0, 300, 20);
        frame.add(hostAddressLabel);

        JLabel hostNameLabel = new JLabel("Host Name: " + localHost.getHostName());
        hostNameLabel.setBounds(0, 20, 300, 20);
        frame.add(hostNameLabel);

        JLabel socketInputLabel = new JLabel("Socket (1024-65535):");
        socketInputLabel.setBounds(0, 50, 150, 20);
        frame.add(socketInputLabel);

        socketInputField = new JTextField("12129");
        socketInputField.setBounds(150, 50, 150, 20);
        frame.add(socketInputField);

        actionButton = new JButton("Setup");
        actionButton.setBounds(0,70, 300, 40);
        actionButton.addActionListener(this);
        frame.add(actionButton);

        stateLabel = new JLabel("");
        stateLabel.setBounds(0, 120, 300, 20);
        frame.add(stateLabel);

        addressLabel = new JLabel("");
        addressLabel.setBounds(0, 140, 300, 20);
        frame.add(addressLabel);

        receivedLabel = new JLabel("");
        receivedLabel.setBounds(0, 170, 300, 20);
        frame.add(receivedLabel);

        frame.setLayout(null);
        frame.setSize(300,300);
        frame.setVisible(true);
    }
}
