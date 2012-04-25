/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka.pathfinders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.Graph;

/**
 *
 * @author maiwald
 */
public class FloydWarshall<V>
{
    private final Graph g;
    private final List<V> vertices;

    private Map<V, Map<V, Double>> d;
    private Map<V, Map<V, V>> t;

    public int counter = 0;

    public FloydWarshall(Graph g)
    {
        this.g = g;
        this.vertices = (List<V>)Arrays.asList(g.vertexSet());

        initializeDMatrix();
        initializeTMatrix();
    }
    
    public List<V> getShortestPath(V source, V target)
    {
        return null;
    }

    private void initializeDMatrix()
    {
        this.d = new HashMap<V, Map<V, Double>>();
        for (V row : this.vertices)
        {
            this.d.put(row, new HashMap<V, Double>());
        
            for (V col : this.vertices)
            {
                Double value = null;
                
                if (col.equals(row))
                    value = 0d;

                else if (this.g.getEdge(row, col) != null)
                    value = this.g.getEdgeWeight(this.g.getEdge(row, col));

                else
                    value = Double.POSITIVE_INFINITY;

                this.d.get(row).put(col, value);
            }
        }
    }

    private void initializeTMatrix()
    {
        this.t = new HashMap<V, Map<V, V>>();
        for (V row : this.vertices)
        {
            this.t.put(row, new HashMap<V, V>());
        
            for (V col : this.vertices)
                this.t.get(row).put(col, null);
        }
    }
    
    private void calculateShortestPaths()
    {
        
    }
}
