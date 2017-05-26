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
 * Desc.:   A Binary Tree which will sort the Players based on their scores
 */
public class PlayerBinaryTree<K extends String, V extends Float> {

    /**
     * A custom class for a Node of the binary Tree
     *
     * @param <K> The Type of the key
     * @param <V> The Type of the value
     */
    private class Node<K extends String, V extends Float> {

        /**
         * The key
         */
        private K key;
        /**
         * The value
         */
        private V value;

        private Node<K, V> left = null;
        private Node<K, V> right = null;

        /**
         * The Constructor for this Node
         * @param key The key for the Node
         * @param value The value for the node
         */
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

    /**
     * The Root-Node of the BinaryTree
     */
    private Node<K, V> root = null;

    /**
     * A Constructor which takes a ModelIterator as a parameter. It will then
     * iterate over all the elements and insert them.
     *
     * @param allPlayers The Collection containing all players
     */
    public PlayerBinaryTree(ModelIterator<Player> allPlayers) {
        while (allPlayers.hasNext()) {
            Player p = allPlayers.next();
            this.insert((K) p.getName(), (V) Float.valueOf(p.getScore()));
        }
    }

    /**
     * A sequential Method which inserts the a new Node
     * @param name The Name of the Player which should be inserted
     * @param value The Score of the Player
     * @return True, if the Node was successfully inserted
     */
    public boolean insert(K name, V value) {
        Node<K, V> nodeToInsert = new Node(name, value);
        if (root == null) {
            root = nodeToInsert;
            return true;
        } else {
            return insertRecursive(root, nodeToInsert);
        }
    }

    /**
     * Inserts a new Node recursively into the BinaryTree
     * @param root The root-Node of the Binary Sub-Tree
     * @param nodeToInsert The node which should be inserted
     * @return True, if the Node was successfully inserted, false otherwise
     */
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

    /**
     * Returns a Collection of the BinaryTree
     * @return The Collection with an ascending order
     */
    public Collection asCollection() {
        ArrayList list = new ArrayList();
        if (root == null) {
            return list;
        } else {
            return this.getColl(list, root);
        }
    }

    /**
     * A recursive Method which fills an ArrayList with all the elements ordered
     * by the score of each Player
     * @param list The Collection which will be filled
     * @param root The root-Node of the binary sub-tree
     * @return The Complete Collection where the elements will be added in
     *         ascending Order
     */
    private Collection getColl(ArrayList list, Node<K, V> root) {
        if (root.getLeft() != null) {
            this.getColl(list, root.getLeft());
        }
        list.add(new Player((String) root.getKey(), (float) root.getValue()).
                toString());
        if (root.getRight() != null) {
            this.getColl(list, root.getRight());
        }
        return list;
    }

}
