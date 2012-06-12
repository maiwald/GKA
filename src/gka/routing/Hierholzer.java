/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka.routing;

import java.util.*;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author maiwald
 */
public class Hierholzer<V, E>
{

    private UndirectedGraph<V, E> g;
    private final Random random = new Random();
    private List<List<V>> paths;

    public List<V> getEulerTour(UndirectedGraph<V, E> g)
    {
        this.g = copyGraph(g);
        this.paths = new ArrayList<List<V>>();

        if (!this.isGraphSuitable())
        {
            return new ArrayList<V>();
        }

        while (!this.isEulerFound())
        {
            V start = getStart();
            V currentVertex = start;

            List<V> path = new ArrayList<V>();
            path.add(currentVertex);

            while (path.size() == 1 || !currentVertex.equals(start))
            {
                List<V> neighbors = new ArrayList(Graphs.neighborListOf(this.g, currentVertex));
                V nextVertex = neighbors.get(this.random.nextInt(neighbors.size()));

                this.g.removeEdge(currentVertex, nextVertex);
                this.g.removeEdge(nextVertex, currentVertex);


                path.add(nextVertex);
                currentVertex = nextVertex;
            }

            this.paths.add(path);
        }

        return this.mergePaths();
    }

    private List<V> mergePaths()
    {
        List<V> path = this.paths.get(0);
        this.paths.remove(0);

        while (!this.paths.isEmpty())
        {
            for (int i = 0; i < path.size(); i++)
            {
                if (path.get(i).equals(this.paths.get(0).get(0)))
                {
                    path.remove(i);
                    path.addAll(i, this.paths.get(0));
                    this.paths.remove(0);

                    break;
                }
            }
        }

        return path;
    }

    private boolean isGraphSuitable()
    {
        ConnectivityInspector ci = new ConnectivityInspector<V, E>(this.g);

        if (!ci.isGraphConnected())
        {
            return false;
        }

        for (V vertex : this.g.vertexSet())
        {
            if (this.g.degreeOf(vertex) % 2 == 1)
            {
                return false;
            }
        }

        return true;
    }

    private boolean isEulerFound()
    {
        return this.g.edgeSet().isEmpty();
    }

    private V getStart()
    {
        if (this.paths.isEmpty())
        {
            List<V> vertexList = new ArrayList(this.g.vertexSet());
            return vertexList.get(this.random.nextInt(vertexList.size()));
        } else
        {
            List<V> vertexList = this.paths.get(this.paths.size() - 1);

            for (V vertex : vertexList)
            {
                if (this.g.degreeOf(vertex) > 0)
                {
                    return vertex;
                }
            }
        }

        return null;
    }

    private UndirectedGraph<V, E> copyGraph(UndirectedGraph<V, E> original)
    {
        UndirectedGraph<V, E> copy = new SimpleGraph(DefaultEdge.class);

        for (V vertex : original.vertexSet())
        {
            copy.addVertex(vertex);
        }

        for (E edge : original.edgeSet())
        {
            copy.addEdge(
                    original.getEdgeSource(edge),
                    original.getEdgeTarget(edge));
        }

        return copy;
    }

    private void debug(Object o)
    {
        System.out.println(o.toString());
    }
}
