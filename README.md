# EA3 - Incorporando UI y BD: ConexionTester

Proyecto realizado en Java Swing sin frameworks de terceros. Incluye una interfaz de conexión JDBC, validación de URL, captura de `ClassNotFoundException` y `SQLException`, y cierre de recursos mediante `try-with-resources`.

## Compilar y abrir (Java JDK 17 o superior)

Desde el directorio raíz del proyecto:

```bash
mkdir -p build
javac -encoding UTF-8 -d build src/ConexionTester.java
java -cp build ConexionTester
```

En Windows (Símbolo del sistema):

```bat
mkdir build
javac -encoding UTF-8 -d build src\ConexionTester.java
java -cp build ConexionTester
```

## Importante: un controlador JDBC no es un framework

El JDK incluye la API JDBC, pero no incorpora el driver de SQLite. Para conectarse realmente a SQLite, instale el controlador JDBC para SQLite (por ejemplo Xerial sqlite-jdbc, compatible con su JDK), guardando su archivo `.jar` localmente en `lib/`. No se distribuye un JAR de terceros en este proyecto.

Mac/Linux:

```bash
java -cp "build:lib/*" ConexionTester
```

Windows:

```bat
java -cp "build;lib/*" ConexionTester
```

Ingrese `jdbc:sqlite:inventario.db`. Deje usuario y contraseña vacíos. Si el controlador se cargó y permite abrir la BD, la aplicación mostrará el mensaje verde. La ejecución de esta prueba exitosa debe verificarse en el equipo donde se instale el controlador; no se afirma que se realizó en este paquete.

Si utiliza MySQL/PostgreSQL, instale el controlador JDBC oficial correspondiente y complete URL, usuario y contraseña.

## Capturas reales incluidas

- `evidencias/01_validacion_url_vacia.png`: la URL vacía se rechaza (ejecución real).
- `evidencias/02_driver_sqlite_no_instalado.png`: se captura ClassNotFoundException por ausencia del driver SQLite (ejecución real).

Para obtener evidencia positiva, configure el driver, pruebe conexión y capture la pantalla en su equipo.

## Contenido

- `src/ConexionTester.java`: código fuente completo.
- `datos/codificacion_ConexionTester.csv`: transcripción CSV de cada línea del código (archivo, número de línea, contenido).
- `evidencias/`: capturas de pruebas reales.
- `EA3_Incorporando_UI_y_BD_Individual_RestrepoNicolas.pdf`: informe APA 7 con espacios por completar.
- `EA3_Incorporando_UI_y_BD_Individual_RestrepoNicolas.docx`: copia editable.

## Subir a GitHub

1. Cree un repositorio público o privado en https://github.com/new, por ejemplo `EA3-ConexionTester`.
2. Abra terminal en la carpeta del proyecto.
3. Ejecute `git init`, `git add .` y `git commit -m "Entrega EA3"`.
4. Ejecute `git branch -M main`.
5. Ejecute `git remote add origin https://github.com/USUARIO/EA3-ConexionTester.git` reemplazando USUARIO.
6. Ejecute `git push -u origin main` (autentíquese mediante método admitido por GitHub).
7. Copie el enlace real del repositorio al informe editable y vuelva a exportar el PDF y armar el ZIP.
8. Entregue el ZIP desde la plataforma de la actividad evaluativa, individualmente por Nicolás Restrepo.

**Pendiente de completar:** URL real del repositorio de Nicolás y evidencia de conexión exitosa. Trabajo individual sin número de grupo. El proyecto no se ha publicado en GitHub desde este entorno.
