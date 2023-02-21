package com.teapot.chapter4.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * P71
 *
 * @param <V> 顶点类型
 * @param <E> 边类型
 */
public abstract class Graph<V, E extends Edge> {
    // 顶点列表
    private final ArrayList<V> vertices = new ArrayList<>();
    // 邻接表（整数索引）
    protected ArrayList<ArrayList<E>> edges = new ArrayList<>();

    public Graph() {
    }

    public Graph(List<V> vertices) {
        this.vertices.addAll(vertices);
        for (V vertex : vertices) {
            edges.add(new ArrayList<>());
        }
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getEdgeCount() {
        return edges.stream().mapToInt(ArrayList::size).sum();
    }

    public int addVertex(V vertex) {
        vertices.add(vertex);
        edges.add(new ArrayList<>());
        // 返回顶点索引
        return getVertexCount() - 1;
    }

    public V vertexAt(int index) {
        return vertices.get(index);
    }

    public int indexOf(V vertex) {
        return vertices.indexOf(vertex);
    }

    /**
     * 获取所有相邻的节点
     * @param index 节点索引
     * @return
     */
    public List<V> neighborsOf(int index) {
        return edges.get(index).stream()
                .map(edge -> vertexAt(edge.v)).collect(Collectors.toList());
    }


    public List<V> neighborsOf(V vertex) {
        return neighborsOf(indexOf(vertex));
    }

    public List<E> edgesOf(int index) {
        return edges.get(index);
    }

    public List<E> edgesOf(V vertex) {
        return edgesOf(indexOf(vertex));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getVertexCount(); i++) {
            sb.append(vertexAt(i));
            sb.append(" -> ");
            sb.append(Arrays.toString(neighborsOf(i).toArray()));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
