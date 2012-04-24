/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka1;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;

/**
 *
 * @author abe263
 */
public class BreadthFirstTraverser<V> extends Traverser<V> {
      
    BreadthFirstTraverser(Graph g, V root) {
        super(g, root);
    }
    
    @Override
    public void traverse() {
        List<V> vertexList = Graphs.neighborListOf(this.g, this.root);
        List<V> parents = new ArrayList<V>();
        parents.add(this.root);
        
        System.out.println(root);

        while (!vertexList.isEmpty()) {
            List<V> children = new ArrayList<V>();
            
            for (V vertex : vertexList) {
                System.out.println(vertex);
                parents.add(vertex); // die jeweilige Ecke zu parents hinzufügen
                children.addAll(Graphs.neighborListOf(this.g, vertex));
            }
            
            children.removeAll(parents); // nach jeder Ebene die parents entfernen
            vertexList = children;
        }
    }
}
