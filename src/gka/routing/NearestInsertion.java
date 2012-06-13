
package gka.routing;

import java.util.*;
import org.jgrapht.WeightedGraph;

/**
 *
 * @author maiwald
 */
public class NearestInsertion<V, E>
{

    private final WeightedGraph<V, E> g;
    private final Random random = new Random();
    private List<V> path;

    public NearestInsertion(WeightedGraph<V, E> g)
    {
        this.g = g;
    }

    public List<V> getShortestHamiltonPath()
    {
        this.path = new ArrayList<V>();

        V start = getRandomStart();
        this.path.add(start);
        this.path.add(start);

        while (!this.pathContainsAllVertices())
        {
            V closestVertex = getClosestVertex();
            Map<List<V>, Double> pathLengthMap = new HashMap<List<V>, Double>();

            for (int i = 1; i < this.path.size(); i++)
            {
                List<V> newPath = new ArrayList<V>();
                newPath.addAll(this.path);
                newPath.add(i, closestVertex);

                pathLengthMap.put(newPath, getPathLength(newPath));
            }

            this.path = getKeyOfSmallestValue(pathLengthMap);
        }

        return this.path;
    }

    private boolean pathContainsAllVertices()
    {
        for (V vertex : this.g.vertexSet())
        {
            if (!this.path.contains(vertex))
            {
                return false;
            }
        }

        return true;
    }

    private V getRandomStart()
    {
        List<V> vertexList = new ArrayList(this.g.vertexSet());
        return vertexList.get(this.random.nextInt(vertexList.size()));
    }

    public double getPathLength(List<V> testPath)
    {
        double total = 0;
        for (int i = 0; i < testPath.size()-1; i++)
        {
            total += this.g.getEdgeWeight(this.g.getEdge(testPath.get(i), testPath.get(i+1)));
        }
        return total;
    }

    private Set<V> getRemainingVerteces()
    {
        Set<V> result = new HashSet<V>(this.g.vertexSet());
        result.removeAll(this.path);
        return result;
    }

    private V getClosestVertex()
    {
        Map<V, Double> vertexDistances = new HashMap<V, Double>();

        for (V remaining : getRemainingVerteces())
        {
            for (V inPath : this.path)
            {
                Double currentDistance = vertexDistances.get(remaining);
                double newDistance = this.g.getEdgeWeight(this.g.getEdge(remaining, inPath));

                if (currentDistance == null || newDistance < currentDistance)
                    vertexDistances.put(remaining, newDistance);
            }
        }

        return getKeyOfSmallestValue(vertexDistances);
    }

    private <K> K getKeyOfSmallestValue(Map<K, Double> map)
    {
        Map.Entry<K,Double> result = null;
        for (Map.Entry<K,Double> entry : map.entrySet())
        {
            if (result == null || entry.getValue().compareTo(result.getValue()) == -1)
                result = entry;
        }

        return result.getKey();
    }
}
