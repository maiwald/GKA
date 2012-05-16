/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka.flows;

import gka.pathfinders.Dijkstra;
import java.util.LinkedList;
import java.util.Queue;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author maiwald
 */
public class EdmondsKarpFlow<V> extends FordFulkersonFlow<V>
{

    private Queue<V> queue;

    public EdmondsKarpFlow(DirectedGraph<V, DefaultWeightedEdge> g)
    {
        super(g);
        this.queue = new LinkedList();
    }

    @Override
    public Double getMaximumFlow(V q, V s)
    {
        this.q = q;
        this.s = s;

        initialize();

        while (true)
        {
            calculateNextVertexQueue();

            if (this.queue.isEmpty())
            {
                break;
            }

            inspectAndMark();

            if (isVertexMarked(this.s))
            {
                widenFlow();
                resetForNextRun();
            }
        }

        return calculateFlow();
    }

    @Override
    protected V getNextUninspectedVertex()
    {
        return this.queue.poll();
    }

    private void calculateNextVertexQueue()
    {
        if (this.queue.isEmpty())
        {
            Graph converted_graph = getConvertedGraph();
            Dijkstra d = new Dijkstra<V>(converted_graph, this.q);
            this.queue.addAll(d.getShortestPathToTarget(this.s));
        }
    }

    private Graph getConvertedGraph()
    {
        Graph converted = new DefaultDirectedGraph(DefaultEdge.class);

        for (V vertex : this.g.vertexSet())
        {
            converted.addVertex(vertex);
        }

        for (DefaultWeightedEdge edge : this.g.edgeSet())
        {
            if (this.f.get(edge) < this.g.getEdgeWeight(edge))
            {
                V source = this.g.getEdgeSource(edge);
                V target = this.g.getEdgeTarget(edge);
                converted.addEdge(source, target);
            }
        }

        return converted;
    }

    @Override
    protected void afterResetHook()
    {
        this.queue.clear();
    }
}
