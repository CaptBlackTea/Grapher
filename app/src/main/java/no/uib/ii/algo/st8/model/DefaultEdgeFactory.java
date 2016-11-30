package no.uib.ii.algo.st8.model;

import java.io.Serializable;

import org.jgrapht.EdgeFactory;

public class DefaultEdgeFactory<V> implements EdgeFactory<V, GrapherEdge<V>>,
		Serializable {
	private static final long serialVersionUID = 1L;
	
	public GrapherEdge<V> createEdge(V source, V target) {
		return new DefaultEdge<V>(source, target);
	}
}
