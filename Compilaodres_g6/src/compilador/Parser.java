//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package compilador;
import accionesSemanticas.AccionSemantica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
//#line 26 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short cte=258;
public final static short cadena=259;
public final static short If=260;
public final static short Then=261;
public final static short Else=262;
public final static short end_if=263;
public final static short out=264;
public final static short fun=265;
public final static short Return=266;
public final static short Break=267;
public final static short When=268;
public final static short Do=269;
public final static short until=270;
public final static short comp_menor_igual=271;
public final static short comp_mayor_igual=272;
public final static short comp_distinto=273;
public final static short assign=274;
public final static short ui16=275;
public final static short f64=276;
public final static short defer=277;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    4,
    5,    3,    3,    3,    3,    3,    6,    6,    6,    8,
    8,    8,   11,   11,    9,    9,   12,   12,   13,   13,
   16,   16,   14,   14,   14,   18,   18,   18,   10,   10,
   17,   17,   19,   19,   20,    7,    7,   21,   22,   22,
   22,   22,   22,   28,   28,   26,   26,   29,   30,   30,
   30,   30,   30,   32,   32,   34,   34,   34,   34,   34,
   34,   34,   34,   34,   34,   34,   35,   35,   35,   35,
   35,   35,   35,   37,   37,   38,   38,   24,   24,   24,
   24,   24,   24,   24,   39,   39,   39,   39,   39,   39,
   39,   40,   40,   40,   40,   40,   40,   40,   36,   36,
   36,   31,   31,   41,   41,   41,   42,   42,   42,   42,
   42,   42,   15,   15,   15,   23,   23,   23,   23,   23,
   33,   33,   33,   44,   44,   44,   43,   43,   43,   45,
   45,   45,   46,   46,   46,   47,   47,   25,   25,   25,
   25,   27,   48,   48,   48,   49,   49,
};
final static short yylen[] = {                            2,
    5,    4,    4,    4,    4,    4,    1,    1,    1,    1,
    1,    2,    1,    1,    2,    3,    2,    1,    1,    3,
    4,    2,    1,    3,    2,    1,    8,    7,    2,    1,
    2,    1,    0,    1,    3,    2,    1,    1,    1,    1,
    1,    2,    2,    1,    1,    2,    1,    1,    2,    2,
    2,    2,    4,    1,    3,   10,    9,    1,    8,    3,
    7,    2,    2,    1,    2,    2,    4,    3,    1,    2,
    1,    2,    1,    2,    2,    1,    4,    5,    4,    5,
    4,    5,    5,    2,    2,    2,    2,    4,    5,    8,
    8,    4,    5,    5,    3,    7,    6,    5,    3,    2,
    2,    3,    7,    6,    5,    3,    2,    2,    3,    2,
    2,    1,    3,    3,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    0,    5,    3,    2,    2,    2,
    3,    3,    1,    3,    3,    1,    3,    3,    1,    1,
    1,    4,    1,    1,    4,    1,    2,    4,    3,    3,
    3,    1,    0,    1,    3,    1,    1,
};
final static short yydefred[] = {                         0,
   11,    7,    9,    0,    0,    0,    8,    0,    0,    0,
    0,   58,    0,   40,   39,    0,    0,    0,    0,    0,
    0,    0,    0,   26,    0,   47,   48,    0,    0,    0,
    0,    0,    0,    0,    0,  146,    0,    0,    0,    0,
  136,  141,  120,  119,  117,  122,  121,    0,  118,    0,
    0,    0,  112,    0,    0,    0,   29,    0,   10,    0,
    0,    0,    0,    0,   15,   46,    0,    0,   25,   23,
    0,   22,    0,    0,   49,   50,   51,   52,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   64,    0,    0,    0,    0,    0,  147,    0,    0,    0,
    0,  111,    0,  110,    0,    0,    0,    0,    0,    0,
  150,    0,  149,    4,    0,    3,    5,   16,    0,   20,
   24,   37,    0,    0,    0,    0,    0,    0,    0,    0,
   62,    0,   74,   70,   66,    0,   75,    0,   63,   65,
   72,    2,  156,  157,    0,    0,    0,    0,  144,    0,
  139,    0,  134,  135,  109,  113,    0,    0,   92,    0,
  100,    0,    0,    0,    0,    0,   88,    0,    0,  148,
    1,   21,   36,    0,    0,   53,    0,    0,    0,  152,
   55,    0,    0,    0,   60,    0,    0,  142,    0,  126,
    0,    0,    0,    0,  107,    0,    0,   99,    0,    0,
    0,   95,   94,    0,   93,    0,   89,    0,    0,   35,
    0,   81,   85,   84,    0,   79,    0,   77,    0,    0,
    0,    0,    0,   67,    0,  155,    0,  137,  138,  106,
    0,    0,    0,  102,    0,    0,    0,    0,    0,  123,
    0,   87,   86,   83,   82,   80,   78,    0,    0,    0,
    0,  145,    0,    0,    0,    0,   98,    0,    0,    0,
    0,   28,    0,    0,   32,    0,   44,   45,    0,    0,
    0,    0,    0,  105,    0,   97,    0,   90,   91,    0,
   31,   27,   43,    0,    0,   61,    0,    0,  104,    0,
   96,   57,   59,    0,  103,   56,
};
final static short yydgoto[] = {                          4,
    5,    6,   17,   61,    7,   18,   19,   20,   21,   22,
   23,   24,   25,  124,  209,  264,  265,  125,  266,  267,
   26,   27,   28,   29,   30,   31,   32,   88,   33,  243,
   50,   90,   51,   91,   92,   52,  179,  215,  109,  164,
   53,   54,  150,   40,   41,  151,   42,  145,  146,
};
final static short yysindex[] = {                       -50,
    0,    0,    0,    0,  -70,  492,    0,  -39,    9,  -33,
 -224,    0,  -37,    0,    0,  321,  -51,  505,  492,  -63,
 -188, -150,  137,    0,   73,    0,    0,   76,   99,  144,
  151,  226,  276,  -51,  222,    0,  -30,   29,  280,  271,
    0,    0,    0,    0,    0,    0,    0,   31,    0,   68,
  116, -101,    0,  -37,  255,  -18,    0,  280,    0,  -42,
  248,  251,  -39,  492,    0,    0, -188, -150,    0,    0,
  261,    0,   74,  -75,    0,    0,    0,    0,   53,    9,
  275,  307,  396,  296,  298,  301,  290,  305,  115,  484,
    0,  327,  333,   14,  -37,  280,    0, -190, -190,  -37,
  -37,    0,   71,    0,  -41,  -37,  -24,  505, -112,  280,
    0,  354,    0,    0,  337,    0,    0,    0,  262,    0,
    0,    0,  147,  364,  370,  358,  383,  163,  -41,  382,
    0,  409,    0,    0,    0,   53,    0,  385,    0,    0,
    0,    0,    0,    0,  386,  389,  289,  390,    0,  277,
    0,  277,    0,    0,    0,    0,  280,    4,    0,  -27,
    0,  -37,  155,  171,  486,   -9,    0,  505,  177,    0,
    0,    0,    0,  398,  -75,    0,  175,  184,  367,    0,
    0,   81,  -37,  425,    0,  416,  -41,    0,   14,    0,
   14,  -37,  -37,  -22,    0,  -37,  250,    0,  -37,  320,
  426,    0,    0,  421,    0,  486,    0,   70,  336,    0,
  235,    0,    0,    0,  218,    0,  194,    0,  220,  224,
  430,  328,  -37,    0,   95,    0,  448,    0,    0,    0,
  -37,  400,  456,    0,  407,  458,  -37,  256,  461,    0,
  291,    0,    0,    0,    0,    0,    0,  457,  466,  419,
  468,    0,  428,  469,  -37,  472,    0,  470,  274,  278,
  505,    0,  368,  404,    0,  505,    0,    0, -232,  413,
  483,  504,  488,    0,  516,    0,  494,    0,    0,  505,
    0,    0,    0,  -39,  517,    0,  437, -232,    0,  510,
    0,    0,    0,  523,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,  139,    0,    0,
  530,    0,    0,    0,    0,    0,    0,  -31,  -29,  133,
  134,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   19,    0,   -7,    0,   -2,   56,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  236,    0,    0,
    0,    0,  513,  -14,    0,    0,  149,    0,    0,    0,
    0,    0,    0,  532,    0,    0,    0,    0,    0,    0,
  210,    0,    0,    0,  427,  438,    0,  451,    0,    0,
    0,  462,    0,  536,    0,  238,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  103,    0,    0,    0,  108,
    0,  234,    0,    0,  579,    0,    0,    0,    0,    0,
    0,    0,   -3,    0,  542,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  543,    0,   43,    0,   65,
    0,   78,    0,    0,    0,    0,  111,    0,    0,    0,
    0,    0,  -96,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  465,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  473,    0,    0,    0,    0,
  536,    0,    0,    0,    0,    0,  326,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  471,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  467,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  475,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  247,   59,  266,    0,  -16,  204,    0,  571,  100,
   21,   15,    0,    0,    0,    0,  330,  429,  341, -254,
  -17,    0,  -32,    0,  422,  403,  395,    0,    0,    7,
  114,  520,   -8,  -76,    0,  525,    0,  431,    0,  -85,
  502,  350,  509,    0,  185,    0,   20,  424,  432,
};
final static int YYTABLESIZE=779;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         39,
   84,   66,   65,   38,   58,   38,   56,   38,    2,   95,
    3,  283,  199,  140,   38,  162,  114,  231,   47,   49,
   46,    3,  113,  169,  284,  283,  101,   13,   96,   14,
  196,  198,   57,  130,  161,   69,  230,   38,  129,   89,
   38,   13,   71,  196,   12,  110,   66,  118,   48,  195,
   84,  130,    3,   38,   39,  140,  129,   84,   38,  140,
  140,  140,  195,  140,   34,  140,  148,  149,   47,   49,
   46,  102,    3,   59,   60,   38,   11,  140,  140,  140,
  140,   69,   59,  143,  143,  143,  147,  143,  119,  143,
   47,   49,   46,   13,   84,   14,  133,  157,  133,   84,
  133,  143,  143,  143,  143,  131,   70,  131,  104,  131,
   12,  155,   74,  144,  133,  133,  133,  133,  132,   68,
  132,  221,  132,  131,  131,  131,  131,   47,   49,   46,
   47,   49,   46,  178,   75,  251,  132,  132,  132,  132,
   47,   49,   46,  115,   84,   66,   84,   66,  116,  166,
  167,  114,  259,  200,   47,   49,   46,   76,   98,  107,
   99,  103,  115,  115,  115,  101,  101,  116,  116,  116,
  114,  114,  114,  123,  222,   47,   49,   46,   84,   66,
   73,  122,   23,  214,   84,  219,    8,  232,   66,    9,
  235,   18,   19,   10,   11,   72,  152,   23,   12,   14,
   15,   11,   77,   13,   14,   15,    1,   17,  144,   78,
  144,   14,   15,  202,  250,   35,   36,   35,   36,   35,
   36,   64,  253,  268,  263,   55,   35,   36,  258,   43,
   44,   45,   63,  213,   37,    9,  285,  158,  159,   10,
  112,  160,  182,  268,   12,  268,  275,   63,  268,   13,
    9,   16,  242,  205,   10,  294,  194,   18,   19,   12,
   63,   94,  268,    9,   13,   35,   36,   10,   54,  194,
  143,   36,   12,   17,  123,   39,  128,   13,  127,   43,
   44,   45,   62,   79,  153,  154,   97,   35,   36,  140,
  140,  140,  151,  242,  128,  111,  127,   83,  108,   93,
  225,   43,   44,   45,   73,   73,  116,  240,  234,  117,
  163,  165,  100,  143,  143,  143,   83,  101,  192,  120,
  172,   12,   98,  193,   99,  115,  133,  133,  133,  190,
  121,   98,  128,   99,   54,  131,  131,  131,   43,   44,
   45,   43,   44,   45,   14,   15,  129,  136,  132,  132,
  132,   43,   44,   45,  133,  168,  134,   83,  151,  135,
  236,  197,   98,  137,   99,   43,   44,   45,  249,  197,
   98,  206,   99,  115,  115,  115,  228,  229,  116,  116,
  116,  114,  114,  114,  138,  141,   43,   44,   45,   18,
   19,  142,   18,   19,  170,  171,   18,   19,   83,  105,
  106,   18,   19,  173,  174,   17,   18,   19,   17,   18,
   19,   63,   17,  175,    9,  262,  176,   17,   10,  180,
  201,  183,   17,   12,  187,   17,  188,   87,   13,  191,
  204,   63,  189,  203,   80,   86,  211,  212,   10,  207,
  254,   81,   98,   12,   99,   59,  216,  256,   13,   98,
   63,   99,  105,   80,   85,  208,  245,   10,  241,  271,
   81,   98,   12,   99,  223,  237,   54,   13,  273,   54,
   98,  239,   99,   54,  224,   54,   54,   87,   54,  238,
  244,  126,  246,   54,   87,   86,  247,  248,  252,   83,
  151,   63,   86,  151,   80,  255,  269,  151,   10,  151,
  151,   81,  151,   12,   85,   83,   63,  151,   13,    9,
  277,   85,   98,   10,   99,  233,  257,  158,   12,  260,
  131,   87,  181,   13,  270,  272,   87,  274,  282,   86,
  276,  105,   63,  185,   86,   80,  278,  286,  186,   10,
  279,  287,   81,  288,   12,   82,  289,    8,   85,   13,
    9,   71,  291,   85,   10,   11,  290,  292,   98,   12,
   99,  293,   69,  296,   13,   14,   15,  261,  295,   30,
  152,   87,   33,   87,  105,   76,  153,    8,    6,   86,
    9,   86,   34,  154,   10,   11,   73,  125,  108,   12,
   67,   41,  281,  124,   13,   14,   15,   68,   85,   42,
   85,  280,  132,  210,  127,   87,  156,  152,  139,  220,
   59,   87,    0,   86,  227,    0,    0,    0,    0,   86,
  226,    0,    0,   63,   63,    0,   80,    9,  217,  218,
   10,   10,   85,   81,    0,   12,   12,    0,   85,   63,
   13,   13,   80,  177,  261,    0,   10,    0,    0,   81,
    0,   12,   63,    0,    0,   80,   13,    0,    0,   10,
    0,  130,   81,    0,   12,   63,    0,    0,   80,   13,
    0,    0,   10,    0,  184,   81,    0,   12,    0,    0,
    0,    0,   13,   71,    0,    0,   71,    0,    0,    0,
   71,    0,   71,   71,   69,   71,    0,   69,    0,    0,
   71,   69,    0,   69,   69,    0,   69,   76,    0,    0,
   76,   69,    0,    0,   76,    0,   76,   76,   73,   76,
    0,   73,    0,    0,   76,   73,    0,   73,   73,   68,
   73,    0,   68,    0,    0,   73,   68,    0,   68,   68,
   63,   68,   63,   80,    0,    9,   68,   10,    8,   10,
   81,    9,   12,    0,   12,   10,   11,   13,    0,   13,
   12,   63,    0,    0,    9,   13,   14,   15,   10,    0,
    0,    0,    0,   12,    0,    0,    0,    0,   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          8,
   33,   19,   19,   45,   13,   45,   40,   45,   59,   40,
  123,  266,   40,   90,   45,   40,   59,   40,   60,   61,
   62,  123,   41,  109,  257,  280,  123,   59,   37,   59,
   40,   59,  257,   41,   59,   21,   59,   41,   41,   33,
   44,  274,   22,   40,   59,   54,   64,   64,   40,   59,
   83,   59,  123,   45,   63,  132,   59,   90,   45,   41,
   42,   43,   59,   45,    6,   47,  257,  258,   60,   61,
   62,   41,  123,  125,   16,   45,  265,   59,   60,   61,
   62,   67,  125,   41,   42,   43,   95,   45,   68,   47,
   60,   61,   62,  125,  127,  125,   41,  106,   43,  132,
   45,   59,   60,   61,   62,   41,  257,   43,   41,   45,
  125,   41,   40,   94,   59,   60,   61,   62,   41,   20,
   43,   41,   45,   59,   60,   61,   62,   60,   61,   62,
   60,   61,   62,  127,   59,   41,   59,   60,   61,   62,
   60,   61,   62,   41,  177,  163,  179,  165,   41,  262,
  263,   41,  238,  162,   60,   61,   62,   59,   43,  261,
   45,   48,   60,   61,   62,  262,  263,   60,   61,   62,
   60,   61,   62,   74,  183,   60,   61,   62,  211,  197,
   44,  257,   44,  177,  217,  179,  257,  196,  206,  260,
  199,   59,   59,  264,  265,   59,   58,   59,  269,  275,
  276,  265,   59,  274,  275,  276,  257,   59,  189,   59,
  191,  275,  276,   59,  223,  257,  258,  257,  258,  257,
  258,   18,  231,  241,  241,  259,  257,  258,  237,  271,
  272,  273,  257,   59,  274,  260,  269,  262,  263,  264,
  259,  266,  129,  261,  269,  263,  255,  257,  266,  274,
  260,    5,   59,  263,  264,  288,  266,  125,  125,  269,
  257,   40,  280,  260,  274,  257,  258,  264,   59,  266,
  257,  258,  269,  125,  175,  284,   41,  274,   41,  271,
  272,  273,   17,   58,  100,  101,  258,  257,  258,  271,
  272,  273,   59,   59,   59,   41,   59,  123,   52,   34,
  187,  271,  272,  273,   44,   44,   59,  208,   59,   59,
  107,  108,   42,  271,  272,  273,  123,   47,   42,   59,
   59,  269,   43,   47,   45,   60,  271,  272,  273,   41,
  257,   43,   58,   45,  125,  271,  272,  273,  271,  272,
  273,  271,  272,  273,  275,  276,   40,   58,  271,  272,
  273,  271,  272,  273,   59,  109,   59,  123,  125,   59,
   41,  158,   43,   59,   45,  271,  272,  273,   41,  166,
   43,  168,   45,  271,  272,  273,  192,  193,  271,  272,
  273,  271,  272,  273,  270,   59,  271,  272,  273,  257,
  257,   59,  260,  260,   41,   59,  264,  264,  123,   50,
   51,  269,  269,  257,   41,  257,  274,  274,  260,  277,
  277,  257,  264,   44,  260,  125,   59,  269,  264,  257,
  266,   40,  274,  269,   40,  277,   41,   33,  274,   40,
  165,  257,   44,  263,  260,   33,  262,  263,  264,  263,
   41,  267,   43,  269,   45,  125,  263,   41,  274,   43,
  257,   45,  103,  260,   33,   58,  263,  264,  123,   41,
  267,   43,  269,   45,   40,   40,  257,  274,   41,  260,
   43,  206,   45,  264,   59,  266,  267,   83,  269,   59,
  263,   79,  263,  274,   90,   83,  263,   58,   41,  123,
  257,  257,   90,  260,  260,   40,   40,  264,  264,  266,
  267,  267,  269,  269,   83,  123,  257,  274,  274,  260,
   41,   90,   43,  264,   45,  266,   59,  262,  269,   59,
  125,  127,  128,  274,   59,   58,  132,   59,  125,  127,
   59,  182,  257,  125,  132,  260,  263,  125,  136,  264,
  263,   59,  267,   40,  269,  270,   59,  257,  127,  274,
  260,  125,   59,  132,  264,  265,   41,   41,   43,  269,
   45,  125,  125,   41,  274,  275,  276,  277,   59,   40,
   58,  177,   41,  179,  225,  125,   41,  257,    0,  177,
  260,  179,   41,   41,  264,  265,  125,  123,  263,  269,
   20,  125,  263,  123,  274,  275,  276,  125,  177,  125,
  179,  261,   83,  175,   80,  211,  105,   99,  125,  179,
  125,  217,   -1,  211,  191,   -1,   -1,   -1,   -1,  217,
  189,   -1,   -1,  257,  257,   -1,  260,  260,  262,  263,
  264,  264,  211,  267,   -1,  269,  269,   -1,  217,  257,
  274,  274,  260,  261,  277,   -1,  264,   -1,   -1,  267,
   -1,  269,  257,   -1,   -1,  260,  274,   -1,   -1,  264,
   -1,  266,  267,   -1,  269,  257,   -1,   -1,  260,  274,
   -1,   -1,  264,   -1,  266,  267,   -1,  269,   -1,   -1,
   -1,   -1,  274,  257,   -1,   -1,  260,   -1,   -1,   -1,
  264,   -1,  266,  267,  257,  269,   -1,  260,   -1,   -1,
  274,  264,   -1,  266,  267,   -1,  269,  257,   -1,   -1,
  260,  274,   -1,   -1,  264,   -1,  266,  267,  257,  269,
   -1,  260,   -1,   -1,  274,  264,   -1,  266,  267,  257,
  269,   -1,  260,   -1,   -1,  274,  264,   -1,  266,  267,
  257,  269,  257,  260,   -1,  260,  274,  264,  257,  264,
  267,  260,  269,   -1,  269,  264,  265,  274,   -1,  274,
  269,  257,   -1,   -1,  260,  274,  275,  276,  264,   -1,
   -1,   -1,   -1,  269,   -1,   -1,   -1,   -1,  274,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=277;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","cte","cadena","If","Then","Else",
"end_if","out","fun","Return","Break","When","Do","until","comp_menor_igual",
"comp_mayor_igual","comp_distinto","assign","ui16","f64","defer",
};
final static String yyrule[] = {
"$accept : program",
"program : header_program begin cuerpo_prog end ';'",
"program : begin cuerpo_prog end ';'",
"program : header_program begin end ';'",
"program : header_program begin cuerpo_prog ';'",
"program : header_program cuerpo_prog end ';'",
"program : header_program begin cuerpo_prog end",
"program : ';'",
"header_program : nombre_programa",
"begin : '{'",
"end : '}'",
"nombre_programa : ID",
"cuerpo_prog : declaracion ejecucion",
"cuerpo_prog : declaracion",
"cuerpo_prog : ejecucion",
"cuerpo_prog : ejecucion declaracion",
"cuerpo_prog : declaracion ejecucion declaracion",
"declaracion : declaracion_variables declaracion_funcion",
"declaracion : declaracion_variables",
"declaracion : declaracion_funcion",
"declaracion_variables : tipo list_var ';'",
"declaracion_variables : declaracion_variables tipo list_var ';'",
"declaracion_variables : list_var ';'",
"list_var : ID",
"list_var : list_var ',' ID",
"declaracion_funcion : declaracion_funcion funcion",
"declaracion_funcion : funcion",
"funcion : header_funcion '(' list_de_parametros ')' asignacion_tipo '{' cuerpo_de_la_funcion '}'",
"funcion : header_funcion '(' list_de_parametros ')' asignacion_tipo '{' '}'",
"header_funcion : fun ID",
"header_funcion : fun",
"cuerpo_de_la_funcion : declaracion ejecucion_funcion",
"cuerpo_de_la_funcion : ejecucion_funcion",
"list_de_parametros :",
"list_de_parametros : parametro",
"list_de_parametros : parametro ',' parametro",
"parametro : tipo ID",
"parametro : ID",
"parametro : tipo",
"tipo : f64",
"tipo : ui16",
"ejecucion_funcion : bloque_funcion",
"ejecucion_funcion : defer bloque_funcion",
"bloque_funcion : bloque_funcion sentencia_funcion",
"bloque_funcion : sentencia_funcion",
"sentencia_funcion : sentencia",
"ejecucion : ejecucion sentencia",
"ejecucion : sentencia",
"sentencia : sentencia_ejecutable",
"sentencia_ejecutable : asignacion ';'",
"sentencia_ejecutable : seleccion ';'",
"sentencia_ejecutable : impresion ';'",
"sentencia_ejecutable : do_until ';'",
"sentencia_ejecutable : etiqueta ':' do_until ';'",
"pbreak : Break",
"pbreak : Break ':' etiqueta",
"do_until : pdo bloque_sentencias_do until '(' condicion ')' ':' '(' asignacion ')'",
"do_until : pdo until '(' condicion ')' ':' '(' asignacion ')'",
"pdo : Do",
"bloque_sentencias_do : '{' sentencia_ejecutable_do Return '(' expresion ')' ';' '}'",
"bloque_sentencias_do : '{' sentencia_ejecutable_do '}'",
"bloque_sentencias_do : '{' Return '(' expresion ')' ';' '}'",
"bloque_sentencias_do : '{' '}'",
"bloque_sentencias_do : sentencia_ejecutable_do '}'",
"sentencia_ejecutable_do : sentencia_do",
"sentencia_ejecutable_do : sentencia_ejecutable_do sentencia_do",
"sentencia_do : do_until ';'",
"sentencia_do : etiqueta ':' do_until ';'",
"sentencia_do : etiqueta ':' do_until",
"sentencia_do : do_until",
"sentencia_do : impresion ';'",
"sentencia_do : impresion",
"sentencia_do : seleccion_en_do ';'",
"sentencia_do : seleccion_en_do",
"sentencia_do : asignacion ';'",
"sentencia_do : pbreak ';'",
"sentencia_do : pbreak",
"seleccion_en_do : If condicion_salto_if then_seleccion_do end_if",
"seleccion_en_do : If condicion_salto_if then_seleccion_do else_seleccion_do end_if",
"seleccion_en_do : If condicion_salto_if bloque_sentencias_do end_if",
"seleccion_en_do : If condicion_salto_if then_seleccion_do bloque_sentencias_do end_if",
"seleccion_en_do : If condicion_salto_if Then end_if",
"seleccion_en_do : If condicion_salto_if then_seleccion_do Else end_if",
"seleccion_en_do : If condicion_salto_if Then else_seleccion_do end_if",
"then_seleccion_do : Then bloque_sentencias_do",
"then_seleccion_do : Then ';'",
"else_seleccion_do : Else bloque_sentencias_do",
"else_seleccion_do : Else ';'",
"seleccion : If condicion_salto_if then_seleccion end_if",
"seleccion : If condicion_salto_if then_seleccion else_seleccion end_if",
"seleccion : If condicion_salto_if begin ejecucion end ';' else_seleccion end_if",
"seleccion : If condicion_salto_if then_seleccion begin ejecucion end ';' end_if",
"seleccion : If condicion_salto_if Then end_if",
"seleccion : If condicion_salto_if then_seleccion Else end_if",
"seleccion : If condicion_salto_if Then else_seleccion end_if",
"then_seleccion : Then ejecucion ';'",
"then_seleccion : Then ejecucion Return '(' expresion ')' ';'",
"then_seleccion : Then Return '(' expresion ')' ';'",
"then_seleccion : Then '(' expresion ')' ';'",
"then_seleccion : Then Return ';'",
"then_seleccion : Then ';'",
"then_seleccion : Then ejecucion",
"else_seleccion : Else ejecucion ';'",
"else_seleccion : Else ejecucion Return '(' expresion ')' ';'",
"else_seleccion : Else Return '(' expresion ')' ';'",
"else_seleccion : Else '(' expresion ')' ';'",
"else_seleccion : Else Return ';'",
"else_seleccion : Else ';'",
"else_seleccion : Else ejecucion",
"condicion_salto_if : '(' condicion ')'",
"condicion_salto_if : condicion ')'",
"condicion_salto_if : '(' ')'",
"condicion : expresion_bool",
"condicion : condicion comparador expresion_bool",
"expresion_bool : expresion comparador expresion",
"expresion_bool : expresion comparador",
"expresion_bool : comparador expresion",
"comparador : comp_distinto",
"comparador : '='",
"comparador : comp_mayor_igual",
"comparador : comp_menor_igual",
"comparador : '<'",
"comparador : '>'",
"asignacion_tipo : ':' tipo",
"asignacion_tipo : ':'",
"asignacion_tipo :",
"asignacion : ID assign '(' expresion ')'",
"asignacion : ID assign expresion",
"asignacion : assign expresion",
"asignacion : ID expresion",
"asignacion : ID assign",
"expresion : expresion '+' termino_positivo",
"expresion : expresion '-' termino_positivo",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino_positivo : termino_positivo '*' factor",
"termino_positivo : termino_positivo '/' factor",
"termino_positivo : factor_positivo",
"factor : ID",
"factor : constante",
"factor : ID '(' list_parametros_reales ')'",
"factor_positivo : ID",
"factor_positivo : cte",
"factor_positivo : ID '(' list_parametros_reales ')'",
"constante : cte",
"constante : '-' cte",
"impresion : out '(' cadena ')'",
"impresion : out '(' ')'",
"impresion : out cadena ')'",
"impresion : out '(' cadena",
"etiqueta : ID",
"list_parametros_reales :",
"list_parametros_reales : parametro_real",
"list_parametros_reales : parametro_real ',' parametro_real",
"parametro_real : ID",
"parametro_real : constante",
};

//#line 311 "gramatica.y"
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
                        System.out.println("next char: " + caracter);
                        identificador_token = AnalizadorLexico.cambiarEstado(lector, caracter);

                        // Si llego a un estado final
                        System.out.println("identificador token: " + identificador_token);
                        if (identificador_token != AccionSemantica.TOKEN_ACTIVO) {
                                yylval = new ParserVal(AnalizadorLexico.token_actual.toString());
                                AnalizadorLexico.token_actual.delete(0, AnalizadorLexico.token_actual.length());
                                System.out.println("identificador: " + identificador_token);
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
//#line 769 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 2:
//#line 25 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba nombre del programa");}
break;
case 3:
//#line 26 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaban sentencias de ejecucion");}
break;
case 4:
//#line 27 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '}' al final del programa");}
break;
case 5:
//#line 28 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '{' antes de las sentencias");}
break;
case 6:
//#line 29 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un ';' al final del programa");}
break;
case 7:
//#line 30 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un programa");}
break;
case 8:
//#line 33 "gramatica.y"
{ Parser.declarando = false;}
break;
case 9:
//#line 36 "gramatica.y"
{Parser.declarando = true;}
break;
case 13:
//#line 46 "gramatica.y"
{Parser.declarando = false;}
break;
case 15:
//#line 48 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 16:
//#line 49 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 17:
//#line 53 "gramatica.y"
{Parser.declarando = false;}
break;
case 18:
//#line 54 "gramatica.y"
{Parser.declarando = false;}
break;
case 19:
//#line 55 "gramatica.y"
{Parser.declarando = false;}
break;
case 23:
//#line 63 "gramatica.y"
{ int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
 		TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");}
break;
case 24:
//#line 66 "gramatica.y"
{ int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
        		   TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                           TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");}
break;
case 28:
//#line 76 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el cuerpo de la funcion");}
break;
case 29:
//#line 79 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
			TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaTipos.fun_TYPE);
			TablaSimbolos.agregarAtributo(ptr_id, "uso", TablaTipos.fun_TYPE);}
break;
case 30:
//#line 82 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera nombre de la funcion");}
break;
case 36:
//#line 95 "gramatica.y"
{ int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
		     int primerSeparador = Parser.ambito.toString().indexOf(NAME_MANGLING_CHAR);
                     int ultimoSeparador = Parser.ambito.toString().lastIndexOf(NAME_MANGLING_CHAR);
                     String nombre_funcion = Parser.ambito.substring(ultimoSeparador + 1) + Parser.ambito.substring(primerSeparador, ultimoSeparador);
                     int ptr_func = TablaSimbolos.obtenerSimbolo(nombre_funcion);
		     TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
		     TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");
                     TablaSimbolos.agregarAtributo(ptr_func, "tipo_parametro", tipo);	}
break;
case 37:
//#line 103 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el tipo del parametro");}
break;
case 38:
//#line 104 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
break;
case 39:
//#line 107 "gramatica.y"
{tipo = TablaTipos.f64_TYPE;}
break;
case 40:
//#line 108 "gramatica.y"
{tipo = TablaTipos.ui16_TYPE;}
break;
case 41:
//#line 111 "gramatica.y"
{}
break;
case 42:
//#line 112 "gramatica.y"
{}
break;
case 55:
//#line 137 "gramatica.y"
{}
break;
case 57:
//#line 141 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre Do y until"); }
break;
case 62:
//#line 150 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre Do y until"); }
break;
case 63:
//#line 151 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '{' "); }
break;
case 68:
//#line 160 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
break;
case 69:
//#line 161 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
break;
case 71:
//#line 163 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
break;
case 73:
//#line 165 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
break;
case 76:
//#line 168 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';'"); }
break;
case 79:
//#line 173 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera 'Then' luego de la condicion del If"); }
break;
case 80:
//#line 174 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera Else"); }
break;
case 81:
//#line 175 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
break;
case 82:
//#line 176 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del Else"); }
break;
case 83:
//#line 177 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del Then"); }
break;
case 85:
//#line 181 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del Then");}
break;
case 87:
//#line 186 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del Else ");}
break;
case 90:
//#line 191 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera Then"); }
break;
case 91:
//#line 192 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera Else"); }
break;
case 92:
//#line 193 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
break;
case 93:
//#line 194 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del Else"); }
break;
case 94:
//#line 195 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del Then"); }
break;
case 98:
//#line 201 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada Return");}
break;
case 99:
//#line 202 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 100:
//#line 203 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del Then");}
break;
case 101:
//#line 204 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';' "); }
break;
case 105:
//#line 211 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada Return");}
break;
case 106:
//#line 212 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 107:
//#line 213 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del Else ");}
break;
case 108:
//#line 214 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ';' "); }
break;
case 110:
//#line 219 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera '(' al principio de la condicion");}
break;
case 111:
//#line 220 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera una condicion");}
break;
case 115:
//#line 229 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion luego del comparador");}
break;
case 116:
//#line 230 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion antes del comparador");}
break;
case 123:
//#line 241 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
 				TablaSimbolos.agregarAtributo(ptr_id, "retorno", tipo);}
break;
case 124:
//#line 243 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se debe definir el tipo");}
break;
case 125:
//#line 244 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se debe definir el tipo");}
break;
case 126:
//#line 247 "gramatica.y"
{}
break;
case 128:
//#line 249 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un identificador en el lado izquierdo de la asignacion");}
break;
case 129:
//#line 250 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el simbolo de asignacion  entre el identificador y la expresion");}
break;
case 130:
//#line 251 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion ");}
break;
case 142:
//#line 271 "gramatica.y"
{}
break;
case 145:
//#line 276 "gramatica.y"
{}
break;
case 146:
//#line 279 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
               TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");}
break;
case 147:
//#line 281 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
		  TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
		  String lexema = negarConstante(val_peek(0).sval);}
break;
case 148:
//#line 286 "gramatica.y"
{ String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                                String valor = val_peek(1).sval;
                                String tipo = "string";
                                TablaSimbolos.agregarSimbolo(nombre);
                                int puntero = TablaSimbolos.obtenerSimbolo(nombre);
                                TablaSimbolos.agregarAtributo(puntero, "valor", valor);
                                TablaSimbolos.agregarAtributo(puntero, "tipo", tipo);}
break;
case 149:
//#line 293 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un mensaje dentro del out ");}
break;
case 150:
//#line 294 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '(' antes del inicio del comentario a imprimir");}
break;
case 151:
//#line 295 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un ')' luego del comentario a imprimir");}
break;
case 153:
//#line 301 "gramatica.y"
{}
break;
case 154:
//#line 302 "gramatica.y"
{}
break;
case 155:
//#line 303 "gramatica.y"
{}
break;
case 156:
//#line 306 "gramatica.y"
{}
break;
case 157:
//#line 307 "gramatica.y"
{}
break;
//#line 1257 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
