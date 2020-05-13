package game;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class GameManager {

    public GameManager() {
        System.out.println("client created");
        open();
    }

    private void open() {
        System.out.println("client opened");
        long l = 0;
        while (true) {
            System.out.println("client running");
            try {
                Socket socket = new Socket("localhost", 12129);

                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);

                pw.println("Hello server! " + l++);

                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));

                System.out.println(br.readLine());

                br.close();
                socket.close();
            } catch (Exception e) {
                System.err.println("Client exception: " + e);
            }

        }
    }
}