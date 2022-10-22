package accionesSemanticas;

import compilador.AnalizadorLexico;

import java.io.IOException;
import java.io.Reader;

public class AS9 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        token.delete(0, token.length()); // Reinicia el token

        try {
            lector.read(); // Lee el siguiente caracter
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }

        return AnalizadorLexico.CADENA;
    }
}
