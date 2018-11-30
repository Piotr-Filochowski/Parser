public class Parser {


    BinaryTree binaryTree;

    Parser(){


    }

    boolean checkSyntax(BinaryTree binaryTree){
        this.binaryTree = binaryTree;
        return checkNode(binaryTree.root);

    }

    boolean checkNode(Node node){
        if(isNumber(node.value)){
            if(!isLeaf(node)){
                return false;
            }
        } else{
            if(!hasChildren(node)){
                return false;
            }
        }
        return true;
    }

    private boolean hasChildren(Node node) {
        if(node.left != null || node.right != null) {
            return true;
        }
        return false;
    }

    private boolean isLeaf(Node node) {
        if(node.left == null && node.right == null){
            return true;
        }
        else return false;
    }


    private boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }


}
