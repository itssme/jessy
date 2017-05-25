package utils;

import model.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    23.05.17
 * Poject:  jessy
 * Package: utils
 * Desc.:
 */
public class PlayerBinaryTree<K extends String, V extends Float> {


    private class Node<K extends String, V extends Float> {

        private K key;
        private V value;

        private Node<K, V> left = null;
        private Node<K, V> right = null;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getLeft() {
            return left;
        }

        public void setLeft(Node<K, V> left) {
            this.left = left;
        }

        public Node<K, V> getRight() {
            return right;
        }

        public void setRight(Node<K, V> right) {
            this.right = right;
        }
    }

    private Node<K, V> root = null;

    public PlayerBinaryTree() {
    }

    public PlayerBinaryTree(ModelIterator<Player> allPlayers) {
        while (allPlayers.hasNext()) {
            Player p = allPlayers.next();
            this.insert((K) p.getName(), (V) Float.valueOf(p.getScore()));
        }
    }

    public boolean insert(K name, V value) {
        Node<K, V> nodeToInsert = new Node(name, value);
        if (root == null) {
            root = nodeToInsert;
            return true;
        } else {
            return insertRecursive(root, nodeToInsert);
        }
    }

    private boolean insertRecursive(Node<K, V> root, Node<K, V> nodeToInsert) {
        if (nodeToInsert.getValue().floatValue() <
                root.getValue().floatValue()) {
            if (root.getLeft() != null) {
                return insertRecursive(root.getLeft(), nodeToInsert);
            } else {
                root.setLeft(nodeToInsert);
                return true;
            }
        } else if (nodeToInsert.getValue().floatValue() >
                root.getValue().floatValue()) {
            if (root.getRight() != null) {
                return insertRecursive(root.getRight(), nodeToInsert);
            } else {
                root.setRight(nodeToInsert);
                return true;
            }
        }
        return true;
    }

    public Collection asCollection() {
        ArrayList list = new ArrayList();
        if (root == null) {
            return list;
        } else {
            return this.getColl(list, root);
        }
    }

    private Collection getColl(ArrayList list, Node<K, V> root) {
        if (root.getLeft() != null) {
            this.getColl(list, root.getLeft());
        }
        list.add("" + root.getKey() + " - " + root.getValue());
        if (root.getRight() != null) {
            this.getColl(list, root.getRight());
        }
        return list;
    }

}
