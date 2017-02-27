

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Calendar;

public final class ServerSide {
	
	public static ServerSocket serverSocket;
	// Set port number
	public static int portno = 8080;
	
	public static void main(String argv[]) throws Exception {
		try {
		// Establish the listening socket
		serverSocket = new ServerSocket(portno);
		System.out.println("Extracting and displaying the connection- Port number.....");
		System.out.println("Port number =  " + serverSocket.getLocalPort());
		} catch (IOException e) {
		    e.printStackTrace();
		    System.exit(1);
		}
		// Wait for and process HTTP service requests
		while (true) {
			// Wait for TCP connection
			Socket requestSocket = serverSocket.accept();

			// Create an object to handle the request
			HttpRequest request = new HttpRequest(requestSocket);
			//request.run();

			// Create a new thread for the request
			Thread thread = new Thread(request);

			// Start the thread
			thread.start();
			
			//requestSocket.close();
		}
	}
}

final class HttpRequest implements Runnable {
	// Constants
	// Recognized HTTP methods
	public static class HTTP_METHOD {
		public static String HEAD = "HEAD";
		public static String POST = "POST";
		public static String GET = "GET";
		
	}

	public static String WEB_ROOT = System.getProperty("user.dir") + File.separator  + "webroot";
	public static String HTTPVERSION = "HTTP/1.0 200 OK";
	public static String CRLF = "\r\n";
	public static String SERVERNAME = "Server: MyServer";
	public static int BUFFER_SIZE = 1024;
	public static Socket socket;
	public static String uri;
	

	// Constructor
	@SuppressWarnings("static-access")
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

	// Implements the run() method of the Runnable interface
	public void run() {
		try {
			processRequest();
			processResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Process a HTTP request
	public static void processRequest() throws Exception {
		try {
			System.out.println("##########REQUEST############");
			System.out.println(Calendar.getInstance().getTime()+"|Creating Request....");
			// Read a set of characters from the socket
			InputStream input = socket.getInputStream();
		    StringBuffer requestBuffer = new StringBuffer(2048);
		    int i;
		    byte[] buffer = new byte[BUFFER_SIZE];
		    try {
		      i = input.read(buffer);
		    }
		    catch (IOException e) {
		      e.printStackTrace();
		      i = -1;
		    }
		    for (int j=0; j<i; j++) {
		    	requestBuffer.append((char) buffer[j]);
		    }
		    System.out.print(requestBuffer.toString());
		    uri = parseUri(requestBuffer.toString());
		} catch (SocketException sockExp) {
			sockExp.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String parseUri(String requestString) {
	    int index1, index2;
	    index1 = requestString.indexOf(' ');	
	    if (index1 != -1) {
	      index2 = requestString.indexOf(' ', index1 + 1);
	      if (index2 > index1)
	        return requestString.substring(index1 + 1, index2);
	    }
	    return null;
	  }

	  public static String getUri() {
	    return uri;
	  }
	  
	  public static void processResponse() throws Exception {
		  System.out.println("Displaying connections");
		  System.out.println("###############RESPONSE######################");
		  System.out.println(Calendar.getInstance().getTime()+"|Creating & Sending Response....");
		  OutputStream output = socket.getOutputStream();
		  String headerString = "HTTP/1.1 200 OK \r\n" +
		          "Content-Type: text/html\r\n" +
		          "Content-Length: 23\r\n" +
		          "\r\n";
		  //output.write(headerString.getBytes());
		  System.out.println("brinda " + WEB_ROOT);
		  byte[] bytes = new byte[BUFFER_SIZE];
		    FileInputStream fis = null;
		    try {
		      File file = new File(WEB_ROOT, getUri());
		      System.out.println("brinda fp " + file.getAbsolutePath());
		      if (file.exists()) {
		    	  output.write(headerString.getBytes());
		        fis = new FileInputStream(file);
		        int ch = fis.read(bytes, 0, BUFFER_SIZE);
		        while (ch!=-1) {
		          output.write(bytes, 0, ch);
		          ch = fis.read(bytes, 0, BUFFER_SIZE);
		        }
		      }
		      else {
		        // file not found
		        String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
		          "Content-Type: text/html\r\n" +
		          "Content-Length: 23\r\n" +
		          "\r\n" +
		          "<h1>File Not Found</h1>";
		        output.write(errorMessage.getBytes());
		      }
		      System.out.println(Calendar.getInstance().getTime()+"|----Response Sent----");
		    }
		    catch (NullPointerException nullExp) {
		        // thrown if cannot instantiate a File object
		    	//output.write(nullExp.getMessage().getBytes());
		    	nullExp.printStackTrace();
		      }
		    catch (Exception e) {
		      // thrown if cannot instantiate a File object
		      e.printStackTrace();
		    }
		    finally {
		      if (fis!=null)
		        fis.close();
		      output.flush();
		      output.close();
		      //socket.close();
		    }
	  }
	
	/*private static String contentType(String fileName) {
		if (fileName.toLowerCase().endsWith(".htm") || fileName.toLowerCase().endsWith(".html")) {
			return "text/html";
		} else if (fileName.toLowerCase().endsWith(".gif")) {
			return "image/gif";
		} else if (fileName.toLowerCase().endsWith(".jpg")) {
			return "image/jpeg";
		} else {
			return "application/octet-stream";
		}
	}*/
}