package assignment5;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */

public class ConsumerTask implements Runnable{
	private final ContoCorrente conto;
	private final Contatore contatore;
	private int localBonifici;
	private int localAccrediti;
	private int localBollettini;
	private int localF24;
	private int localPagoBancomat;
	
	public ConsumerTask(ContoCorrente conto, Contatore contatore) {
		this.conto = conto;
		this.contatore = contatore;
		localBonifici = localAccrediti = localBollettini = localF24 = localPagoBancomat = 0;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		for (Movimento movimento: conto.getMovimenti()) {
			String causale = movimento.getCausale().toString();
			switch(causale) {
				case "Bonifico":
					localBonifici++;
					break;
				case "Accredito":
					localAccrediti++;
					break;
				case "Bollettino":
					localBollettini++;
					break;
				case "F24":
					localF24++;
					break;
				case "PagoBancomat":
					localPagoBancomat++;
			}
		}
		
		// accedo alla risorsa solamente dopo aver contato tutti i movimenti del mio task (conto corrente).
		increaseTotal();
	}
	
	public void increaseTotal() {
		contatore.accessContatori();
		
		contatore.increaseBonifici(localBonifici);
		contatore.increaseAccrediti(localAccrediti);
		contatore.increaseBollettini(localBollettini);
		contatore.increaseF24(localF24);
		contatore.increasePagoBancomat(localPagoBancomat);
		
		String nomeCorrentista = conto.getNomeCorrentista();
		System.out.println(nomeCorrentista + " ha effettuato un totale di " + localBonifici + " bonifici.\n"
						   + nomeCorrentista + " ha effettuato un totale di " + localAccrediti + " accrediti.\n"
						   + nomeCorrentista + " ha effettuato un totale di " + localBollettini + " bollettini.\n"
						   + nomeCorrentista + " ha effettuato un totale di " + localF24 + " F24.\n" 
						   + nomeCorrentista + " ha effettuato un totale di " + localPagoBancomat + " PagoBancomat.\n");
		
		contatore.releaseContatori();
	}
}
