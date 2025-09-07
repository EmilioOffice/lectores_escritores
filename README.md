# Lectores y Escritores
Este proyecto es un desarrollo académico para la materia de Computación Paralela en Java

Éste es otro famoso problema, el cual modela el acceso a una base de datos. Imaginemos una enorme
base de datos para un sistema de reservaciones en una línea aérea, con muchos procesos de
competencia, que intentan leer y escribir en ella. Se puede aceptar que varios procesos lean la base de
datos al mismo tiempo, pero si uno de los procesos está escribiendo (es decir, modificando) la base de
datos, ninguno de los demás procesos debería tener acceso a esta, ni si quiera los lectores.
