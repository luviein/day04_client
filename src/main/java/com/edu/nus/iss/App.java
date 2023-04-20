package com.edu.nus.iss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws NumberFormatException, UnknownHostException, IOException
    {
        String serverHost = args[0];
        String serverPort = args[1];

        //establish connection to server - slide 8
        //***** server must be started first

        Socket socket = new Socket(serverHost, Integer.parseInt(serverPort));

        //setup console input from keyboard
        Console con = System.console();
        //variables to save keyboard input
        String keyboardInput = "";
        //variable to save msgReceived
        String msgReceived = "";

        try(InputStream is = socket.getInputStream()){
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            try(OutputStream os = socket.getOutputStream()){
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                //while loop here
                while(!keyboardInput.equals("close")){
                    keyboardInput = con.readLine("Enter a command.");
                    
                    //sends message through the communication tunnel. writing data to cookie
                    dos.writeUTF(keyboardInput);
                    dos.flush(); //have to always flush to send message across

                    //receive message from server (response) and process it
                    msgReceived = dis.readUTF();
                    System.out.println(msgReceived);

                }

                //close in while loop
                dos.close();
                bos.close();
                os.close();

            }catch(EOFException ex){

            }
            //closes streams in reverse order
            dis.close();
            bis.close();
            is.close();


        }catch(EOFException ex){
            ex.printStackTrace();
            socket.close();
        }
    
    
    }
}
