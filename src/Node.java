class Node {

    String value;
    Node left;
    Node right;

    Node(String value) {
        this.value = value;
        right = null;
        left = null;
    }
    Node(String value, Node left, Node right){
        this.right = right;
        this.left = left;
        this.value = value;
    }

}