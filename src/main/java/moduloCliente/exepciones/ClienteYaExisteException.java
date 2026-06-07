package moduloCliente.exepciones;

public class ClienteYaExisteException extends RuntimeException {
    public ClienteYaExisteException(String msg) {
        super(msg);
    }
}
