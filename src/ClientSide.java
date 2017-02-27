
import java.io.*;
import java.net.*;

public class ClientSide {
    public static void main(String argv[]) throws Exception {
    	
    	String serverIP = argv[0].trim();
    	int portNo = Integer.parseInt(argv[1].trim());
    	String fileName = argv[2].trim();
    	
    	/*String serverIP = "localhost";
    	int portNo = Integer.parseInt("8080");
    	String fileName = "hi.txt";*/
    	
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        long startTime = System.currentTimeMillis();
        Socket clientSocket = new Socket(serverIP, portNo);
        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        sentence = "GET "+fileName+" http/1.1 \n content-type:text/html\r\n";   // Client request to be sent to the server
		outToServer.writeObject(sentence + '\n');    // Sending client request to server
		outToServer.flush();
		
        modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence.toString());
        
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Calculating and displaying the round trip time-(RTT)");
        System.out.println("RTT = "+totalTime+"ns");
        clientSocket.close();
    }
}