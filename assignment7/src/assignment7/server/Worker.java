package assignment7.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/*
 * Autore: Alessandro Puccia
 * Matricola: 547462
 * Corso A
 */

public class Worker implements Runnable {
	private ServerSocketChannel serverChannel;
	private Selector selector;
	private ServerSocket serverSocket;
	private int numRequests;

	private final static int PORT = 6789;
	private final static int BUFF_SIZE = 1024;
	private final static String SERVER_RESP = " echoed by server";
	private final PrintWriter workerPrintWriter = new PrintWriter(System.out, true);
	
	public Worker(int numRequests) {
		try {
			serverChannel = ServerSocketChannel.open();
			serverSocket = serverChannel.socket();
			
			serverSocket.bind(new InetSocketAddress(PORT));
			serverChannel.configureBlocking(false);
			
			selector = Selector.open();
			
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			this.numRequests = numRequests;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (numRequests > 0) {
			try {
				selector.select();
				
				Set <SelectionKey> readyKeys = selector.selectedKeys();
				Iterator <SelectionKey> iterator = readyKeys.iterator();
				
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					
					if (key.isAcceptable()) {
						// mi metto in attesa dei client.
						SocketChannel clientChannel = serverChannel.accept();
						workerPrintWriter.println("Nuova richiesta di connessione!");

						clientChannel.configureBlocking(false);
						
						// mi registro sulle operazioni di lettura e scrittura.
						SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, null);
						
						// alloco il buffer e lo allego alla key.
						ByteBuffer buffer = ByteBuffer.allocate(BUFF_SIZE);
						clientKey.attach(buffer);
					}
					else if (key.isReadable()) {
						StringBuilder builder = new StringBuilder();
						
						// recupero il buffer e il canale dalla key.
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						SocketChannel clientChannel = (SocketChannel) key.channel();
						
						// leggo dal canale.
						clientChannel.read(buffer);
						
						if (key.isWritable()) {
							// preparo la risposta del server aggiungendo echoed by server.
							builder.append(new String(buffer.array(), 0, buffer.position()));
							workerPrintWriter.println("Il server ha letto: " + builder.toString());
							builder.append(SERVER_RESP);

							byte[] stringBytes = builder.toString().getBytes();

							buffer.clear();
							
							// scrivo sul canale, prevedo il caso in cui aggiungendo la risposta del server vado oltre la dimensione del buffer
							int bytesOff = 0, max = 0;
							while (bytesOff < stringBytes.length) {
								max = Math.min(buffer.capacity(), stringBytes.length - bytesOff);
								
								buffer.put(stringBytes, bytesOff, max);
								bytesOff += max;
								
								buffer.flip();
								while (buffer.hasRemaining()) {
									clientChannel.write(buffer);
								}
								
								buffer.clear();
							}
							
							workerPrintWriter.println("Il server ha scritto un totale di " + bytesOff + " bytes");
						}
						key.cancel();
						clientChannel.close();

						numRequests--;
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
