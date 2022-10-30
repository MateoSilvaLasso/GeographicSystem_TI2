package exception;

public class IdAlreadyUsedException extends RuntimeException{
    public IdAlreadyUsedException(String id){
        super("this id ("+id+") is already used.");
    }
}
