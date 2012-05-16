/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka.flows;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author maiwald
 */
public class FordFulkersonFlow<V>
{

    protected DefaultDirectedWeightedGraph<V, DefaultWeightedEdge> g;
    protected Map<DefaultWeightedEdge, Double> f;
    protected V q;
    protected V s;

    private Map<V, Marker> marked;

    private class Marker
    {

        private V predecessor;
        private double increment;
        private boolean inspected;
        private boolean forward;

        public Marker()
        {
            this(null, Double.POSITIVE_INFINITY);
        }

        public Marker(V predecessor, Double increment)
        {
            this.predecessor = predecessor;
            this.increment = increment;
            this.inspected = false;
        }
        
        public V getPredecessor()
        {
            return this.predecessor;
        }
        
        public double getIncrement()
        {
            return this.increment;
        }

        public boolean isForward()
        {
            return this.forward == true;
        }

        public boolean isBackward()
        {
            return !this.isForward();
        }

        public void setInspected()
        {
            this.inspected = true;
        }

        public boolean isInspected()
        {
            return this.inspected;
        }

        public void setForward()
        {
            this.forward = true;
        }

        public void setBackward()
        {
            this.forward = false;
        }
    }

    public FordFulkersonFlow(DefaultDirectedWeightedGraph<V, DefaultWeightedEdge> g)
    {
        this.g = g;
    }

    public Double getMaximumFlow(V q, V s)
    {
        this.q = q;
        this.s = s;

        initialize();

        while (!areAllMarkedEdgesInspected())
        {
            inspectAndMark();

            if (isVertexMarked(this.s))
            {
                widenFlow();
                resetForNextRun();
            }
        }

        return calculateFlow();
    }

    protected boolean areAllMarkedEdgesInspected()
    {
        for (Marker m : this.marked.values())
        {
            if (!m.isInspected())
            {
                return false;
            }
        }

        return true;
    }

    protected void inspectAndMark()
    {
        V current_vertex = getNextUninspectedVertex();
        Marker current_marker = this.marked.get(current_vertex);
        current_marker.setInspected();

        for (DefaultWeightedEdge edge : this.g.outgoingEdgesOf(current_vertex))
        {
            V target = this.g.getEdgeTarget(edge);
            if (!isVertexMarked(target) && hasSufficientCapacity(edge))
            {
                Marker marker = new Marker(current_vertex,
                        Math.min(current_marker.getIncrement(), this.g.getEdgeWeight(edge) - this.f.get(edge)));
                marker.setForward();
                this.marked.put(target, marker);
            }
        }

        for (DefaultWeightedEdge edge : this.g.incomingEdgesOf(current_vertex))
        {
            V source = this.g.getEdgeSource(edge);
            if (!isVertexMarked(source) && this.f.get(edge) > 0)
            {
                Marker marker = new Marker(current_vertex, Math.min(current_marker.getIncrement(), this.f.get(edge)));
                marker.setBackward();
                this.marked.put(source, marker);
            }
        }
    }

    protected V getNextUninspectedVertex()
    {
        for (V vertex : this.marked.keySet())
        {
            if (!this.marked.get(vertex).isInspected())
            {
                return vertex;
            }
        }

        return null;
    }

    protected boolean hasSufficientCapacity(DefaultWeightedEdge edge)
    {
        return this.f.get(edge) < this.g.getEdgeWeight(edge);
    }

    protected boolean isVertexMarked(V vertex)
    {
        return this.marked.get(vertex) != null;
    }

    protected void widenFlow()
    {
        V vertex = this.s;
        Marker s_marker = this.marked.get(this.s);

        if (this.getClass() == FordFulkersonFlow.class)
            System.out.print("[");
        
        while (!vertex.equals(this.q))
        {
            Marker marker = this.marked.get(vertex);
            V predecessor = marker.getPredecessor();

            if (this.getClass() == FordFulkersonFlow.class)
                System.out.printf("%s, ", vertex);
            
            if (marker.isForward())
            {
                DefaultWeightedEdge edge = this.g.getEdge(predecessor, vertex);
                this.f.put(edge, this.f.get(edge) + s_marker.getIncrement());
            } else
            {
                DefaultWeightedEdge edge = this.g.getEdge(predecessor, vertex);
                this.f.put(edge, this.f.get(edge) - s_marker.getIncrement());
            }

            vertex = predecessor;
        }
        
        if (this.getClass() == FordFulkersonFlow.class)
            System.out.printf("%s]\n", vertex);
    }
    
    protected double calculateFlow()
    {
        Set<V> q_set = new HashSet(this.marked.keySet());

        double outgoing_value = 0;
        for (V vertex : q_set)
        {
            for (DefaultWeightedEdge e : this.g.outgoingEdgesOf(vertex))
            {
                if (!q_set.contains(this.g.getEdgeTarget(e)))
                {
                    outgoing_value += this.f.get(e);
                }
            }
        }

        double incoming_value = 0;
        for (V vertex : q_set)
        {
            for (DefaultWeightedEdge e : this.g.incomingEdgesOf(vertex))
            {
                if (!q_set.contains(this.g.getEdgeSource(e)))
                {
                    incoming_value += this.f.get(e);
                }
            }
        }

        return outgoing_value - incoming_value;
    }

    protected void initialize()
    {
        this.f = new HashMap<DefaultWeightedEdge, Double>();
        for (DefaultWeightedEdge e : g.edgeSet())
        {
            f.put(e, 0.0);
        }

        resetForNextRun();
    }

    protected void resetForNextRun()
    {
        this.marked = new HashMap<V, Marker>();
        this.marked.put(q, new Marker());
        afterResetHook();
    }
    
    protected void afterResetHook()
    {
    }
}
