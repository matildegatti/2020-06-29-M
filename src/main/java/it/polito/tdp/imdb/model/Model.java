package it.polito.tdp.imdb.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private SimpleWeightedGraph<Director, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer,Director> idMap;
	private List<Director> percorsoMigliore;
	private int lunghezzaMax;
	
	public Model() {
		dao=new ImdbDAO();
		idMap=new HashMap<>();
		
		this.dao.listAllDirectors(idMap);
	}

	public void creaGrafo(Integer anno) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, this.dao.getVertici(idMap, anno));
		
		for(Adiacenze a:this.dao.getAdiacenze(idMap, anno)) {
			Graphs.addEdge(grafo, a.getD1(),a.getD2(), a.getPeso());
		}
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public boolean getGrafo() {
		if(grafo==null)
			return false;
		return true;
	}
	
	public Set<Director> getVertici(){
		return this.grafo.vertexSet();
	}

	public List<Vicini> registiAdiacenti(Director direttore) {
		List<Vicini> adiacenti=new LinkedList<>();
		
		for(Director d:Graphs.neighborListOf(grafo, direttore)) {
			DefaultWeightedEdge e=this.grafo.getEdge(d, direttore);
			double peso=this.grafo.getEdgeWeight(e);
			adiacenti.add(new Vicini(d,peso));
		}
		
		Collections.sort(adiacenti,new Comparator<Vicini>() {
			@Override
			public int compare(Vicini a0, Vicini a1) {
				return Double.compare(a1.getPeso(), a0.getPeso());
			}
		});
	
		return adiacenti;
	}

	public List<Director> ricorsione(int x, Director direttore) {
		this.percorsoMigliore=new LinkedList<>();
		lunghezzaMax=Integer.MIN_VALUE;
		
		List<Director> parziale=new LinkedList<>();
		parziale.add(direttore);
		
		cerca(parziale,direttore,x);
		return this.percorsoMigliore;
	}

	private void cerca(List<Director> parziale, Director direttore, int x) {
		if(Graphs.neighborListOf(grafo, direttore).size()==0) {
			if(parziale.size()>lunghezzaMax) {
				this.percorsoMigliore=new LinkedList<>(parziale);
				this.lunghezzaMax=parziale.size();
			}
			return;
		}
		
		for(Director d:Graphs.neighborListOf(grafo, direttore)) {
			if(!parziale.contains(d)) {
				parziale.add(d);
				double peso=this.calcolaPeso(parziale);
				if(peso<x) {
					cerca(parziale,d,x);
					parziale.remove(parziale.size()-1);
				}
				else if(peso==x){
					if(parziale.size()>lunghezzaMax) {
						this.percorsoMigliore=new LinkedList<>(parziale);
						this.lunghezzaMax=parziale.size();
					}
					return;
				}
				else {
					parziale.remove(parziale.size()-1);
					if(parziale.size()>lunghezzaMax) {
						this.percorsoMigliore=new LinkedList<>(parziale);
						this.lunghezzaMax=parziale.size();
					}
					return;
				}	
			}
		}
	}
	
	private Integer calcolaPeso(List<Director> parziale) {
		int peso=0;
		int i=0; //indice che mi serve per prendere il match successivo in parziale
		if(parziale.size()>0) {
		for (Director m : parziale) {
			if (i==(parziale.size()-1)) 
				break;
			DefaultWeightedEdge e = grafo.getEdge(m, parziale.get(i+1));
			i++;
			peso += grafo.getEdgeWeight(e);
		}
		}
		return peso;
	}
	
	public int attoriCondivisi() {
		return this.calcolaPeso(percorsoMigliore);
	}
}
