package assignment7.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/*
 * Autore: Alessandro Puccia
 * Matricola: 547462
 * Corso A
 */

public class MainClass {
	public static int PORT = 6789;
	public static int BUFF_SIZE = 1024;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SocketChannel client;
		try {
			client = SocketChannel.open(new InetSocketAddress(PORT));
			ByteBuffer buffer = ByteBuffer.allocate(BUFF_SIZE);
		
			// preparo la stringa da mandare al server.
			String toSend = args[0];
			byte[] stringBytes = toSend.getBytes();
			
			buffer.put(stringBytes);
			buffer.flip();
			client.write(buffer);
			
			buffer.clear();
			System.out.println("Il client ha scritto: " + toSend);
			
			// caso in cui la risposta del server sia maggiore della dimensione del buffer.
			StringBuilder builder = new StringBuilder();
			int bytesOff = 0, max = 0;
			while ((max = client.read(buffer)) != -1) {
				builder.append(new String(buffer.array(), bytesOff, max));
				bytesOff += max;
				buffer.clear();
			}
			
			System.out.println("Il client ha letto: " + builder.toString());
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
