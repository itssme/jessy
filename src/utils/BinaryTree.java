package utils;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    23.05.17
 * Poject:  jessy
 * Package: utils
 * Desc.:
 */
public class BinaryTree<K extends String, V extends Integer> {

    private class Node<K extends String, V extends Integer> {

        private K key;
        private V value;

        private Node<K, V> left;
        private Node<K, V> right;

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

    public BinaryTree() {
    }

    public BinaryTree(K name, V score) {
        root = new Node<K, V>(name, score);
    }

    public boolean insert(K name, V value) {
        Node<K, V> nodeToInsert = new Node(name, value);
        return insertRecursive(root, nodeToInsert);
    }

    private boolean insertRecursive(Node<K, V> root, Node<K, V> nodeToInsert) {
        if (nodeToInsert.getValue().intValue() < root.getValue().intValue()) {
            return insertRecursive(root.getLeft(), nodeToInsert);
        }
        return true;
    }

}
