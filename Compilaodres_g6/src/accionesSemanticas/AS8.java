package accionesSemanticas;

import java.io.Reader;

public class AS8 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        return token.charAt(0);
    }
}
