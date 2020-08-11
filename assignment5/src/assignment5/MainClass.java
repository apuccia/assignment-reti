package assignment5;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import assignment5.Movimento.Causale;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<ContoCorrente> conti;
		JsonArray jsonCorrentisti;
		
		int nConti, nCausali;
		
		nConti = Integer.parseInt(args[0]);
		nCausali = Integer.parseInt(args[1]);
		
		// genero i conti correnti.
		conti = generator(nConti, nCausali);
		
		// serializzo in json i conti correnti.
		jsonCorrentisti = createJson(conti);
		
		// scrivo il json file.
		writeJson(jsonCorrentisti);
		
		// creo il threadpool (fixedthreadpool da 4 thread).
		Pool thPool = new Pool();
		
		// creo il thread produttore e avvio.
		Producer producer = new Producer(thPool);
		Thread producerThread = new Thread(producer);
		producerThread.start();
		
		try {
			// aspetto la terminazione del produttore.
			producerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Produttore terminato");
	}
	
	public static JsonArray createJson(ArrayList<ContoCorrente> conti) {
		JsonArray jsonConti = new JsonArray();
		
		for(ContoCorrente conto: conti) {
			// creo l'oggetto JSON che rappresenta l'oggetto di tipo ContoCorrente.
			JsonObject jsonConto = new JsonObject();
			
			// aggiungo il nome del correntista.
			jsonConto.put("Nome correntista", conto.getNomeCorrentista());
			
			// creo un array JSON che andrà a contenere gli oggetti JSON che rappresentano i movimenti.
			JsonArray jsonMovimenti = new JsonArray();
			for(Movimento movimento: conto.getMovimenti()) {
				// creo l'oggetto JSON che rappresenta l'oggetto di tipo Movimento.
				JsonObject jsonMovimento = new JsonObject();
				
				// aggiungo la data.
				jsonMovimento.put("Data", movimento.getDate().toString());
				// aggiungo la causale.
				jsonMovimento.put("Causale", movimento.getCausale().toString());
				
				// aggiungo il movimento al jsonarray.
				jsonMovimenti.add(jsonMovimento);
			}
			
			// aggiungo il JSONarray contenente i movimenti al conto.
			jsonConto.put("Movimenti", jsonMovimenti);
			jsonConti.add(jsonConto);
		}
		return jsonConti;
	}
	
	public static ArrayList<ContoCorrente> generator(int nConti, int nCausali) {
		ArrayList<ContoCorrente> conti = new ArrayList<ContoCorrente>();
		for(int i = 0; i < nConti; i++) {
			conti.add(new ContoCorrente("Utente" + i));
		}
		
		// range di date da cui scegliere la data del movimento.
		long minDay = LocalDate.of(2017, 10, 29).toEpochDay();
		long maxDay = LocalDate.of(2019, 10, 29).toEpochDay();
		
		for(ContoCorrente conto: conti) {
			Causale scelte[] = new Causale[nCausali];
			
			for (int i = 0; i < nCausali; i++) {
				// scelgo in maniera casuale la causale del movimento.
				scelte[i] = Causale.values()[ThreadLocalRandom.current().nextInt(0, 5)];
				
				// scelgo in maniera casuale la data del movimento negli ultimi 2 anni.
				LocalDate date = LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(minDay, maxDay));
				conto.nuovoMovimento(new Movimento(scelte[i], date));
			}
		}
		
		return conti;
	}
	
	public static void writeJson(JsonArray jsonCorrentisti) {
		try (FileChannel writer = FileChannel.open(Paths.get("conticorrenti.json"),
				StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
		
			// formatto la stringa JSON in modo tale da renderla più leggibile e la converto in byte.
			byte[] jsonBytes = Jsoner.prettyPrint(jsonCorrentisti.toJson()).getBytes();
			
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
	
			int offset = 0, maxWrite = 0;
			while (offset < jsonBytes.length) {
				// lunghezza massima dei byte che andrò a scrivere nel file scelta tra i byte che rimangono da scrivere e la 
				// capacità del buffer.
				maxWrite = Math.min(jsonBytes.length - offset, buffer.capacity());
				buffer.put(jsonBytes, offset, maxWrite);
				
				offset += maxWrite;
				buffer.flip();
				while (buffer.hasRemaining()) {
					writer.write(buffer);
				}
				
				buffer.clear();
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
