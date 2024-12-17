package com.bstapp.binarytree.service;

import com.google.gson.Gson;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BinaryTreeService {
    @Data
    static class Node {
        int value;
        Node left, right;

        Node(int value) {
            this.value = value;
        }
    }

    private Node root;

    public void insert(int value) {
        root = insertRec(root, value);
        root = balanceTree(root); // Balance the tree after every insertion
    }

    private Node insertRec(Node root, int value) {
        if (root == null) return new Node(value);
        if (value < root.value) root.left = insertRec(root.left, value);
        else if (value > root.value) root.right = insertRec(root.right, value);
        return root;
    }

    // Balancing the tree
    private Node balanceTree(Node root) {
        List<Node> nodes = new ArrayList<>();
        storeInOrder(root, nodes);
        return buildBalancedTree(nodes, 0, nodes.size() - 1);
    }

    // Store nodes in in-order traversal
    private void storeInOrder(Node node, List<Node> nodes) {
        if (node == null) return;
        storeInOrder(node.left, nodes);
        nodes.add(node);
        storeInOrder(node.right, nodes);
    }

    // Rebuild balanced tree from in-order nodes
    private Node buildBalancedTree(List<Node> nodes, int start, int end) {
        if (start > end) return null;

        int mid = (start + end) / 2;
        Node node = nodes.get(mid);

        node.left = buildBalancedTree(nodes, start, mid - 1);
        node.right = buildBalancedTree(nodes, mid + 1, end);

        return node;
    }

    // Convert the tree to JSON with "root" wrapper
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(new TreeWrapper(root));
    }

    @Data
    static class TreeWrapper {
        private final Node root;

        TreeWrapper(Node root) {
            this.root = root;
        }
    }
}




