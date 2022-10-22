package accionesSemanticas;

import compilador.AnalizadorLexico;
import compilador.Parser;

import java.io.IOException;
import java.io.Reader;

public class ASE implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        try {
            char caracter = (char) lector.read();
            Parser.agregarError(Parser.errores_lexicos, Parser.ERROR, "\"" + token + caracter + '\"');

            if (caracter == AnalizadorLexico.NUEVA_LINEA) {
                int linea_actual = AnalizadorLexico.getLineaActual();
                AnalizadorLexico.setLineaActual(linea_actual + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ERROR_ENCONTRADO;
    }
}
