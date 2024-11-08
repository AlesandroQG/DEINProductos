# Examen Evaluación 1 - Productos
## DM2 DEIN 2024-2025
### Alesandro Quirós Gobbato

Esta es una aplicación hecha con JavaFX para la gestión de productos.

#### Estructura

La estructura del proyecto es la siguiente:
- `src > main`:
    - `java > com.alesandro.productos`:
        - `OlimpiadasApplicacion.java`: Clase que lanza la aplicación
        - `controller`:
            - `ProductosController.java`: Clase que controla los eventos de la ventana principal de la aplicación
        - `dao`:
            - `DaoProducto.java`: Clase que hace las operaciones con la base de datos del modelo Producto
        - `db`:
            - `DBConnect.java`: Clase que se conecta a la base de datos
        - `model`:
            - `Producto.java`: Clase que define el objeto Producto
    - `resources > com.alesandro.productos`:
        - `fxml`:
            - `Productos.fxml`: Ventana principal de la aplicación
        - `images`: Carpeta que contiene las imágenes de la aplicación
        - `sql`:
            - `productos.sql`: Fichero para la creación de la base de datos
        - `style`: Carpeta que contiene los estilos de la aplicación
