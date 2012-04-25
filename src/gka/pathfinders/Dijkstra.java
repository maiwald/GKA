/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka.pathfinders;

import java.util.*;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author maiwald
 */
public class Dijkstra<V> {
    
    private final Graph g;
    private final V source;
    
    private Set<V> remaining = new HashSet();

    private Map<V,Double> distances = new HashMap();
    private Map<V,V> predecessors = new HashMap();

    public int counter = 0;
    
    public Dijkstra(Graph g, V source)
    {
        this.g = g;
        this.source = source;

        clearState();
        calculateShortestPaths();
    }
    
    
    public List<V> getShortestPathToTarget(V target)
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

    private void calculateShortestPaths()
    {
        this.remaining = new HashSet<V>(this.g.vertexSet());

        for (V elem : this.remaining)
            this.distances.put(elem, Double.POSITIVE_INFINITY);
        
        this.distances.put(this.source, 0d);
        
        while(!this.remaining.isEmpty())
        {
            V closest = getClosestVertex();
            List<V> neighbors = getNeighbors(closest);
            this.remaining.remove(closest);
            
            for (V neighbor : neighbors)
            {
                if (this.remaining.contains(neighbor))
                    updateDistance(closest, neighbor);
            }
        }

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

    private List<V> getNeighbors(V vertex)
    {
        List<V> neighbors = new ArrayList();

        if (this.g instanceof DirectedGraph)
        {
            Set<DefaultEdge> edges = (((DirectedGraph)this.g).outgoingEdgesOf(vertex));
            for (DefaultEdge e : edges)
                neighbors.add((V)this.g.getEdgeTarget(e));
        }
        else
        {
            neighbors = (List<V>)Graphs.neighborListOf(this.g, vertex);
        }
        
        return neighbors;
    }
    
    private void clearState() {
        this.distances.clear();
        this.predecessors.clear();

        this.counter = 0;
    }            
}
