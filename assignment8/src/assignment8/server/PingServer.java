package assignment8.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Random;

/*
	Autore: Alessandro Puccia
	Matricola: 547462
	Corso A
*/

public class PingServer implements Runnable {
	private final int BUFFER_SIZE = 100;
	private final Random lossProb;
	private final PrintWriter serverPrintWriter;
	private Selector selector;
	private DatagramChannel serverChannel;
	
	public PingServer(int port, long seed) {
		
		try {
			selector = Selector.open();
			
			serverChannel = DatagramChannel.open();
			serverChannel.socket().bind(new InetSocketAddress(port));
			serverChannel.configureBlocking(false);
			
			SelectionKey clientKey = serverChannel.register(selector, SelectionKey.OP_READ);
			clientKey.attach(ByteBuffer.allocate(BUFFER_SIZE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		serverPrintWriter = new PrintWriter(System.out, true);
		if (seed == 0) {
			// se il seed non è stato passato 
			lossProb = new Random();
		} else {
			lossProb = new Random(seed);
		}
	}
	
	@Override
	public void run() {
		Boolean loss = false;
		SocketAddress address = null;
		String data = null, action = null;
		long sleepTime = 0;
		int nKeys = 0;
		
		while (true) {
			try {
				// attendo per un massimo di 10 secondi
				nKeys = selector.select(10000);
		
				if (nKeys == 0) {
					// non ho ricevuto nulla, esco
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
			while (selectedKeys.hasNext()) {
				
				SelectionKey key = selectedKeys.next();
				selectedKeys.remove();
					
				ByteBuffer buffer = (ByteBuffer)key.attachment();
					
				if (!key.isValid()) {
					break;
				}
				else if (key.isReadable()) {
					try {
						// ottengo il dato + indirizzo mittente
						address = serverChannel.receive(buffer);
						
						// leggo dal buffer e lo riposiziono
						data = new String(buffer.array(), 0, buffer.position());
						buffer.clear();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
							
						try {
							serverChannel.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					// scelta random di "perdita" del pacchetto
					loss = lossProb.nextInt(4) <= 1;
					if (loss) {
						sleepTime = 0;
						action = "not sent";
					}
					else {
						key.interestOps(SelectionKey.OP_WRITE);
							
						// scelta random di delay
						sleepTime = lossProb.nextInt(400);
						action = "delayed " + sleepTime + " ms";
					}
						
					serverPrintWriter.println(address.toString() + "> " + data + " ACTION: " + action);
				}
				else if (key.isWritable()) {
					try {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// rimando il dato inviato dal client e contenuto ancora nel buffer
						serverChannel.send(buffer, address);

						buffer.clear();
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					key.interestOps(SelectionKey.OP_READ);
				}
			}
		}
		
		try {
			selector.close();
			serverChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

