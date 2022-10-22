package compilador;

public class TablaTipos {
    public static final int ui16 = 0;
    public static final int f64 = 1;
    public static final int fun = 2;

    public static final String f64_TYPE = "f64";
    public static final String ui16_TYPE = "ui16";
    public static final String fun_TYPE = "funcion";
    public static final String STR_TYPE = "string";
    public static final String ERROR_TYPE = "error";

    private static final String[][] tiposSumaResta = { { ui16_TYPE, f64_TYPE, ERROR_TYPE },
                                                       { f64_TYPE, f64_TYPE, ERROR_TYPE },
                                                       { ERROR_TYPE, ERROR_TYPE, ERROR_TYPE} };
    private static final String[][] tiposMultDiv = { { ui16_TYPE, f64_TYPE, ERROR_TYPE },
                                                     { f64_TYPE, f64_TYPE, ERROR_TYPE },
                                                     { ERROR_TYPE, ERROR_TYPE, ERROR_TYPE} };
    private static final String[][] tiposComparadores = { { ui16_TYPE, f64_TYPE, ERROR_TYPE },
                                                          { f64_TYPE, f64_TYPE, ERROR_TYPE },
                                                          { ERROR_TYPE, ERROR_TYPE, ERROR_TYPE } };
    private static final String[][] tiposAsig = { { ui16_TYPE, ERROR_TYPE, ERROR_TYPE },
                                                  { f64_TYPE, f64_TYPE, ERROR_TYPE },
                                                  { ERROR_TYPE, ERROR_TYPE, fun_TYPE } };

    private static String tipoResultante(String op1, String op2, String operador) {
        int fil = getNumeroTipo(op1);
        int col = getNumeroTipo(op2);

        switch (operador) {
            case ("+"):
            case ("-"):
                return tiposSumaResta[fil][col];
            case ("*"):
            case ("/"):
                return tiposMultDiv[fil][col];
            case (":="):
                return tiposAsig[fil][col];
            case ("<="):
            case (">="):
            case ("<>"):
            case ("=="):
                return tiposComparadores[fil][col];
            default:
                return ERROR_TYPE;
        }
    }

    private static int getNumeroTipo(String tipo) {
        if (tipo.equals(ui16_TYPE)) return ui16;
        else if (tipo.equals(f64_TYPE)) return f64;
        else return fun;
    }
}