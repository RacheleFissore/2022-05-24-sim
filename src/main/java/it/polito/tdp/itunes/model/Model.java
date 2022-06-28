package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private ItunesDAO dao;
	private Graph<Track, DefaultWeightedEdge> grafo;
	private Map<Integer, Track> idMap;
	private List<Adiacenza> archi;
	private List<Track> best;
	
	public Model() {
		dao = new ItunesDAO();
		idMap = new HashMap<>();
		
		for(Track track : dao.getAllTracks()) {
			idMap.put(track.getTrackId(), track);
		}
	}
	
	public void creaGrafo(Genre genere) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		archi = new ArrayList<>();
		Graphs.addAllVertices(grafo, dao.getVertici(genere));
		archi = dao.getArchi(genere, idMap);
		
		for(Adiacenza adiacenza : archi) {
			Graphs.addEdgeWithVertices(grafo, adiacenza.getVertice1(), adiacenza.getVertice2(), adiacenza.getPeso());
		}
	}
	
	public int nVertici() {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}
	
	public Set<Track> getVertici() {
		return grafo.vertexSet();
	}
	
	public List<Genre> getGeneri() {
		return dao.getAllGenres();
	}
	
	public String stampaMassimo() {
		String stampa = "";
		Collections.sort(archi);
		int pesoMax = archi.get(0).getPeso();
		List<Adiacenza> result = new ArrayList<>();
		
		for(Adiacenza adiacenza : archi) {
			if(adiacenza.getPeso() == pesoMax) {
				result.add(adiacenza);
			}
		}
		
		for(Adiacenza adiacenza : result) {
			stampa += adiacenza + "\n";
		}
		
		return stampa;
	}
	
	public String trovaSequenza(Track partenza, int memoria) {
		String stampa = "";
		best = new ArrayList<>();
		List<Track> parziale = new ArrayList<>();
		
		ConnectivityInspector<Track, DefaultWeightedEdge> ci = new ConnectivityInspector<>(grafo);
		List<Track> raggiungibiList = new ArrayList<>(ci.connectedSetOf(partenza));
		raggiungibiList.remove(partenza);
		
		cerca(parziale, raggiungibiList, partenza.getBytes(), memoria);
		stampa += partenza.getName() + "\n";
		
		for(Track track : best) {
			stampa += track.getName() + "\n";
		}
		return stampa;
	}

	private void cerca(List<Track> parziale, List<Track> raggiungibiList, int bytes, int memoria) {
		if(bytes > memoria) {
			return;
		}
		
		if(parziale.size() > best.size()) {
			best = new ArrayList<>(parziale);
		}
		
		for(Track track : raggiungibiList) {
			if(!parziale.contains(track)) {
				bytes += track.getBytes();
				parziale.add(track);
				cerca(parziale, raggiungibiList, bytes, memoria);
				parziale.remove(track);
				bytes -= track.getBytes();
			}
		}
	}
	
	
}
