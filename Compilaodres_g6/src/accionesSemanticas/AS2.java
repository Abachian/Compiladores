package accionesSemanticas;

import java.io.IOException;
import java.io.Reader;

public class AS2 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        try {
            return lector.read();
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }

        return ERROR_ENCONTRADO;
    }
}
