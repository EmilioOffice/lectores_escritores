# 📚✍️ El Problema de los Lectores y Escritores en Java

Este proyecto es una simulación gráfica e interactiva del clásico problema de concurrencia **"El Problema de los Lectores y Escritores"**. Fue desarrollado como una práctica académica para la materia de Computación Paralela, utilizando hilos (`Threads`) en Java y una interfaz gráfica construida con Swing.

---

## 📜 Descripción del Problema

Este problema modela el acceso concurrente a un recurso compartido, como una base de datos. Múltiples procesos (hilos) compiten por acceder a este recurso, y se dividen en dos tipos:

*   **Lectores:** Procesos que solo necesitan leer datos del recurso.
*   **Escritores:** Procesos que necesitan modificar (escribir) datos en el recurso.

Las reglas de acceso son estrictas para garantizar la integridad de los datos:
1.  Varios **lectores** pueden acceder al recurso simultáneamente.
2.  Si un **escritor** está accediendo al recurso, ningún otro proceso (ni lector ni escritor) puede entrar. Solo puede haber un escritor a la vez.

### El Desafío: La Inanición (Starvation)

El principal desafío en este problema no es el interbloqueo, sino la **inanición**. Si hay un flujo constante de lectores, un escritor podría tener que esperar indefinidamente para obtener acceso. Del mismo modo, si se da prioridad a los escritores, los lectores podrían quedar bloqueados por mucho tiempo. La solución debe ser "justa" para evitar que un tipo de proceso monopolice el recurso.

---

## ✨ Características de la Simulación

Este programa ofrece un entorno interactivo para visualizar y comprender las complejidades de los bloqueos compartidos y exclusivos:

*   **Simulación Gráfica**: Interfaz clara y en tiempo real construida con Java Swing que muestra el estado de todos los componentes.
*   **Monitoreo de Estados**:
    *   **Procesos**: Visualiza el estado de cada Lector y Escritor (`INACTIVO`, `ESPERANDO`, `ACCEDIENDO`).
    *   **Recurso Compartido**: Monitorea el estado de la "Base de Datos" (`INACTIVA`, `LECTURA`, `ESCRITURA`), mostrando qué procesos están accediendo en cada momento.
*   **Gestión de Acceso Justo**: La solución utiliza un `ReentrantReadWriteLock` en modo "justo" para evitar la inanición, asegurando que los hilos que más tiempo llevan esperando tengan prioridad.
*   **Controles Dinámicos**: Permite **"Añadir Lector"** o **"Añadir Escritor"** a la simulación "al vuelo", observando cómo el sistema se adapta a la nueva carga de trabajo.
*   **Gestión Segura de Hilos**: El programa gestiona el ciclo de vida de los hilos de forma segura y eficiente.

---

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje**: Java
*   **Librerías Gráficas**: `Java AWT` y `Java Swing` para la interfaz de usuario.
*   **Concurrencia**: `java.lang.Thread` y, de forma crucial, `java.util.concurrent.locks.ReentrantReadWriteLock` para gestionar los bloqueos de lectura compartida y escritura exclusiva.

---

## 🚀 Instrucciones de Ejecución

Para compilar y ejecutar este proyecto, necesitas tener instalado el **JDK (Java Development Kit)** en tu sistema.

### Pasos

1.  **Clonar o Descargar el Repositorio**:
    Asegúrate de tener los 4 archivos `.java` en la misma carpeta:
    *   `Main.java`
    *   `PanelSimulacion.java`
    *   `Proceso.java`
    *   `BaseDeDatos.java`

2.  **Abrir una Terminal**:
    Navega a través de la línea de comandos hasta el directorio donde guardaste los archivos.

3.  **Compilar el Código**:
    Ejecuta el siguiente comando para compilar todos los archivos Java.
    ```bash
    javac *.java
    ```

4.  **Ejecutar el Programa**:
    Una vez compilado, ejecuta la clase principal para iniciar la simulación.
    ```bash
    java Main
    ```

¡Listo! La ventana de la simulación aparecerá y podrás interactuar con ella.

---

## 👨‍💻 Autor y Propietario

Este proyecto y su código fuente son propiedad de:

*   **Juan Emilio Marquez Gudiño**
*   **Correo**: <emiliomrqz@gmail.com>

---

*Este proyecto fue creado con fines educativos.*