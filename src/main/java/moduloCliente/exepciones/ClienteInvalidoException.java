package moduloCliente.exepciones;

public class ClienteInvalidoException extends RuntimeException {
    public ClienteInvalidoException(String msg) {
        super(msg);
    }
}