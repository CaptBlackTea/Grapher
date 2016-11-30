package no.uib.ii.algo.st8;

import java.util.ArrayList;
import java.util.Stack;

import no.uib.ii.algo.st8.model.GrapherEdge;
import no.uib.ii.algo.st8.model.GrapherVertex;
import no.uib.ii.algo.st8.util.Neighbors;

import org.jgrapht.graph.SimpleGraph;

public class Undo {
  private SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> graph;
  private final Stack<History> history = new Stack<History>();

  private volatile boolean hasChanged = true;

  public Undo(SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> graph) {
    this.graph = graph;
  }

  public boolean graphChangedSinceLastCheck() {
    boolean x = hasChanged;
    hasChanged = false;
    return x;
  }

  public void clear(SimpleGraph<GrapherVertex, GrapherEdge<GrapherVertex>> graph) {

    System.out.println("Clearing memory ... ");

    this.graph = graph;
    hasChanged = false;
    history.clear();

    System.out.println("done");
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
        for (GrapherVertex v : h.neighbors) {
          graph.addEdge(h.vertex, v);
          System.out.println("\t\tadding edge" + h.vertex.getId() + " - " + v.getId());
        }
      } else {
        graph.removeVertex(h.vertex);
        System.out.println("\tremoving vertex " + h.vertex.getId());
      }
    } else {
      if (!h.add) {
        graph.addEdge(h.vertex, h.otherVertex);
        System.out.println("\tadding edge " + h.vertex.getId() + " - " + h.otherVertex.getId());
      } else {
        graph.removeEdge(h.vertex, h.otherVertex);
        System.out.println("\tremoving edge " + h.vertex.getId() + " - " + h.otherVertex.getId());
      }
    }
    hasChanged = true;
    return true;
  }

  public boolean addVertex(GrapherVertex v) {
    addHistory(v, true);
    return graph.addVertex(v);
  }

  public boolean removeVertex(GrapherVertex v) {
    addHistory(v, false);
    return graph.removeVertex(v);
  }

  public GrapherEdge<GrapherVertex> addEdge(GrapherVertex v, GrapherVertex u) {
    addHistory(v, u, true);
    return graph.addEdge(v, u);
  }

  public GrapherEdge<GrapherVertex> addEdge(GrapherEdge<GrapherVertex> e) {
    return addEdge(e.getSource(), e.getTarget());
  }

  public GrapherEdge<GrapherVertex> removeEdge(GrapherVertex v, GrapherVertex u) {
    addHistory(v, u, false);
    return graph.removeEdge(v, u);
  }

  private void addHistory(GrapherVertex v, GrapherVertex u, boolean add) {
    hasChanged = true;
    history.push(new History(v, u, add));
  }

  private void addHistory(GrapherVertex v, boolean add) {
    hasChanged = true;
    history.push(new History(v, add));
  }

  private class History {
    boolean isVertex;
    GrapherVertex vertex;
    GrapherVertex otherVertex;
    ArrayList<GrapherVertex> neighbors = new ArrayList<GrapherVertex>();
    boolean add;

    public History(GrapherVertex v, boolean add) {
      isVertex = true;
      this.add = add;
      this.vertex = v;
      if (!add)
        neighbors.addAll(Neighbors.openNeighborhood(graph, v));
    }

    public History(GrapherVertex v, GrapherVertex u, boolean add) {
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
