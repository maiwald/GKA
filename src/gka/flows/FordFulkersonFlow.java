/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka.flows;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        private Double capacity;
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

        public void setForward()
        {
            this.forward = true;
        }

        public void setBackward()
        {
            this.forward = false;
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
                System.out.println(this.f);
                widenFlow();
            }
        }

        return calculateFlow();

    }

    private void inspectAndMark()
    {
        V current_vertex = getNextInspectedVertex();
        Marker current_marker = this.markers.get(current_vertex);
        current_marker.setInspected();

        Set<DefaultWeightedEdge> outgoing_edges = this.g.outgoingEdgesOf(current_vertex);
        for (DefaultWeightedEdge edge : outgoing_edges)
        {
            V target = this.g.getEdgeTarget(edge);
            if (!isVertexMarked(target) && hasSufficientCapacity(edge))
            {
                Marker marker = new Marker(current_vertex,
                        Math.min(current_marker.capacity, this.g.getEdgeWeight(edge) - this.f.get(edge)));
                marker.setForward();
                this.markers.put(target, marker);
            }
        }

        Set<DefaultWeightedEdge> incoming_edges = this.g.incomingEdgesOf(current_vertex);
        for (DefaultWeightedEdge edge : incoming_edges)
        {
            V source = this.g.getEdgeSource(edge);
            if (!isVertexMarked(source) && this.f.get(edge) > 0)
            {
                Marker marker = new Marker(current_vertex, Math.min(current_marker.capacity, this.f.get(edge)));
                marker.setBackward();
                this.markers.put(source, marker);
            }
        }
    }

    private void widenFlow()
    {
        V vertex = this.s;
        Marker s_marker = this.markers.get(this.s);
        while (vertex != this.q)
        {
            Marker marker = this.markers.get(vertex);
            V predecessor = marker.predecessor;

            if (marker.isForward())
            {
                DefaultWeightedEdge edge = this.g.getEdge(predecessor, vertex);
                this.f.put(edge, this.f.get(edge) + s_marker.capacity);
            } else
            {
                DefaultWeightedEdge edge = this.g.getEdge(vertex, predecessor);
                this.f.put(edge, this.f.get(edge) - s_marker.capacity);
            }

            vertex = predecessor;
        }

        resetMarkers();
    }
    
    private double calculateFlow()
    {
        V vertex = this.s;
        Double result = Double.POSITIVE_INFINITY;

        while (vertex != this.q)
        {
            Marker marker = this.markers.get(vertex);
            V predecessor = marker.predecessor;

            if (marker.isForward())
            {
                DefaultWeightedEdge edge = this.g.getEdge(predecessor, vertex);
                result = Math.min(result, this.g.getEdgeWeight(edge));
            }
            else
            {
                DefaultWeightedEdge edge = this.g.getEdge(vertex, predecessor);
                result = Math.min(result, this.g.getEdgeWeight(edge));
            }

            vertex = predecessor;
        }
        
        return result;
    }

    private boolean isVertexMarked(V vertex)
    {
        return this.markers.get(vertex) != null;
    }

    private boolean hasSufficientCapacity(DefaultWeightedEdge edge)
    {
        return this.f.get(edge) < this.g.getEdgeWeight(edge);
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

    private V getNextInspectedVertex()
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
