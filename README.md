# Proyecto Integrador - Estructura de Datos I

**Grupo 7 - Sección 555**

Comparación experimental de tres algoritmos de búsqueda de patrones en cadenas de texto:

- **KMP** (Knuth-Morris-Pratt)
- **Boyer-Moore**
- **Rabin-Karp**

Las implementaciones provienen de la librería `algs4` (Sedgewick y Wayne, *Algorithms, 4th Edition*).

## ¿Qué hace el programa?

`Experimento.java` mide el tiempo que tarda cada algoritmo en **preparar el patrón** y en **buscarlo** dentro de un texto, bajo distintos escenarios.

**Escenarios de texto**

| Escenario | Descripción |
|-----------|-------------|
| Aleatorio | Caracteres ASCII imprimibles al azar |
| ADN | Alfabeto de 4 letras (A, C, G, T) al azar |
| Español | Fragmento de un texto real en español (`corpus.txt`) |
| Repetitivo | Una larga cadena de "a" con una sola "b" cerca del final (peor caso) |

**Parámetros**

- Tamaños de texto (n): 100 000, 1 000 000 y 10 000 000 caracteres
- Tamaños de patrón (m): 5, 10, 25, 50 y 100 caracteres
- 10 patrones distintos por cada combinación, y se reporta el promedio

En total son 4 escenarios × 3 tamaños × 5 patrones = **60 combinaciones**. En cada una, el resultado de cada algoritmo se verifica contra `String.indexOf` para comprobar que encontró la posición correcta.

## Archivos del repositorio

| Archivo | Para qué sirve |
|---------|----------------|
| `Experimento.java` | Código del experimento |
| `algs4.jar` | Librería con los algoritmos KMP, Boyer-Moore y Rabin-Karp |
| `corpus.txt` | Texto en español usado en el escenario "Español" |
| `Ejecutar.bat` | Script para ejecutar el programa en Windows |

## Cómo ejecutarlo

**Requisito:** tener Java (JDK) instalado.

**Opción 1 (Windows):** hacer doble clic en `Ejecutar.bat`.

**Opción 2 (consola):**

```bash
javac -cp algs4.jar Experimento.java
java -cp ".;algs4.jar" Experimento
```

En Linux o macOS, usar `:` en lugar de `;`:

```bash
java -cp ".:algs4.jar" Experimento
```

> El experimento maneja textos de hasta 10 millones de caracteres, por lo que puede tardar unos minutos.

## Resultados

Al terminar, el programa genera tres archivos CSV (separados por `;`) en la misma carpeta:

- `mediciones.csv`: todas las mediciones individuales (tiempo de preparación y de búsqueda, en nanosegundos)
- `busqueda.csv`: promedio del tiempo de búsqueda por combinación, en milisegundos
- `preprocesamiento.csv`: promedio del tiempo de preparación del patrón según su tamaño, en microsegundos

## Referencia

Sedgewick, R. y Wayne, K. *Algorithms, 4th Edition*. Addison-Wesley. https://algs4.cs.princeton.edu
