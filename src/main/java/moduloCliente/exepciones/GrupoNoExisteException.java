package moduloCliente.exepciones;

public class GrupoNoExisteException extends RuntimeException {
    public GrupoNoExisteException(String msg) {
        super(msg);
    }
}
