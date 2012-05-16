/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gka.traversers;

import org.jgrapht.Graph;

/**
 *
 * @author abe263
 */
public abstract class Traverser<V>
{

    protected Graph g;
    protected V root;

    protected Traverser(Graph g, V root)
    {
        this.g = g;
        this.root = root;
    }

    public abstract void traverse();
}
