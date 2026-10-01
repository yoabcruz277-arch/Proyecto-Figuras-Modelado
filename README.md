# Proyecto 1: Reconocimiento de Figuras Geométricas 

Este proyecto analiza imágenes en formato `.bmp` para detectar, contar y clasificar figuras geométricas (Triángulos, Cuadriláteros, Círculos u Otros).

---

## Requisitos

*  Se requiere **Java 21** o superior para garantizar la correcta compilación y compatibilidad.
* **Sistema Operativo:** Compatible con macOS, Linux y Windows.

---

## 📂 Estructura del Repositorio

```text
Proyecto-Figuras-Modelado/
├── src/                # Código fuente y recursos
│   ├── Main.java
│   ├── Mod1/           # Módulo de procesamiento de imagen
│   ├── Mod2/           # Módulo de análisis matemático y clasificación
│   └── imagenes/       # Imágenes .bmp para las pruebas del proyecto
├── Reportes/           # Reportes del proyecto en PDF y casos de prueba
└── README.md           # Guía de usuario
```

---

##  Guía de Descarga, Compilación y Ejecución del Programa

Sigue estos pasos en tu terminal para descargar, compilar y ejecutar la aplicación:

### 1. Clonar o Descargar el Repositorio
Abre tu terminal y clona el proyecto con Git:
```bash
git clone [https://github.com/yoabcruz277-arch/Proyecto-Figuras-Modelado.git](https://github.com/yoabcruz277-arch/Proyecto-Figuras-Modelado.git)
cd Proyecto-Figuras-Modelado
```
*(Si descargaste el proyecto en ZIP, descomprímelo y entra a la carpeta desde la terminal usando `cd Proyecto-Figuras-Modelado-main`)*.

---

### 2. Verificar la versión de Java
Asegúrate de contar con **Java 21** o superior:
```bash
java -version
```

> [!Atención]
> Si la versión mostrada es menor a la 21, instala JDK 21 antes de compilar para evitar errores de compatibilidad.

---

### 3. Compilar el proyecto
Crea la carpeta de ejecutables `bin/` y compila todos los módulos del código desde la raíz del proyecto:

* **En macOS / Linux:**
  ```bash
  mkdir -p bin
  javac -d bin -sourcepath src src/Main.java src/Mod1/*.java src/Mod2/*.java
  ```

* **En Windows (CMD / PowerShell):**
  ```cmd
  mkdir bin
  javac -d bin -sourcepath src src/Main.java src/Mod1/*.java src/Mod2/*.java
  ```

---

### 4. Ejecutar el programa
Inicia la aplicación ejecutando la clase principal `Main`:

```bash
java -cp bin Main
```

---

##  Uso del Programa

1. Selecciona la opción **`1`** en el menú para analizar una imagen.
2. Ingresa la ruta del archivo `.bmp`. Puedes probar con las imágenes incluidas en el proyecto o cualquier ruta de tu sistema:
   * **Ruta relativa dentro del proyecto:** `src/imagenes/nombre_imagen.bmp`
   * **Ruta absoluta en tu sistema:** *(En macOS puedes arrastrar cualquier archivo `.bmp` directamente a la terminal)*.
3. El programa desplegará el análisis con los siguientes datos:
   * **Cantidad** de figuras encontradas.
   * **Color** de la figura.
   * **Vértices** calculados por RDP.
   * **Área** ($px^2$) y **Perímetro** ($px$).
   * **Factor de circularidad**.
   * **Categoría** clasificada (Triángulo, Cuadrilátero, Círculo u Otros).