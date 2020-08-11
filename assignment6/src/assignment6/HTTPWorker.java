package assignment6;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;

/*
	Autore: Alessandro Puccia
	Matricola: 547462 
	Corso A
*/

public class HTTPWorker implements Runnable {
	private final Socket sock;
	
	public HTTPWorker(Socket sock) {
		this.sock = sock;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		PrintWriter workerPrinter = new PrintWriter(System.out, true);
		
		try (BufferedOutputStream outputStream = new BufferedOutputStream(sock.getOutputStream());
			 BufferedReader inputReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));) {
			
			// leggo la prima riga della richiesta HTTP e la tokenizzo
			String HTTPRequest = inputReader.readLine();
			String[] requestChunks = HTTPRequest.split(" ");
			StringBuilder HTTPResponseHeader = new StringBuilder();
			
			workerPrinter.println("Richiesta HTTP:\n" + HTTPRequest + "\n");
			
			try {
				File file;
				file = new File("C:\\Users\\aless\\Desktop" + requestChunks[1]);
				
				if (requestChunks[0].compareTo("GET") != 0) {
					// se il metodo HTTP non è get 
					HTTPResponseHeader.append("HTTP/1.1 501 Not Implemented\r\n");
					workerPrinter.println("Risposta HTTP metodo non implementato:\n" + HTTPResponseHeader + "\n");
					
					outputStream.write(HTTPResponseHeader.toString().getBytes());
				}
				else if (!file.exists()) {
					// oppure il file non esiste allora mando errore
					HTTPResponseHeader.append("HTTP/1.1 404 Not Found\r\n");
					workerPrinter.println("Risposta HTTP risorsa non trovata:\n" + HTTPResponseHeader + "\n");
					
					outputStream.write(HTTPResponseHeader.toString().getBytes());
				}
				else {
					// richiesta corretta
					HTTPResponseHeader.append(requestChunks[2] + " 200 OK\r\n");
					
					String contentType = contentTypeFinder(requestChunks[1]);
					
					if (contentType != null) {
						HTTPResponseHeader.append("Content-Type: " + contentTypeFinder(requestChunks[1]) + "\r\n\n");
					}
					
					workerPrinter.println("Risposta HTTP OK:\n" + HTTPResponseHeader + "\n");
					
					outputStream.write(HTTPResponseHeader.toString().getBytes());
					Files.copy(file.toPath(), outputStream);
				}
				
				outputStream.flush();
			} catch(IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			sock.close();
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		workerPrinter.println("Richiesta completata\n");
	}
	
	String contentTypeFinder(String name) {
		String contentType, extension;
		
		extension = name.substring(name.lastIndexOf(".") + 1);
		switch (extension) {
			case "jpg":
			case "jpeg": {
				// standard MIME è jpeg
				contentType = "image/jpeg";
				break;
			}
			case "png":
			case "gif": {
				contentType = "image/" + extension;
				break;
			}
			case "mp4": {
				contentType = "video/" + extension;
				break;
			}
			default:
				contentType = null;
		}
		
		return contentType;
	}
}
