/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;

/**
 *
 * @author maiwald
 */
public class ShortestPathDijkstra<V> {
    
    private final Graph g;
    
    private Set<V> done = new HashSet();
    private Set<V> remaining = new HashSet();

    private Map<V,Double> distances = new HashMap();
    private Map<V,V> predecessors = new HashMap();

    int counter = 0;
    
    public ShortestPathDijkstra(Graph g)
    {
        this.g = g;
    }

    public List<V> getShortestPath(V source, V target)
    {
        clearState();
        this.remaining = new HashSet<V>(this.g.vertexSet());

        for (V elem : this.remaining)
            this.distances.put(elem, Double.POSITIVE_INFINITY);
        
        this.distances.put(source, 0d);
        
        while(!this.remaining.isEmpty())
        {
            V closest = getClosestVertex();
            this.remaining.remove(closest);
            
            for (V neighbor : (List<V>)Graphs.neighborListOf(this.g, closest))
            {
                if (this.remaining.contains(neighbor))
                    updateDistance(closest, neighbor);
            }
        }

        return getPath(target);
    }

    private V getClosestVertex()
    {
        return Collections.min(this.remaining, new Comparator<V>() {
            @Override
            public int compare(V a, V b) {
                return distances.get(a).compareTo(distances.get(b));
            }
        });
    }

    private void updateDistance(V source, V target)
    {
        this.counter++;
        
        double alternative = this.distances.get(source) + 
                this.g.getEdgeWeight(this.g.getEdge(source, target));

        if (alternative < this.distances.get(target))
        {
            this.distances.put(target, alternative);
            this.predecessors.put(target, source);
        }
    }
    
    private List<V> getPath(V target)
    {
        List<V> path = new LinkedList();
        path.add(target);

        V tmp = target;
        while (this.predecessors.get(tmp) != null) 
        {
            tmp = this.predecessors.get(tmp);
            path.add(0, tmp);
        }

        return path;
    }

    private void clearState() {
        this.done.clear();
        this.distances.clear();
        this.predecessors.clear();
        
        this.counter = 0;
    }            
}
