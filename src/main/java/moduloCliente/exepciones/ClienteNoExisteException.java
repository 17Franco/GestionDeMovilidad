package moduloCliente.exepciones;

public class ClienteNoExisteException extends RuntimeException {
    public ClienteNoExisteException(String message) {
        super(message);
    }
}
