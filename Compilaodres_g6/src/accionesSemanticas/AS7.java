package accionesSemanticas;

import compilador.TablaPalabrasReservadas;

import java.io.IOException;
import java.io.Reader;

public class AS7 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        try {
            char siguiente_caracter = (char) lector.read(); // Lee el siguiente caracter
            token.append(siguiente_caracter); // Concatena el caracter actual
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }

        String simbolo = token.toString();

        return TablaPalabrasReservadas.obtenerIdentificador(simbolo);
    }
}
