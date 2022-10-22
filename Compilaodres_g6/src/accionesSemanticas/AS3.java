package accionesSemanticas;

import compilador.AnalizadorLexico;

import compilador.Parser;
import compilador.TablaPalabrasReservadas;
import compilador.TablaSimbolos;

import java.io.Reader;

public class AS3 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        System.out.println("accion semantica 3");
        int identificador;
        String simbolo = token.toString();
        int id_palabra_reservada = TablaPalabrasReservadas.obtenerIdentificador(simbolo);

        if (id_palabra_reservada != TablaPalabrasReservadas.PALABRA_NO_RESERVADA) {
            System.out.println("es palabra reservada");
            identificador = id_palabra_reservada;
        } else {
            if (token.length() > AnalizadorLexico.LONGITUD_IDENTIFICADOR) {
                simbolo = token.substring(0, AnalizadorLexico.LONGITUD_IDENTIFICADOR);
                Parser.agregarError(Parser.errores_lexicos, Parser.WARNING, "El identificador " + token +
                                            " fue truncado debido a que supero la longitud maxima de " +
                                            AnalizadorLexico.LONGITUD_IDENTIFICADOR + " caracteres");
            }

            boolean simbolo_encontrado = TablaSimbolos.obtenerSimbolo(simbolo) != TablaSimbolos.NO_ENCONTRADO;

            System.out.println("parser declarando: " + Parser.declarando);
            if (Parser.declarando && !simbolo_encontrado) {
                // Si se esta declarando y el simbolo no esta en la tabla, se agrega
                System.out.println("simbolo no definido");
                TablaSimbolos.agregarSimbolo(simbolo);
            } else if (Parser.declarando) {
                // Si se esta declarando y el simbolo esta en la tabla, quiere decir que ya fue definido
                System.out.println("simbolo ya definido");
                Parser.agregarError(Parser.errores_semanticos, Parser.ERROR, "El identificador " + token + " ya fue declarado");
            }

            identificador = AnalizadorLexico.IDENTIFICADOR;
        }

        return identificador;
    }
}
