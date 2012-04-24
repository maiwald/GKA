/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka;

import org.jgrapht.Graph;

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
        Traverser depthFirst = new DepthFirstTraverser<String>(depthFirstGraph, "Hamburg");
        System.out.println("Depth First");
        depthFirst.traverse();
        System.out.println();
    }

    public static void aufgabe_2() {
        Graph g = (new GraphLoader("data/graph_01.graph.txt")).getGraph();
        ShortestPathDijkstra sp = new ShortestPathDijkstra<String>(g);
        System.out.println(sp.getShortestPath("Hamburg", "München"));
        System.out.println(sp.counter);
    }
}
