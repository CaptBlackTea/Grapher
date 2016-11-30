package no.uib.ii.algo.st8.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import no.uib.ii.algo.st8.GraphViewController;
import no.uib.ii.algo.st8.model.GrapherEdge;
import no.uib.ii.algo.st8.model.GrapherVertex;

import org.jgrapht.graph.SimpleGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileAccess {

	public String save(
			SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> graph)
			throws JSONException {
		JSONObject json = new JSONObject();
		JSONObject vertices = new JSONObject();
		json.put("vertices", vertices);

		JSONArray edges = new JSONArray();
		json.put("edges", edges);

		Integer vertexNumber = 1;

		for (GrapherVertex v : graph.vertexSet()) {
			JSONObject vertexJson = new JSONObject();
			vertexJson.put("x", (double) v.getCoordinate().getX());
			vertexJson.put("y", (double) v.getCoordinate().getY());
			if (v.getLabel().isEmpty() || vertices.has(v.getLabel())) {
				v.setLabel("n" + vertexNumber);
			}
			vertices.put(v.getLabel(), vertexJson);
			vertexNumber += 1;
		}
		for (GrapherEdge<GrapherVertex> e : graph.edgeSet()) {
			JSONObject edge = new JSONObject();
			edge.put("source", e.getSource().getLabel());
			edge.put("target", e.getTarget().getLabel());
			edges.put(edge);
		}
		return json.toString();
	}

	public void load(GraphViewController controller,
			String json) throws JSONException {
		SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> graph = controller.getGraph();
		JSONObject graphJson = new JSONObject(json);
		HashSet<GrapherVertex> verticesSet = new HashSet<GrapherVertex>(
				graph.vertexSet());
		HashSet<GrapherEdge<GrapherVertex>> edgesSet = new HashSet<GrapherEdge<GrapherVertex>>(
				graph.edgeSet());
		graph.removeAllVertices(verticesSet);
		graph.removeAllEdges(edgesSet);

		JSONObject vertices = graphJson.getJSONObject("vertices");
		HashMap<String, GrapherVertex> verticesMap = new HashMap<String, GrapherVertex>();
		for (@SuppressWarnings("rawtypes")
		Iterator i = vertices.keys(); i.hasNext();) {
			String key = (String) i.next();
			JSONObject vertexJson = vertices.getJSONObject(key);
			GrapherVertex vertex = controller.createVertex(new Coordinate(
					vertexJson.getDouble("x"), vertexJson.getDouble("y")));
			vertex.setLabel(key);
			graph.addVertex(vertex);
			verticesMap.put(key, vertex);
		}

		JSONArray edges = graphJson.getJSONArray("edges");

		for (int i = 0; i < edges.length(); i++) {
			JSONObject edge = edges.getJSONObject(i);
			String source = edge.getString("source");
			String target = edge.getString("target");
			graph.addEdge(verticesMap.get(source), verticesMap.get(target));
		}

	}

}
