package Exception;

public class IncorrectFormatExcepcion extends RuntimeException{
    public IncorrectFormatExcepcion(String dato){
        super("el dato "+dato+" tiene una forma incorrecta");
    }

    public IncorrectFormatExcepcion(double dato){
        super("el dato "+dato+" tiene una forma incorrecta");
    }
}
