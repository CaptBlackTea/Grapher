package no.uib.ii.algo.st8;

import java.util.ArrayList;
import java.util.Stack;

import no.uib.ii.algo.st8.algorithms.Neighbors;
import no.uib.ii.algo.st8.model.DefaultEdge;
import no.uib.ii.algo.st8.model.DefaultVertex;

import org.jgrapht.graph.SimpleGraph;

public class Undo {
	private final SimpleGraph<DefaultVertex, DefaultEdge<DefaultVertex>> graph;
	private final Stack<History> history = new Stack<History>();

	public Undo(SimpleGraph<DefaultVertex, DefaultEdge<DefaultVertex>> graph) {
		this.graph = graph;
	}

	/**
	 * 
	 * @return true if there were more to undo, false if at beginning of history
	 */
	public boolean undo() {
		if (history.isEmpty())
			return false;

		History h = history.pop();
		if (h.isVertex) {
			if (!h.add) {
				graph.addVertex(h.vertex);
				System.out.println("\tadding vertex " + h.vertex.getId());
				for (DefaultVertex v : h.neighbors) {
					graph.addEdge(h.vertex, v);
					System.out.println("\t\tadding edge" + h.vertex.getId()
							+ " - " + v.getId());
				}
			} else {
				graph.removeVertex(h.vertex);
				System.out.println("\tremoving vertex " + h.vertex.getId());
			}
		} else {
			if (!h.add) {
				graph.addEdge(h.vertex, h.otherVertex);
				System.out.println("\tadding edge " + h.vertex.getId() + " - "
						+ h.otherVertex.getId());
			} else {
				graph.removeEdge(h.vertex, h.otherVertex);
				System.out.println("\tremoving edge " + h.vertex.getId()
						+ " - " + h.otherVertex.getId());
			}
		}

		return true;
	}

	public boolean addVertex(DefaultVertex v) {
		addHistory(v, true);
		return graph.addVertex(v);
	}

	public boolean removeVertex(DefaultVertex v) {
		addHistory(v, false);
		return graph.removeVertex(v);
	}

	public DefaultEdge<DefaultVertex> addEdge(DefaultVertex v, DefaultVertex u) {
		addHistory(v, u, true);
		return graph.addEdge(v, u);
	}

	public DefaultEdge<DefaultVertex> removeEdge(DefaultVertex v,
			DefaultVertex u) {
		addHistory(v, u, false);
		return graph.removeEdge(v, u);
	}

	private void addHistory(DefaultVertex v, DefaultVertex u, boolean add) {
		history.push(new History(v, u, add));
	}

	private void addHistory(DefaultVertex v, boolean add) {
		history.push(new History(v, add));
	}

	private class History {
		boolean isVertex;
		DefaultVertex vertex;
		DefaultVertex otherVertex;
		ArrayList<DefaultVertex> neighbors = new ArrayList<DefaultVertex>();
		boolean add;

		public History(DefaultVertex v, boolean add) {
			isVertex = true;
			this.add = add;
			this.vertex = v;
			if (!add)
				neighbors.addAll(Neighbors.openNeighborhood(graph, v));
		}

		public History(DefaultVertex v, DefaultVertex u, boolean add) {
			isVertex = false;
			this.add = add;
			this.vertex = v;
			this.otherVertex = u;
		}

		@Override
		public String toString() {
			return (add ? "add " : "del ") + (isVertex ? "vertex" : "edge");
		}
	}

}