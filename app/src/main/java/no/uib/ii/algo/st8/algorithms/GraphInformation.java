package no.uib.ii.algo.st8.algorithms;

import no.uib.ii.algo.st8.algorithms.RegularityInspector.StronglyRegularWitness;
import no.uib.ii.algo.st8.interval.SimpleToBasicWrapper;
import no.uib.ii.algo.st8.model.GrapherEdge;
import no.uib.ii.algo.st8.model.GrapherVertex;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.SimpleGraph;

public class GraphInformation {

  public static String graphInfo(SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> graph) {

    int vertexCount = graph.vertexSet().size();
    if (vertexCount == 0) {
      return "The empty graph";
    }
    int edgeCount = graph.edgeSet().size();
    if (edgeCount == 0) {
      if (vertexCount == 1) {
        return "K1";
      } else {
        return "The trivial graph on " + vertexCount + " vertices";
      }
    }

    ConnectivityInspector<GrapherVertex, GrapherEdge<GrapherVertex>> inspector = new ConnectivityInspector<GrapherVertex, GrapherEdge<GrapherVertex>>(
        graph);

    boolean isConnected = inspector.isGraphConnected();
    int nc = 1;
    if (!isConnected) {
      nc = inspector.connectedSets().size();
    }

    boolean acyclic = GirthInspector.isAcyclic(graph);
    boolean isChordal = SimplicialInspector.isChordal(graph);

    boolean isInterval = (new SimpleToBasicWrapper<GrapherVertex, GrapherEdge<GrapherVertex>>(graph)).getIntervalGraph() != null;

    int maxDegree = maxDegree(graph);
    int minDegree = minDegree(graph);
    String s = "";
    if (isInterval) {
      s += "Interval: ";
    } else if (isChordal) {
      s += "Chordal: ";
    }
    if (isConnected) {
      s += (acyclic ? "Tree" : "Connected graph");
    } else {
      s += (acyclic ? "Forest" : "Disconnected graph");
      s += " (" + nc + " components)";
    }
    s += " on " + vertexCount + " vertices";
    s += " and " + edgeCount + " edges.";
    if (maxDegree == minDegree) {
      if (maxDegree == vertexCount - 1) {
        s += " Complete, K_" + vertexCount;
      } else {
        if (maxDegree == 2) {
          // this is a disjoint union of cycles
          if (isConnected)
            s += " Cycle, C_" + vertexCount + ",";
          else
            s += " union of cycles,";
        }
        StronglyRegularWitness srw = new RegularityInspector<GrapherVertex, GrapherEdge<GrapherVertex>>(graph).isStronglyRegular();
        if (srw != null) {
          // graph is strongly regular!
          s += " " + srw;
        } else {
          s += " " + maxDegree + "-regular";
        }
      }
    } else {
      s += " Max degree " + maxDegree + ", min degree " + minDegree;
    }
    return s;
  }

  public static int maxDegree(SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> graph) {
    // TODO What to do on empty graphs?

    int d = 0;

    for (GrapherVertex v : graph.vertexSet()) {
      d = Math.max(d, graph.degreeOf(v));
    }

    return d;
  }

  public static int minDegree(SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> graph) {
    // TODO What to do on empty graphs?
    int d = graph.vertexSet().size();

    for (GrapherVertex v : graph.vertexSet()) {
      d = Math.min(d, graph.degreeOf(v));
    }

    return d;
  }
}
