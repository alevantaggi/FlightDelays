package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;


public class Model {
	
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer, Airport> idMap;
	
	public Model() {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao= new ExtFlightDelaysDAO();
		this.idMap= new HashMap<>();
		this.dao.loadAllAirports(idMap);
	}
	
	public void creaGrafo(int nArilaines) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		Graphs.addAllVertices(this.grafo, this.dao.getVertici(nArilaines, idMap));
		
		List<Rotta> edges= this.dao.getRotte(idMap);
		
		for(Rotta r: edges) {
			Airport origin= r.getOrigin();
			Airport destionation= r.getDestination();
			int n= r.getTotale();
			
			if(this.grafo.vertexSet().contains(origin) && this.grafo.vertexSet().contains(destionation)) {
				DefaultWeightedEdge edge= this.grafo.getEdge(origin, destionation);
				
				if(edge!=null) {
					double weight= this.grafo.getEdgeWeight(edge);
					weight+=n;
					this.grafo.setEdgeWeight(edge, weight);
				}
				
				else {
					this.grafo.addEdge(origin, destionation);
					this.grafo.setEdgeWeight(origin, destionation, n);
				}
			}
		}
		
		System.out.println("Grafo creato");
		System.out.println("Ci sono: "+ this.grafo.vertexSet().size()+" vertici");
		System.out.println("Ci sono: "+ this.grafo.edgeSet().size()+" archi");


		
	}
	
	public List<Airport> trovaPercorso(Airport origin, Airport destination){
		// Creare iteratore e visitare tutto il grafo
		List<Airport> risultato= new ArrayList<>();		
		BreadthFirstIterator<Airport, DefaultWeightedEdge> visita= new BreadthFirstIterator<>(this.grafo, origin);

		while(visita.hasNext()) 
			visita.next();

		Airport corrente= destination;
		risultato.add(destination);
		
		DefaultWeightedEdge e= visita.getSpanningTreeEdge(corrente);
		while(e!=null) {
			Airport precedente= Graphs.getOppositeVertex(this.grafo, e, corrente);
			risultato.add(0, precedente);
			corrente= precedente;
			
			e= visita.getSpanningTreeEdge(corrente);
		}
		
		return risultato;
	}
	
	public boolean grafoIsNotEmpty() {
		return (this.grafo.vertexSet().size()>0 && this.grafo.edgeSet().size()>0);
	}

	public Set<Airport> getAereoporti(){
		return this.grafo.vertexSet();
	}
}



















