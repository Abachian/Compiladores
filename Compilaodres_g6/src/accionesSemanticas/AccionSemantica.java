package accionesSemanticas;

import java.io.Reader;

public interface AccionSemantica {
    int TOKEN_ACTIVO = -1;
    int ERROR_ENCONTRADO = -2;

    int ejecutar(Reader lector, StringBuilder token);
}
