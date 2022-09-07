package com.company;

import java.util.*;

public class Graph {

    private List<Integer> nodes = new ArrayList<>();

    private Map<Integer, List<Integer>> edges = new HashMap<>();

    public Graph(int n, int m) {

        for (int i = 1; i <= n; i++){
            nodes.add(i);
            edges.put(i, new ArrayList<>());
        }

        initializeEdges(m);
    }

    public void initializeEdges(int m){
        if(m > nodes.size()){
            addHamiltonianCycle();
        }
        m = m - nodes.size();
        Random random = new Random();

        for(int i = 0; i < m; i++){
            int from = this.nodes.get(random.nextInt(this.nodes.size()));
            int to = this.nodes.get(random.nextInt(this.nodes.size()));

            while (! addEdge(from, to)){
                from = this.nodes.get(random.nextInt(this.nodes.size()));
                to = this.nodes.get(random.nextInt(this.nodes.size()));
            }
        }
    }

    public void addHamiltonianCycle(){
        List<Integer> nodesCopy = new ArrayList<>(this.nodes);
        Collections.shuffle(nodesCopy);

        int i;
        for(i = 0; i < nodesCopy.size() - 1; i++){
            addEdge(nodesCopy.get(i), nodesCopy.get(i + 1));
        }
        addEdge(nodesCopy.get(i), nodesCopy.get(0));
    }

    public boolean addEdge(int from, int to){
        if (! this.edges.get(from).contains(to)){
            this.edges.get(from).add(to);
            return true;
        }
        return false;
    }

    public int getNumberOfNodes(){
        return this.nodes.size();
    }

    public int getNumberOfEdges(){
        int i = 0;
        for(Integer n: this.edges.keySet()){
            i += this.edges.get(n).size();
        }
        return i;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public List<Integer> getNextNodes(int currentNode) {
        return this.edges.get(currentNode);
    }

    public boolean checkValidity(List<Integer> path){
        for(int i = 0; i < path.size() - 1; i++){
            if(! isEdge(path.get(i), path.get(i + 1))){
                return false;
            }
        }
        return true;
    }

    public boolean isEdge(int a, int b){
        return this.edges.get(a).contains(b);
    }
}
