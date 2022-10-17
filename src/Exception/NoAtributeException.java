package Exception;

public class NoAtributeException extends RuntimeException{
    public NoAtributeException(String atribute){
        super("El atributo "+ atribute+" no existe");
    }
}
