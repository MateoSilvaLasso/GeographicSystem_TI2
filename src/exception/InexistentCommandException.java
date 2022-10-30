package exception;

public class InexistentCommandException extends RuntimeException{
    public InexistentCommandException(String command){
        super("The command "+command+" doesn't exist in the system.");
    }
}
