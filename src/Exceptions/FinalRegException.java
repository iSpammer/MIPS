package Exceptions;

public class FinalRegException extends Exception {
    String [] reg;
    public FinalRegException(String [] reg, String message) {
        super(message);
        this.reg = reg;
    }
}
