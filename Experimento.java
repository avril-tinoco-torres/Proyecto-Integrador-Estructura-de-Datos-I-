import edu.princeton.cs.algs4.KMP;
import edu.princeton.cs.algs4.BoyerMoore;
import edu.princeton.cs.algs4.RabinKarp;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Experimento {
    static final String[] escenarios = {"Aleatorio", "ADN", "Espanol", "Repetitivo"};
    static final String[] algoritmos = {"KMP", "Boyer-Moore", "Rabin-Karp"};
    static final int[] patrones = {5, 10, 25, 50, 100};

    static String crearTexto(int escenario, int n, String corpus, Random azar) {
        if (escenario == 2) return corpus.substring(0, n);
        if (escenario == 3) return "a".repeat(n - 100) + "b" + "a".repeat(99);
        StringBuilder texto = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            if (escenario == 1) {
                texto.append("ACGT".charAt(azar.nextInt(4)));
            } else {
                texto.append((char)(32 + azar.nextInt(95)));
            }
        }
        return texto.toString();
    }

    // Guardamos dos tiempos: preparar el patron y buscarlo.
    static long[] medir(int algoritmo, String patron, String texto, int esperado) {
        long inicio = System.nanoTime();
        long preparado;
        int indice;
        if (algoritmo == 0) {
            KMP kmp = new KMP(patron);
            preparado = System.nanoTime();
            indice = kmp.search(texto);
        } else if (algoritmo == 1) {
            BoyerMoore bm = new BoyerMoore(patron);
            preparado = System.nanoTime();
            indice = bm.search(texto);
        } else {
            RabinKarp rk = new RabinKarp(patron);
            preparado = System.nanoTime();
            indice = rk.search(texto);
        }
        long fin = System.nanoTime();
        if (indice != esperado) throw new IllegalStateException("Indice incorrecto: " + indice);
        return new long[]{preparado - inicio, fin - preparado};
    }

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);
        String corpus = Files.readString(Path.of("corpus.txt"));
        int[] tamanos = {100000, 1000000, 10000000};
        long[][] preparacion = new long[5][3];
        int filas = 0;
        for (int a = 0; a < 3; a++) medir(a, "b", "aab", 2);
        try (PrintWriter datos = new PrintWriter("mediciones.csv");
             PrintWriter resumen = new PrintWriter("busqueda.csv");
             PrintWriter tabla = new PrintWriter("preprocesamiento.csv")) {
            datos.println("escenario;n;m;prueba;algoritmo;indice;pre_ns;busqueda_ns");
            String encabezado = "escenario;n;m;KMP_ms;BoyerMoore_ms;RabinKarp_ms;indice_medio";
            resumen.println(encabezado);
            System.out.println(encabezado);
            for (int e = 0; e < escenarios.length; e++) {
                for (int n : tamanos) {
                    Random azar = new Random(20261118L + n + e);
                    String texto = crearTexto(e, n, corpus, azar);
                    // Usamos las mismas posiciones para los cinco tamanos de patron.
                    int[] posiciones = new int[10];
                    for (int r = 0; r < 10; r++) {
                        if (e == 3) {
                            posiciones[r] = n - 100;
                        } else {
                            posiciones[r] = azar.nextInt(n / 2, n - 99);
                        }
                    }
                    for (int j = 0; j < patrones.length; j++) {
                        int m = patrones[j];
                        long[] busqueda = new long[3];
                        long indices = 0;
                        for (int r = 0; r < 10; r++) {
                            String patron = texto.substring(posiciones[r], posiciones[r] + m);
                            int indice = texto.indexOf(patron);
                            indices += indice;
                            for (int a = 0; a < algoritmos.length; a++) {
                                long[] tiempo = medir(a, patron, texto, indice);
                                preparacion[j][a] += tiempo[0];
                                busqueda[a] += tiempo[1];
                                datos.printf("%s;%d;%d;%d;%s;%d;%d;%d%n", escenarios[e], n, m,
                                        r + 1, algoritmos[a], indice, tiempo[0], tiempo[1]);
                                filas++;
                            }
                        }
                        // Promedio de diez busquedas, expresado en milisegundos.
                        String fila = String.format("%s;%d;%d;%.6f;%.6f;%.6f;%.1f", escenarios[e], n, m,
                                busqueda[0] / 10.0 / 1000000, busqueda[1] / 10.0 / 1000000, busqueda[2] / 10.0 / 1000000, indices/10.0);
                        resumen.println(fila);
                        System.out.println(fila);
                    }
                }
            }
            tabla.println("m;KMP_us;BoyerMoore_us;RabinKarp_us");
            for (int j = 0; j < patrones.length; j++) {
                tabla.printf("%d;%.3f;%.3f;%.3f%n", patrones[j], preparacion[j][0] / 120.0 / 1000,
                        preparacion[j][1] / 120.0 / 1000, preparacion[j][2] / 120.0 / 1000);
            }
            if (tabla.checkError() || resumen.checkError() || datos.checkError())
                throw new IOException("Error al guardar los CSV.");
        }
        System.out.println("60 combinaciones | 600 entradas verificadas | " + filas + " mediciones");
    }
}
