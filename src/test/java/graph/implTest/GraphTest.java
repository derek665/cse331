package graph.implTest;

import static org.junit.Assert.*;
import java.util.*;
import graph.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * This class contains a set of test cases that can be used to test the implementation of Graph class
 */

public final class GraphTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    private Graph<String, String> graph1;
    private Graph<String, String> graph2;
    private Graph<String, String> graph3;

    @Before
    public void setUp() {
        graph1 = new Graph<>();
        graph2 = new Graph<>();
        graph2.addNode("n1");
        graph2.addNode("n2");
        graph3 = new Graph<>();
        graph3.addNode("n1");
        graph3.addNode("n2");
        graph3.addNode("n3");
        graph3.addChild("n1", "n2", "e1");
        graph3.addChild("n1", "n2", "e2");
        graph3.addChild("n1", "n3", "e1");
        assertTrue(graph2.hasNode("n1") && graph2.hasNode("n2"));
        assertTrue(graph3.getLabels("n1", "n2").contains("e1"));
    }

    @Test
    public void testEmptyGraph() {
        assertEquals(0, graph1.getNodes().size());
    }

    @Test
    public void testAddOne() {
        Graph<String, String> g1 = new Graph<>();
        assertTrue(g1.addNode("n1"));
        Set<String> s = g1.getNodes();
        assertTrue(s.contains("n1"));
        assertEquals(1, s.size());
    }

    @Test
    public void testAddChild2() {
        Edge<String, String> e = new Edge<>("n2", "e1");
        assertTrue(graph2.addChild("n1", e));
    }

    @Test
    public void testAddOneMore() {
        Graph<String, String> g1 = new Graph<>();
        assertTrue(g1.addNode("n1"));
        assertTrue(g1.addNode("n2"));
        Set<String> s = g1.getNodes();
        assertTrue(s.contains("n1") && s.contains("n2"));
        assertEquals(2, s.size());
    }

    @Test
    public void testAddEdge() {
        graph2.addChild("n1", "n2", "e1");
        Edge e = new Edge<>("n2", "e1");
        assertTrue(graph2.getEdges("n1").contains(e));
    }

    @Test
    public void testAddDuplicateNode() {
        assertFalse(graph3.addNode("n1"));
        Set<Edge<String, String>> s = graph3.getEdges("n1");
        boolean found = false;
        for (Edge e : s) {
            if (e.getChild().equals("n2")) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testHasEdge() {
        Edge<String, String> e = new Edge<>("n2", "e1");
        assertTrue(graph3.hasEdge("n1", e));
    }

    @Test
    public void testAddDuplicateLabel() {
        assertFalse(graph3.addChild("n1", "n2", "e1"));
        Set<String> s = graph3.getLabels("n1", "n2");
        assertTrue(s.contains("e1"));
    }

    @Test
    public void testAddMoreEdge() {
        graph2.addChild("n1", "n2", "e2");
        Set<String> s = graph2.getLabels("n1", "n2");
        assertTrue(s.contains("e2"));
    }

    @Test
    public void testIsChild() {
        assertTrue(graph3.isChildOf("n2", "n1"));
        assertTrue(graph3.isChildOf("n3", "n1"));
    }

    @Test
    public void testGetNodesSize() {
        Set<String> s = graph3.getNodes();
        assertEquals(3, s.size());
    }

    @Test
    public void testGetEdgeSize() {
        Set<Edge<String, String>> s = graph3.getEdges("n1");
        assertEquals(3, s.size());
    }

    @Test
    public void testGetLabelsSize() {
        Set<String> s = graph3.getLabels("n1", "n2");
        assertEquals(2, s.size());
    }

    @Test
    public void testRemoveNode() {
        Graph<String, String> g1 = new Graph<>();
        g1.addNode("n1");
        assertTrue(g1.hasNode("n1"));
        g1.removeNode("n1");
        assertFalse(g1.hasNode("n1"));
    }

    @Test
    public void testRemoveEdge2() {
        Graph<String, String> g1 = new Graph<>();
        g1.addNode("n1");
        g1.addNode("n2");
        g1.addChild("n1", "n2", "e1");
        g1.removeEdgeFrom("n1",new Edge<>("n2", "e1"));
        assertTrue(g1.getLabels("n1", "n2").isEmpty());
    }

    @Test
    public void testRemoveNodeWithEdges() {
        Graph<String, String> g1 = new Graph<>();
        g1.addNode("n1");
        g1.addNode("n2");
        g1.addChild("n1", "n2", "e1");
        assertTrue(g1.getLabels("n1", "n2").contains("e1"));
        g1.addChild("n2", "n1", "e2");
        assertTrue(g1.getLabels("n2", "n1").contains("e2"));
        g1.removeNode("n1");
        assertFalse(g1.hasNode("n1"));
        Set<Edge<String, String>> s = g1.getEdges("n2");
        assertTrue(s.isEmpty());
    }

    @Test
    public void testRemoveEdgeFrom() {
        Graph<String, String> g1 = new Graph<>();
        g1.addNode("n1");
        g1.addNode("n2");
        g1.addChild("n1", "n2", "e1");
        g1.removeEdgeFrom("n1", "n2", "e1");
        assertTrue(g1.getLabels("n1", "n2").isEmpty());
    }

    @Test
    public void testCircularEdge() {
        Graph<String, String> g1 = new Graph<>();
        g1.addNode("n1");
        assertTrue(g1.addChild("n1", "n1", "e1"));
        assertTrue(g1.getLabels("n1", "n1").contains("e1"));
        Edge e = new Edge<>("n1", "e1");
        assertTrue(g1.getEdges("n1").contains(e));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeToNonExistParent() {
        graph1.addChild("n3", "n1", "e1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeToNonExistChild() {
        graph1.addChild("n1", "n3", "e1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistNode() {
        graph1.removeNode("n4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEdgeOnNonExistParent() {
        graph3.getEdges("n5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLabelOnNonExistNodes() {
        graph3.getLabels("n5", "n6");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEdgeOnNonExistParent() {
        graph3.removeEdgeFrom("n5", "n1", "e1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEdgeOnNonExistChild() {
        graph3.removeEdgeFrom("n1", "n5", "e1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEdgeOnNonExistLabel() {
        graph3.removeEdgeFrom("n1", "n2", "e5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsChildOfOnNonExistNode() {
        graph1.isChildOf("n5", "n7");
    }
}