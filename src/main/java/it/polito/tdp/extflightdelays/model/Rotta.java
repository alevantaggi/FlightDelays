package it.polito.tdp.extflightdelays.model;

public class Rotta {
	private Airport origin;
	private Airport destination;
	private int totale;
	
	public Rotta(Airport origin, Airport destination, int totale) {
		this.origin = origin;
		this.destination = destination;
		this.totale = totale;
	}

	public Airport getOrigin() {
		return origin;
	}

	public Airport getDestination() {
		return destination;
	}

	public int getTotale() {
		return totale;
	}
	
	
	

}
