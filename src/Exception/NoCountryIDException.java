package Exception;

public class NoCountryIDException extends RuntimeException{
    public NoCountryIDException(String id){
        super("El id "+id+" no esta en el sistema");
    }
}
