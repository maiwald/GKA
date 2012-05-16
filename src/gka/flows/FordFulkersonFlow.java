/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka.flows;

import java.util.*;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author maiwald
 */
public class FordFulkersonFlow<V>
{

    private DirectedGraph<V, DefaultWeightedEdge> g;
    private Map<DefaultWeightedEdge, Double> f;
    private V q;
    private V s;
    private Map<V, Marker> markers;

    private class Marker
    {

        private V predecessor;
        private double capacity;
        private boolean inspected;
        private boolean forward;

        public Marker()
        {
            this(null, Double.POSITIVE_INFINITY);
        }

        public Marker(V predecessor, Double capacity)
        {
            this.predecessor = predecessor;
            this.capacity = capacity;
            this.inspected = false;
        }
        
        public V getPredecessor()
        {
            return this.predecessor;
        }
        
        public double getCapacity()
        {
            return this.capacity;
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

    public FordFulkersonFlow(DirectedGraph<V, DefaultWeightedEdge> g)
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
                resetMarkers();
            }
        }

        return calculateFlow();
    }

    private boolean areAllMarkedEdgesInspected()
    {
        for (Marker m : this.markers.values())
        {
            if (!m.isInspected())
            {
                return false;
            }
        }

        return true;
    }

    private void inspectAndMark()
    {
        V current_vertex = getNextUninspectedVertex();
        Marker current_marker = this.markers.get(current_vertex);
        current_marker.setInspected();

        for (DefaultWeightedEdge edge : this.g.outgoingEdgesOf(current_vertex))
        {
            V target = this.g.getEdgeTarget(edge);
            if (!isVertexMarked(target) && hasSufficientCapacity(edge))
            {
                Marker marker = new Marker(current_vertex,
                        Math.min(current_marker.getCapacity(), this.g.getEdgeWeight(edge) - this.f.get(edge)));
                marker.setForward();
                this.markers.put(target, marker);
            }
        }

        for (DefaultWeightedEdge edge : this.g.incomingEdgesOf(current_vertex))
        {
            V source = this.g.getEdgeSource(edge);
            if (!isVertexMarked(source) && this.f.get(edge) > 0)
            {
                Marker marker = new Marker(current_vertex, Math.min(current_marker.getCapacity(), this.f.get(edge)));
                marker.setBackward();
                this.markers.put(source, marker);
            }
        }
    }

    private V getNextUninspectedVertex()
    {
        for (V vertex : this.markers.keySet())
        {
            if (!this.markers.get(vertex).isInspected())
            {
                return vertex;
            }
        }

        return null;
    }

    private boolean hasSufficientCapacity(DefaultWeightedEdge edge)
    {
        return this.f.get(edge) < this.g.getEdgeWeight(edge);
    }

    private boolean isVertexMarked(V vertex)
    {
        return this.markers.get(vertex) != null;
    }

    private void widenFlow()
    {
        V vertex = this.s;
        Marker s_marker = this.markers.get(this.s);

        while (vertex != this.q)
        {
            Marker marker = this.markers.get(vertex);
            V predecessor = marker.getPredecessor();

            System.out.printf("%s, ", vertex);

            if (marker.isForward())
            {
                DefaultWeightedEdge edge = this.g.getEdge(predecessor, vertex);
                this.f.put(edge, this.f.get(edge) + s_marker.getCapacity());
            } else
            {
                DefaultWeightedEdge edge = this.g.getEdge(vertex, predecessor);
                this.f.put(edge, this.f.get(edge) - s_marker.getCapacity());
            }

            vertex = predecessor;
        }

        System.out.printf("%s\n", this.q);
        System.err.println(this.f);
    }

    private double calculateFlow()
    {
        Double result = Double.POSITIVE_INFINITY;

        Set<V> q_set = new HashSet();
        q_set.add(this.q);

        Set<V> s_set = new HashSet();
        s_set.add(this.s);

        Random r = new Random();

        Set<V> rest = new HashSet(this.g.vertexSet());
        rest.removeAll(Arrays.asList(this.q, this.s));

        for (V vertex : rest)
        {
            if (r.nextBoolean())
            {
                q_set.add(vertex);
            } else
            {
                s_set.add(vertex);
            }
        }

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

    private void initialize()
    {
        this.f = new HashMap<DefaultWeightedEdge, Double>();
        for (DefaultWeightedEdge e : g.edgeSet())
        {
            f.put(e, 0.0);
        }

        resetMarkers();
    }

    private void resetMarkers()
    {
        this.markers = new HashMap<V, Marker>();
        this.markers.put(q, new Marker());
    }
}
