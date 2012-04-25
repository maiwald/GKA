/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka;

import gka.pathfinders.Dijkstra;
import gka.pathfinders.FloydWarshall;
import gka.traversers.DepthFirst;
import gka.traversers.Traverser;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author abe263
 */
public class GKA {

    public static void main(String[] args) {
        aufgabe_2();
    }

    public static void aufgabe_1() {
        Graph depthFirstGraph = (new GraphLoader("data/graph_01.graph.txt")).getGraph();
        Traverser depthFirst = new DepthFirst<String>(depthFirstGraph, "Hamburg");
        System.out.println("Depth First");
        depthFirst.traverse();
        System.out.println();
    }

    public static void aufgabe_2() {
        Graph g = (new GraphLoader("data/graph_04.graph.txt")).getGraph();

        Dijkstra sp_dijkstra = new Dijkstra<String>(g, "v2");
        System.out.println(sp_dijkstra.getShortestPathToTarget("v6"));
        System.out.println(sp_dijkstra.counter);

        FloydWarshall sp_warshall = new FloydWarshall<String>(g);
        System.out.println(sp_warshall.getShortestPath("v2", "v6"));
        System.out.println(sp_warshall.counter);
        
        sp_warshall.printDMatrix();
        sp_warshall.printTMatrix();
    }
}
