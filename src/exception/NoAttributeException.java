package exception;

public class NoAttributeException extends RuntimeException{
    public NoAttributeException(String attribute){
        super("The attribute "+ attribute+" doesn't exist.");
    }
}
