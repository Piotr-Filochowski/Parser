public class SyntaxException extends Exception {
    String value;

    SyntaxException(String value){
        this.value = value;
    }

}
