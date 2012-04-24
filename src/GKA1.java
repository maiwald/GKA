/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka1;

import org.jgrapht.Graph;

/**
 *
 * @author abe263
 */
public class GKA1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Graph depthFirstGraph = (new GraphLoader("test_graph1.graph")).getGraph();
        Traverser depthFirst = new DepthFirstTraverser<String>(depthFirstGraph, "Hamburg");
        System.out.println("Depth First");
        depthFirst.traverse();
        System.out.println();
        
        Graph breadthFirstGraph = (new GraphLoader("test_graph1.graph")).getGraph();
        Traverser breadthFirst = new BreadthFirstTraverser<String>(breadthFirstGraph, "Hamburg");
        System.out.println("Breadth First");
        breadthFirst.traverse();
        System.out.println();
    }
}
