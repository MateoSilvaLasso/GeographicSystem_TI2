package exception;

public class NoCountryIDException extends RuntimeException{
    public NoCountryIDException(String id){
        super("The id "+id+" is not in the system.");
    }
}
