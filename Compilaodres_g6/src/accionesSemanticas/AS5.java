package accionesSemanticas;

import compilador.AnalizadorLexico;
import compilador.Parser;
import compilador.TablaSimbolos;
import compilador.TablaTipos;

import java.io.Reader;

public class AS5 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        String simbolo = token.toString();

        try {
            long valor_simbolo = Long.parseLong(simbolo);

            if (valor_simbolo > AnalizadorLexico.MAX_INT_VALUE) {
                Parser.agregarError(Parser.errores_lexicos, Parser.WARNING, "El numero largo " + simbolo +
                                            " fue truncado al valor maximo, ya que supera a este mismo");
                simbolo = Double.toString(AnalizadorLexico.MAX_INT_VALUE);
            }
        } catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }

        if (TablaSimbolos.obtenerSimbolo(simbolo) == TablaSimbolos.NO_ENCONTRADO) {
            TablaSimbolos.agregarSimbolo(simbolo);
            int ptr_id = TablaSimbolos.obtenerSimbolo(simbolo);
            TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaTipos.ui16_TYPE);
        }

        return AnalizadorLexico.CONSTANTE;
    }
}
