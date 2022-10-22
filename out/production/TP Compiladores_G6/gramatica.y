%{
package compilador;
import accionesSemanticas.AccionSemantica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
%}
        //declaracion de tokens a recibir del Analizador Lexico
%token ID cte cadena If Then Else end_if out fun Return Break When Do
 until comp_menor_igual comp_mayor_igual comp_distinto assign
 ui16 f64 defer

%left '+' '-'
%left '*' '/'
%left '>' '<'

%start program

%%      //declaracion de la gramatica del lenguaje

program: header_program begin cuerpo_prog end ';'
		| begin cuerpo_prog end ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba nombre del programa");}
	    | header_program begin end ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaban sentencias de ejecucion");}
		| header_program begin cuerpo_prog ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '}' al final del programa");}
		| header_program cuerpo_prog end ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '{' antes de las sentencias");}
		| header_program begin cuerpo_prog end {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un ';' al final del programa");}
		| ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un programa");}
;

header_program: nombre_programa { Parser.declarando = false;}
;

begin: '{' {Parser.declarando = true;}
;

end: '}'
;

nombre_programa: ID
;

cuerpo_prog: declaracion ejecucion
			| declaracion {Parser.declarando = false;}
			| ejecucion
			| ejecucion declaracion {agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
			| declaracion ejecucion declaracion {agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
;

//reglas de declaraciones y bloques de sentencias
declaracion: declaracion_variables declaracion_funcion {Parser.declarando = false;}
        | declaracion_variables {Parser.declarando = false;}
        | declaracion_funcion {Parser.declarando = false;}
;

declaracion_variables: tipo list_var ';'
					 | declaracion_variables tipo list_var ';'
					 | list_var ';'
;

list_var: ID{ int ptr_id = TablaSimbolos.obtenerSimbolo($1.sval + Parser.ambito.toString());
 		TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");}
        | list_var ',' ID{ int ptr_id = TablaSimbolos.obtenerSimbolo($3.sval + Parser.ambito.toString());
        		   TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                           TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");}
;

declaracion_funcion: declaracion_funcion funcion
                | funcion
;

funcion: header_funcion '(' list_de_parametros ')' asignacion_tipo '{' cuerpo_de_la_funcion '}'
		|  header_funcion '(' list_de_parametros ')' asignacion_tipo '{' '}' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera el cuerpo de la funcion");}
;

header_funcion: fun ID {int ptr_id = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambito.toString());
			TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaTipos.fun_TYPE);
			TablaSimbolos.agregarAtributo(ptr_id, "uso", TablaTipos.fun_TYPE);}
            | fun {agregarError(errores_sintacticos, Parser.ERROR, "Se espera nombre de la funcion");}
;

cuerpo_de_la_funcion: declaracion ejecucion_funcion
        | ejecucion_funcion
;

list_de_parametros:
	           | parametro
	           | parametro ',' parametro

;

parametro: tipo ID { int ptr_id = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambito.toString());
		     int primerSeparador = Parser.ambito.toString().indexOf(NAME_MANGLING_CHAR);
                     int ultimoSeparador = Parser.ambito.toString().lastIndexOf(NAME_MANGLING_CHAR);
                     String nombre_funcion = Parser.ambito.substring(ultimoSeparador + 1) + Parser.ambito.substring(primerSeparador, ultimoSeparador);
                     int ptr_fun = TablaSimbolos.obtenerSimbolo(nombre_funcion);
		     TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
		     TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");
                     TablaSimbolos.agregarAtributo(ptr_fun, "tipo_parametro", tipo);	}
          | ID {agregarError(errores_sintacticos, Parser.ERROR, "Se espera el tipo del parametro");}
          | tipo {agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
;

tipo: f64 {tipo = TablaTipos.f64_TYPE;}
    | ui16 {tipo = TablaTipos.ui16_TYPE;}
;

ejecucion_funcion:  bloque_funcion {}
			| defer bloque_funcion {}
;

bloque_funcion: bloque_funcion sentencia_funcion
        		| sentencia_funcion
;

sentencia_funcion: sentencia
;

ejecucion: ejecucion sentencia
			| sentencia
;

sentencia: sentencia_ejecutable
;

sentencia_ejecutable: asignacion';'
                | seleccion';'
                | impresion ';'
                | do_until ';'
                | etiqueta ':' do_until ';'
;

pbreak: Break
		| Break ':' etiqueta {}
;

do_until: pdo bloque_sentencias_do until '(' condicion ')' ':' '(' asignacion ')'
		| pdo until '(' condicion ')' ':' '(' asignacion ')' { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre Do y until"); }
;

pdo: Do
;

bloque_sentencias_do: '{' sentencia_ejecutable_do Return '(' expresion ')' ';' '}'
					| '{' sentencia_ejecutable_do '}'
					| '{' Return '(' expresion ')' ';' '}'
					| '{' '}' { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre Do y until"); }
                    | sentencia_ejecutable_do '}' { agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '{' "); }
;

sentencia_ejecutable_do: sentencia_do
                        | sentencia_ejecutable_do sentencia_do
;

sentencia_do:    do_until ';'
				| etiqueta ':' do_until ';'
				| etiqueta ':' do_until { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
				| do_until { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
                | impresion ';'
                | impresion { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
                | seleccion_en_do ';'
                | seleccion_en_do { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
                | asignacion ';'
                | pbreak ';'
                | pbreak { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
;

seleccion_en_do: If condicion_salto_if then_seleccion_do end_if
        | If condicion_salto_if then_seleccion_do else_seleccion_do end_if
        | If condicion_salto_if bloque_sentencias_do end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera 'Then' luego de la condicion del If"); }
        | If condicion_salto_if then_seleccion_do bloque_sentencias_do end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera Else"); }
        | If condicion_salto_if Then end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
        | If condicion_salto_if then_seleccion_do Else end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del Else"); }
        | If condicion_salto_if Then else_seleccion_do end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del Then"); }
;

then_seleccion_do: Then bloque_sentencias_do ;
				| Then ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del Then");}

;

else_seleccion_do: Else bloque_sentencias_do ;
				| Else ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del Else ");}
;
 
seleccion: If condicion_salto_if then_seleccion end_if
        | If condicion_salto_if then_seleccion else_seleccion end_if
        | If condicion_salto_if begin ejecucion end ';' else_seleccion end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera Then"); }
        | If condicion_salto_if then_seleccion begin ejecucion end ';' end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera Else"); }
        | If condicion_salto_if Then end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
        | If condicion_salto_if then_seleccion Else end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del Else"); }
        | If condicion_salto_if Then else_seleccion end_if { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del Then"); }
;

then_seleccion: Then ejecucion ';'
		| Then ejecucion Return '(' expresion ')' ';'
		| Then Return '(' expresion ')' ';'
		| Then '(' expresion ')' ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada Return");}
		| Then Return ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
		| Then ';' { agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del Then");}
		| Then ejecucion { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';' "); }

;

else_seleccion: Else ejecucion ';'
		| Else ejecucion Return '(' expresion ')' ';'
        | Else Return '(' expresion ')' ';'
        | Else '(' expresion ')' ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada Return");}
        | Else Return ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
		| Else ';' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del Else ");}
		| Else ejecucion { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';' "); }
;


condicion_salto_if: '(' condicion ')'
		| condicion ')'{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera '(' al principio de la condicion");}
		| '(' ')'      { agregarError(errores_sintacticos, Parser.ERROR, "Se espera una condicion");}
;


condicion: expresion_bool
        | condicion comparador expresion_bool
;

expresion_bool: expresion comparador expresion
		| expresion comparador {agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion luego del comparador");}
        | comparador expresion {agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion antes del comparador");}
;

comparador: comp_distinto
        | '='
        | comp_mayor_igual
        | comp_menor_igual
        | '<'
        | '>'
;

asignacion_tipo: ':' tipo {int ptr_id = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambito.toString());
 				TablaSimbolos.agregarAtributo(ptr_id, "retorno", tipo);}
			| ':' {agregarError(errores_sintacticos, Parser.ERROR, "Se debe definir el tipo");}
			| {agregarError(errores_sintacticos, Parser.ERROR, "Se debe definir el tipo");}
;

asignacion: ID assign '(' expresion ')' {}
			| ID assign expresion
			| assign expresion {agregarError(errores_sintacticos, Parser.ERROR, "Se espera un identificador en el lado izquierdo de la asignacion");}
			| ID expresion  {agregarError(errores_sintacticos, Parser.ERROR, "Se espera el simbolo de asignacion  entre el identificador y la expresion");}
            | ID assign  {agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion ");}
;

expresion: expresion '+' termino_positivo
        | expresion '-' termino_positivo
        | termino
;

termino: termino '*' factor
        | termino '/' factor
		| factor
;

termino_positivo: termino_positivo '*' factor
            | termino_positivo '/' factor
            | factor_positivo
;

factor: ID
        | constante
        | ID '('list_parametros_reales')'{}
;

factor_positivo: ID
                | cte
                | ID '('list_parametros_reales')'{}
;

constante: cte{int ptr_id = TablaSimbolos.obtenerSimbolo($1.sval);
               TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");}
        | '-' cte{int ptr_id = TablaSimbolos.obtenerSimbolo($2.sval);
		  TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
		  String lexema = negarConstante($2.sval);}
;

impresion: out '(' cadena ')' { String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                                String valor = $3.sval;
                                String tipo = "string";
                                TablaSimbolos.agregarSimbolo(nombre);
                                int puntero = TablaSimbolos.obtenerSimbolo(nombre);
                                TablaSimbolos.agregarAtributo(puntero, "valor", valor);
                                TablaSimbolos.agregarAtributo(puntero, "tipo", tipo);}
		| out '(' ')' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera un mensaje dentro del out ");}
		| out cadena ')' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '(' antes del inicio del comentario a imprimir");}
		| out '(' cadena {agregarError(errores_sintacticos, Parser.ERROR, "Se espera un ')' luego del comentario a imprimir");}
;

etiqueta: ID
;

list_parametros_reales: {}
	   	| parametro_real {}
        | parametro_real ',' parametro_real {}
;

parametro_real: ID{}
			| constante {}
;

%%
public static boolean declarando = true;

public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = ".";

public static StringBuilder ambito = new StringBuilder();

public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> errores_semanticos = new ArrayList<>();

private static boolean errores_compilacion;

private static String tipo;
private static int contador_cadenas = 0;
public static final String STRING_CHAR = "‘";
void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

int yylex() {
        int identificador_token = 0;
        Reader lector = AnalizadorLexico.lector;
        AnalizadorLexico.estado_actual = 0;

        // Leo hasta que el archivo termine
        while (true) {
                try {
                        if (FileHelper.endOfFile(lector)) {
                                break;
                        }

                        char caracter = FileHelper.getNextCharWithoutAdvancing(lector);
                        System.out.println(caracter);
                        identificador_token = AnalizadorLexico.cambiarEstado(lector, caracter);

                        // Si llego a un estado final
                        if (identificador_token != AccionSemantica.TOKEN_ACTIVO) {
                                yylval = new ParserVal(AnalizadorLexico.token_actual.toString());
                                AnalizadorLexico.token_actual.delete(0, AnalizadorLexico.token_actual.length());
                                return identificador_token;
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        return identificador_token;
}

public String negarConstante(String constante) {
        // Si la constante es un numero f64, la negamos antes de que se agrege a la tabla de simbolos
        int puntero = TablaSimbolos.obtenerSimbolo(constante);
        String nuevo_lexema;

        if (constante.contains(".")) {
                nuevo_lexema = '-' + constante;
        } else {
                agregarError(errores_sintacticos, Parser.WARNING, "El numero largo -" + constante +
                                " fue truncado al valor minimo, ya que es menor que este mismo");
                nuevo_lexema = "0";
        }

        TablaSimbolos.agregarAtributo(puntero, TablaSimbolos.LEXEMA, nuevo_lexema);
        return nuevo_lexema;
}

public static void crearPunteroFuncion(String puntero_funcion, String funcion) {
        //tomo el tipo de dato de funcion_asignada y funcion de la tabla de simbolos
        int puntero_funcion_asignada = TablaSimbolos.obtenerSimbolo(puntero_funcion);
        int puntero_funcion_declarada = TablaSimbolos.obtenerSimbolo(funcion);

        String tipo_puntero = TablaSimbolos.obtenerAtributo(puntero_funcion_asignada, "tipo");
        String tipo_funcion = TablaSimbolos.obtenerAtributo(puntero_funcion_declarada, "tipo");
        //pregunto si ninguno de ellos es distinto del tipo string
        if (tipo_puntero.equals(TablaTipos.fun_TYPE) && tipo_funcion.equals(TablaTipos.fun_TYPE)){
                //verifico que el atributo 'uso' del simbolo puntero sea: PUNTERO_FUNCION
                String uso_puntero = TablaSimbolos.obtenerAtributo(puntero_funcion_asignada, "uso");

                if (uso_puntero.equals("variable")) {
                        //agrego a los atributos de puntero_funcion todos los atributos de funcion en la tabla de simbolos, con excepcion del atributo 'uso' y 'lexema'
                        Map<String,String> atributos = TablaSimbolos.obtenerAtributos(puntero_funcion_declarada);
                        assert atributos != null;

                        for (String atributo : atributos.keySet()) {
                                if (atributo.equals("uso") || atributo.equals("lexema")) continue;  //no agrego el atributo uso

                                TablaSimbolos.agregarAtributo(puntero_funcion_asignada, atributo, atributos.get(atributo));
                        }
                }
        }
}
public static void agregarError(List<String> errores, String tipo, String error) {
        if (tipo == Parser.ERROR) {
                errores_compilacion = true;
        }

        int linea_actual = AnalizadorLexico.getLineaActual();
        errores.add(tipo + " (Linea " + linea_actual + "): " + error);
}


public static boolean pertenece(String simbolo) {
        // funcion recursiva para controlar si un simbolo se encuentra en la tabla de simbolos
        if (!simbolo.contains(NAME_MANGLING_CHAR)) {
                return false;
        } else if (TablaSimbolos.obtenerSimbolo(simbolo) != TablaSimbolos.NO_ENCONTRADO) {
                return true;
        } else {
                int index = simbolo.lastIndexOf(NAME_MANGLING_CHAR);
                simbolo = simbolo.substring(0, index);
                return pertenece(simbolo);
        }
}



//--FUNCIONES DE IMPRESION Y MAIN--//

public static void imprimirErrores(List<String> errores, String cabecera) {
        // Imprimo los errores encontrados en el programa
        if (!errores.isEmpty()) {
                System.out.println();
                System.out.println(cabecera + ":");

                for (String error: errores) {
                        System.out.println(error);
                }
        }
}


public static void main(String[] args) {
        if (1== 1) {
                String archivo_a_leer = ("S:\\TP Compiladores_G6\\Compilaodres_g6\\src\\compilador\\Test_Basico.txt");
                System.out.println("Se esta compilando el siguiente archivo: " + archivo_a_leer);

                try {
                        AnalizadorLexico.lector = new BufferedReader(new FileReader(archivo_a_leer));
                        Parser parser = new Parser();
                        parser.run();
                } catch (IOException excepcion) {
                        excepcion.printStackTrace();
                }

				TablaSimbolos.imprimirTabla();
                Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
                Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
                }
        }
