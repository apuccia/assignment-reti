package assignment8.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;

/*
	Autore: Alessandro Puccia
	Matricola: 547462
	Corso A
*/

public class PingClient implements Runnable{
	private DatagramSocket clientSocket;
	private final PrintWriter clientPrintWriter;
	private final int serverPort;
	private final String serverAddress;
	private final static int BUFFER_SIZE = 100, TIMEOUT = 2000;
	
	public PingClient(String serverAddress, int serverPort) {
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			clientSocket = null;
		}
		
		clientPrintWriter = new PrintWriter(System.out, true);
		this.serverPort = serverPort;
		this.serverAddress = serverAddress;
	}
	
	private DatagramPacket receivePacket() {
		byte[] buffer = new byte[BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
		
		try {
			//setto il timeout massimo di attesa per il client.
			clientSocket.setSoTimeout(TIMEOUT);
			clientSocket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			packet = null;
			
		}
		
		return packet;
	}
	
	private void sendPacket(String data) {
		byte[] buffer = data.getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		// setto la porta di destinazione
		packet.setPort(serverPort);
		
		InetAddress address = null;
		try {
			// setto l'indirizzo del server
			address = InetAddress.getByName(serverAddress);

			packet.setAddress(address);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (address != null) {
			try {
				clientSocket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		long minRTT = Long.MAX_VALUE, maxRTT = Long.MIN_VALUE;
		float avgRTT = 0;
		int packetReceived = 0;
		
		for (int i = 0; i < 10; i++) {
			Long timestampSend, timestampReceive;
			long curRTT;
			
			// ottengo il timestamp in ms poco prima di inviare il pacchetto.
			timestampSend = ZonedDateTime.now().toInstant().toEpochMilli();
			sendPacket("PING " + i + " " + timestampSend.toString());
			
			// ricevo l'eco del pacchetto (bloccante).
			DatagramPacket packet = receivePacket();
			
			// ottengo il timestamp in ms di ricezione o perdita del pacchetto.
			timestampReceive = ZonedDateTime.now().toInstant().toEpochMilli();
			
			String response;
			if (packet != null) {
				// caso pacchetto ricevuto.
				
				// ottengo la stima dell'RTT per quel pacchetto.
				curRTT = timestampReceive - timestampSend;
				
				if (curRTT > maxRTT) {
					// ho ricevuto un pacchetto con RTT maggiore del massimo corrente.
					maxRTT = curRTT;
				}
				else if (curRTT < minRTT) {
					// ho ricevuto un pacchetto con RTT minore del minimo corrente.
					minRTT = curRTT;
				}
				
				// aggiungo l'RTT corrente per il calcolo successivo della media.
				avgRTT += curRTT;
				
				// aggiungo alla stringa l'RTT corrente.
				response = curRTT + " ms";

				packetReceived++;
			}
			else {
				// caso pacchetto "perso".
				response = "*";
			}

			// stringa riassuntiva per il ping del pacchetto corrente.
			clientPrintWriter.println("PING " + i + " " + timestampSend.toString() + " RTT: " + response);
		}
		
		
		int percentage = 0;
		
		if (packetReceived == 0) {
			minRTT = 0;
			avgRTT = 0;
			maxRTT = 0;
		}
		else {
			// calcolo dell RTT medio.
			avgRTT /= packetReceived;
			
			// calcolo della percentuale di pacchetti persi.
			percentage = ((10 - packetReceived) * 100)/10;
		}
		
		// stampa delle statistiche finali.
		clientPrintWriter.println("---- PING Statistics ----");
		clientPrintWriter.println("10 packets transmitted, " + packetReceived + " packets received, " + percentage + "% packet loss");
		clientPrintWriter.println("round-trip (ms) min/avg/max = " + minRTT + "/" + String.format("%.2f", avgRTT) + "/" + maxRTT + "\n");
		
		clientSocket.close();
	}
}
