/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka;

import gka.flows.EdmondsKarpFlow;
import gka.flows.FordFulkersonFlow;
import gka.pathfinders.Dijkstra;
import gka.pathfinders.FloydWarshall;
import gka.traversers.DepthFirst;
import gka.traversers.Traverser;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

/**
 *
 * @author abe263
 */
public class GKA
{

    public static void main(String[] args)
    {
        aufgabe_3();
    }

    public static void aufgabe_1()
    {
        Graph depthFirstGraph = (new GraphLoader("data/graph_01.graph.txt")).getGraph();
        Traverser depthFirst = new DepthFirst<String>(depthFirstGraph, "Hamburg");
        System.out.println("Depth First");
        depthFirst.traverse();
        System.out.println();
    }

    public static void aufgabe_2()
    {
        Graph g = (new GraphLoader("data/graph_05.graph.txt")).getGraph();

        Dijkstra sp_dijkstra = new Dijkstra<String>(g, "v1");
        System.out.println("## Dijkstra");
        System.out.println("Kürzester Weg: " + sp_dijkstra.getShortestPathToTarget("v9"));
        System.out.println("Kosten: " + sp_dijkstra.getlowestCostToTarget("v9"));
        System.out.println("Vergleiche:" + sp_dijkstra.counter);
        System.out.println();

        System.out.println("## Floyd-Warshall");
        FloydWarshall sp_warshall = new FloydWarshall<String>(g);
        System.out.println("Kürzester Weg: " + sp_warshall.getShortestPath("v1", "v9"));

        if (sp_warshall.isStarkZusammenhaengend())
        {
            System.out.println("der Graph ist nicht stark zusammenhängend.");
        } else
        {
            System.out.println("der Graph ist stark zusammenhängend.");
        }

        // sp_warshall.printDMatrix();
        // sp_warshall.printTMatrix();
    }

    public static void aufgabe_3()
    {
        DefaultDirectedWeightedGraph g = (DefaultDirectedWeightedGraph) (new GraphLoader("data/graph_08.graph")).getGraph();

        System.out.println("Ford Fulkerson");
        FordFulkersonFlow<String> ff = new FordFulkersonFlow(g);
        System.out.printf("%f\n", ff.getMaximumFlow("Hamburg", "München"));
        System.out.println();
        
        System.out.println("Edmonds Karp");
        FordFulkersonFlow<String> ek = new EdmondsKarpFlow(g);
        System.out.printf("%f\n", ek.getMaximumFlow("Hamburg", "München"));
    }
}
