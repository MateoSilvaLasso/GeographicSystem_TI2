package Exception;

public class InexistentComandException extends RuntimeException{
    public InexistentComandException(String comand){
        super("El comando "+comand+" no existe en el sistema");
    }
}
