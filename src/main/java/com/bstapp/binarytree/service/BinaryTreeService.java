package com.bstapp.binarytree.service;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class BinaryTreeService {
    @Data
    static class Node {
        int value;
        Node left, right;

        Node(int value) { this.value = value; }
    }

    private Node root;

    public void insert(int value) {
        root = insertRec(root, value);
    }

    private Node insertRec(Node root, int value) {
        if (root == null) return new Node(value);
        if (value < root.value) root.left = insertRec(root.left, value);
        else if (value > root.value) root.right = insertRec(root.right, value);
        return root;
    }

    public String toJson() {
        return new Gson().toJson(root);
    }
}
