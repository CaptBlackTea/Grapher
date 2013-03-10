package no.uib.ii.algo.st8.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.jgrapht.graph.SimpleGraph;

public class SimplicialInspector {

	public static <V, E> Collection<V> getSimplicialVertices(SimpleGraph<V, E> graph) {
		HashSet<V> simpls = new HashSet<V>();
		for (V v : graph.vertexSet()) {
			ArrayList<V> neighs = new ArrayList<V>();
			neighs.addAll(Neighbors.openNeighborhood(graph, v));

			boolean isSimplicial = true;

			for (int i = 0; i < neighs.size(); i++) {
				for (int j = i + 1; j < neighs.size(); j++) {
					if (!graph.containsEdge(neighs.get(i), neighs.get(j))) {
						isSimplicial = false;
						break;
					}
				}
				if (!isSimplicial)
					break;
			}
			if (isSimplicial)
				simpls.add(v);
		}
		return simpls;
	}
}