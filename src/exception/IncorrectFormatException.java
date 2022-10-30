package exception;

public class IncorrectFormatException extends RuntimeException{
    public IncorrectFormatException(String s){
        super("The command part "+ s +" has an incorrect format.");
    }

    public IncorrectFormatException(double d){
        super("The command part "+ d +" has an incorrect format.");
    }
}
