package gka;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author abe263
 */
public class DepthFirstTraverser<V> extends Traverser<V> {
    
    private List<V> visited;
    
    DepthFirstTraverser(Graph g, V root) {
        super(g, root);
    }
    
    @Override
    public void traverse() {
        this.visited = new ArrayList<V>();
        this.traverseNodes(root);
    }
    
    public void traverseNodes(V element) {
        System.out.println(element);
        this.visited.add(element);
        
        List<V> vertexList = Graphs.neighborListOf(this.g, element);
        
        if (!vertexList.isEmpty()) {
            for (V vertex : vertexList) {
                // globale liste besuchter knoten überprüfen
                if (!this.visited.contains(vertex))
                    this.traverseNodes(vertex);
            }
        }
    }
}


