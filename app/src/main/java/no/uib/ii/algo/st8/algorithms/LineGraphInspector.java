package no.uib.ii.algo.st8.algorithms;

import java.util.HashMap;

import no.uib.ii.algo.st8.model.GrapherEdge;
import no.uib.ii.algo.st8.model.GrapherVertex;
import no.uib.ii.algo.st8.model.GrapherVertexFactory;
import no.uib.ii.algo.st8.util.Coordinate;
import no.uib.ii.algo.st8.util.Neighbors;

import org.jgrapht.graph.SimpleGraph;

public class LineGraphInspector extends
		Algorithm<GrapherVertex, GrapherEdge<GrapherVertex>, SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>>> {

	private final GrapherVertexFactory vertexFactory;

	public LineGraphInspector(SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> graph,
	                          GrapherVertexFactory vertexFactory) {
		super(graph);
		this.vertexFactory = vertexFactory;
	}

	@Override
	public SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> execute() {
		if (graphSize() == 0 || graphEdgeSize() == 0)
			return new SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>>(graph.getEdgeFactory());

		return constructLineGraph();
	}

	private SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> constructLineGraph() {

		SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> lg = new SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>>(
				graph.getEdgeFactory());

		HashMap<GrapherVertex, GrapherEdge<GrapherVertex>> map = new HashMap<GrapherVertex, GrapherEdge<GrapherVertex>>();

		for (GrapherEdge<GrapherVertex> e : graph.edgeSet()) {
			GrapherVertex v = getMidPoint(e.getSource(), e.getTarget());
			lg.addVertex(v);
			map.put(v, e);
		}

		for (GrapherVertex v1 : lg.vertexSet()) {
			for (GrapherVertex v2 : lg.vertexSet()) {
				if (v1 == v2 || lg.containsEdge(v1, v2))
					continue;
				GrapherEdge<GrapherVertex> e1 = map.get(v1);
				GrapherEdge<GrapherVertex> e2 = map.get(v2);

				if (Neighbors.isIncidentEdge(graph, e1, e2))
					lg.addEdge(v1, v2);
			}
		}

		return lg;
	}

	private GrapherVertex getMidPoint(GrapherVertex v1, GrapherVertex v2) {
		return vertexFactory.createVertex(getMidpoint(v1.getCoordinate(), v2.getCoordinate()));
	}

	private Coordinate getMidpoint(Coordinate c1, Coordinate c2) {
		float x1 = c1.getX();
		float y1 = c1.getY();

		float x2 = c2.getX();
		float y2 = c2.getY();

		float x = (x1 + x2) / 2;
		float y = (y1 + y2) / 2;
		return new Coordinate(x, y);
	}
}
