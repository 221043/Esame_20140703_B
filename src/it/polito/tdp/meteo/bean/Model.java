package it.polito.tdp.meteo.bean;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private double minCosto;
	private List<String> citta;
	private Month m;
	private Map<CittaData, Double> leUmidita;
	private Map<Integer, String> viaggioMigliore;
	
	private static final int MAX_VISITE = 4;
//	private static final int MAX_VISITE = 12;
	private static final double C = 100.0;
	private static final double K = 100.0;
//	private static final double K = 1.0;
	private static final int YEAR = 2013;
	
	public List<UmiditaCitta> getUmiditaMedia(Month m){
		MeteoDAO dao = new MeteoDAO();
		return dao.getUmiditaMedia(m);
	}
	
	public Map<Integer, String> ricercaViaggio(Month m){
		this.m = m;
		minCosto = Double.MAX_VALUE;
		Map<Integer, String> viaggio = new HashMap<>();
		LocalDate primoGiorno = LocalDate.of(YEAR, m, 1);
		LocalDate ultimoGiorno = primoGiorno.with(TemporalAdjusters.lastDayOfMonth());
		MeteoDAO dao = new MeteoDAO();
		citta = dao.getElencoCitta();
		leUmidita = dao.getElencoUmidita();
		this.viaggioMigliore = null;
		ricerca(viaggio, 1, 10);
//		ricerca(viaggio, 1, ultimoGiorno.getDayOfMonth());
		return this.viaggioMigliore;
	}
	
	private void ricerca(Map<Integer, String> viaggio, int step, int ldom){
		if(step==ldom+1){
			double costo = calcolaCosto(viaggio, ldom);
			if(costo<minCosto){
				minCosto = costo;
				viaggioMigliore = new HashMap<>(viaggio);
			}
			return;
		}
		for(String c:citta){
			if(numVisite(c, viaggio)<MAX_VISITE){
				viaggio.put(step, c);
				ricerca(viaggio, step+1, ldom);
				viaggio.remove(step);
			}
		}
	}

	private int numVisite(String c, Map<Integer, String> viaggio) {
		int cnt=0;
		for(String s:viaggio.values()){
			if(s.equals(c))
				cnt++;
		}
		return cnt;
	}

	private double calcolaCosto(Map<Integer, String> viaggio, int ldom) {
		double costo=0.0;
		String cittaIeri=null;
		for(int giorno=1; giorno<=ldom; giorno++){
			String cittaOggi = viaggio.get(giorno);
			if(!cittaOggi.equals(cittaIeri))
				costo += C;
			cittaIeri = cittaOggi;
			LocalDate oggi = LocalDate.of(YEAR, this.m, giorno);
			Double um = leUmidita.get(new CittaData(cittaOggi, oggi));
			if(um!=null)
				costo += K*um;
			else
				costo += K*100.0;
		}
		return 0;
	}
	
}
