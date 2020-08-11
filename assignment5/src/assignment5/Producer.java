package assignment5;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import assignment5.Movimento.Causale;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */

public class Producer implements Runnable {
	private final Pool thPool;
	private final Contatore contatore;
	
	public Producer(Pool thPool) {
		this.thPool = thPool;
		contatore = new Contatore();
	}
	
	private ArrayList<ContoCorrente> deserializeJson(JsonArray jsonCorrenti) {
		ArrayList<ContoCorrente> conti = new ArrayList<ContoCorrente>();
		
		for (int i = 0; i < jsonCorrenti.size(); i++) {
			JsonObject contoJson = (JsonObject) jsonCorrenti.get(i);

			// ricostruisco l'oggetto di tipo ContoCorrente.
			String nomeCorrentista = (String) contoJson.get("Nome correntista");
			
			JsonArray movimentiJson = (JsonArray) contoJson.get("Movimenti");
			
			// ricostruisco tutti gli oggetti di tipo Movimento.
			ArrayList<Movimento> movimenti = new ArrayList<Movimento>();
			for(int j = 0; j < movimentiJson.size(); j++) {
				JsonObject movimentoJson = (JsonObject) movimentiJson.get(j);
				
				// ricostruisco l'oggetto di tipo Movimento.
				LocalDate data = LocalDate.parse((String) movimentoJson.get("Data"));
				Movimento movimento = new Movimento(Causale.valueOf((String) movimentoJson.get("Causale")), data);
			
				movimenti.add(movimento);
			}
			
			// ricostruisco l'oggetto di tipo ContoCorrente.
			ContoCorrente conto = new ContoCorrente(nomeCorrentista, movimenti);
			
			// aggiungo il contocorrente ad un arraylist.
			conti.add(conto);
		}
		
		return conti;
	}
	
	private String readJson() {
		String jsonString = "";
		try(FileChannel reader = FileChannel.open(Paths.get("conticorrenti.json"),
				StandardOpenOption.READ)) {
			ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
			
			boolean stop = false;
			while(!stop) {
				int bytesRead = reader.read(buffer);
				
				if (bytesRead == -1) {
					stop = true;
				}
				// creo una stringa di lunghezza pari al numero di byte che occupano il buffer e la concateno.
				String jsonChunk = new String(buffer.array(), 0, buffer.position());
				jsonString += jsonChunk;
				
				buffer.clear();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonString;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String jsonString = readJson();
		
		JsonArray jsonCorrenti = null;
		try {
			jsonCorrenti = (JsonArray) Jsoner.deserialize(jsonString);
		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// ottengo gli oggetti ContoCorrente dal file JSON.
		ArrayList<ContoCorrente> conti = deserializeJson(jsonCorrenti);
		
		// submit del task al threadpool.
		for(ContoCorrente conto: conti) {
			thPool.insertContoCorrente(new ConsumerTask(conto, contatore));
		}
		
		// dopo aver mandato tutti i task aspetto la terminazione del threadpool.
		thPool.shutdown();
		thPool.awaitShutdown();
		System.out.println("Thread pool terminato.\n");
		
		// stampa delle statistiche totali dei movimenti.
		System.out.println("Statistiche totali");
		contatore.printTotal();
	}
}


