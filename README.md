# personapp-hexa-spring-boot
Plantilla Laboratorio Arquitectura Limpia



💻 Explicación de las Operaciones CRUD para Study
El study-controller-v-1 implementa las siguientes operaciones utilizando verbos HTTP, con la base /api/v1/study/. En tu arquitectura, estas peticiones son recibidas por el Adaptador de Entrada (REST API), que luego llama al Caso de Uso (StudyUseCase) en la Capa de Aplicación para ejecutar la lógica de negocio, y el Caso de Uso a su vez utiliza el Puerto de Salida (PersonOutputPort) implementado por los Adaptadores de Salida (MariaDB o MongoDB).

1. Obtener Todos los Estudios (Read - All)
Verbo HTTP: GET

Path: /api/v1/study/{database}

Propósito: Obtener una lista de todos los estudios académicos.

Parámetros:

database (path, required): Especifica la base de datos a consultar. El ejemplo utiliza MONGO, lo que invoca al Adaptador de Salida PersonOutputAdapterMongo.

Respuesta (Código 200 OK): Devuelve un array ([] en el ejemplo) que contendrá objetos con el esquema de un estudio, incluyendo: idProf, ccPer, graduationDate, universityName, database, y status.

Contexto Hexagonal: Este GET en el Adaptador de Entrada llama al Caso de Uso StudyUseCase para la operación de lectura masiva. El Caso de Uso usa la Inversión de Dependencias para seleccionar el StudyOutputPort (MariaDB o MongoDB) basándose en el parámetro database proporcionado en la URL.
<img width="900" height="853" alt="image" src="https://github.com/user-attachments/assets/ec28e42d-8217-458b-8c98-162307adec66" />

2. Crear un Nuevo Estudio (Create)
Verbo HTTP: POST

Path: /api/v1/study (Sin parámetros en la URL)

Propósito: Crear un nuevo registro de estudio académico.

Request Body (Cuerpo de la Petición): Contiene el objeto JSON con la información del estudio a crear, incluyendo los IDs de la clave primaria compuesta (idProf, ccPer), la fecha de graduación, universidad y el nombre de la base de datos (database) a usar.

Respuesta (Código 200 OK): Devuelve el objeto del estudio creado, incluyendo el campo status que indica el éxito de la operación.

Contexto Hexagonal: El Adaptador REST recibe la petición y llama al StudyUseCase. Este caso de uso aplica la Lógica de Negocio (p. ej., validar que la persona y la profesión existan, como se menciona en tus reglas de negocio) antes de llamar al método save() del Puerto de Salida seleccionado.
<img width="902" height="552" alt="image" src="https://github.com/user-attachments/assets/3d97cf32-484a-4ba1-bcb7-6360ddcb66a8" />

3. Actualizar un Estudio Existente (Update)
Verbo HTTP: PUT

Path: /api/v1/study/{database} (El parámetro database aquí parece redundante o incorrecto en el diseño mostrado, ya que la base de datos ya está en el Request Body en el ejemplo de POST/GET)

Propósito: Actualizar un estudio existente (identificado por la clave compuesta dentro del cuerpo de la petición).

Parámetros (Path):

database (path, required): El nombre de la base de datos a usar.

Request Body: JSON con los datos actualizados del estudio. Los campos de la Clave Primaria Compuesta (idProf, ccPer) deben estar presentes para identificar el registro a modificar.

Respuesta (Código 200 OK): Devuelve el objeto del estudio actualizado.

Contexto Hexagonal: Similar al POST, el Caso de Uso orquesta la actualización y usa el puerto apropiado. La Intercambiabilidad de Tecnología es clave aquí: la misma operación StudyUseCase.update(...) funciona para MariaDB y MongoDB, gracias a que el Adaptador correcto implementa el Puerto de Salida.
<img width="905" height="621" alt="image" src="https://github.com/user-attachments/assets/2ddf1094-ccea-4fc9-8921-0b8d5c535640" />

4. Obtener un Estudio Específico (Read - Single)
Verbo HTTP: GET

Path: /api/v1/study/{database}/{idProf}/{ccPer}

Propósito: Obtener los detalles de un estudio específico.

Parámetros (Path, required):

database: La base de datos a consultar.

idProf: Parte de la PK compuesta de la Profession.

ccPer: El identification de la Person (parte de la PK compuesta del Study).

Respuesta (Código 200 OK): Devuelve el objeto JSON del estudio solicitado.

Contexto Hexagonal: Muestra la necesidad de pasar las tres partes de la PK compuesta (person_id, prof_id, prof_name en tu modelo conceptual) en la URL para identificar un Study de forma única (aunque en la imagen solo se ven dos parámetros (idProf y ccPer), asumiendo que el tercer parámetro de la PK es implícito o no requerido en la implementación REST).
<img width="898" height="563" alt="image" src="https://github.com/user-attachments/assets/fd4efe51-75b3-4724-9fde-3e9a0a0f112b" />

5. Eliminar un Estudio (Delete)
Verbo HTTP: DELETE

Path: /api/v1/study/{database}/{idProf}/{ccPer}

Propósito: Eliminar un estudio académico.

Parámetros (Path, required):

database: La base de datos donde se ejecutará la eliminación.

idProf: El ID de la profesión.

ccPer: El DNI/Cédula de la persona.

Respuesta (Código 200 OK): Devuelve un objeto de confirmación, incluyendo un status, una description (probablemente el resultado de la operación) y un date de la ejecución.

Contexto Hexagonal: Esta es la operación de borrado. El StudyUseCase llamará al método delete() del puerto de salida elegido. La arquitectura garantiza que la lógica de negocio no sepa si está borrando un documento de MongoDB o una fila de MariaDB, demostrando la Independencia de Framework y la Intercambiabilidad de Tecnología.







El controlador utiliza la ruta base /api/v1/profession/ para exponer los servicios. El parámetro {database} en el path confirma el soporte Multi-Database de la arquitectura, permitiendo al ProfessionUseCase elegir el Adaptador de Salida adecuado (MariaDB o MongoDB) en tiempo de ejecución.

<img width="906" height="544" alt="image" src="https://github.com/user-attachments/assets/0c10e339-9409-4c9b-85bb-a33e4963f4a5" />


1. Obtener Todas las Profesiones (Read - All)
Verbo HTTP: GET

Path: /api/v1/profession/{database}

Propósito: Recuperar una lista de todas las profesiones registradas.

Parámetros:

database (path, required): Define si la consulta se dirige a la implementación de MariaDB o MongoDB.

Respuesta (Código 200 OK): Devuelve un array ([]) que contiene objetos Profession con su identification, name, description, database y status.

Contexto Hexagonal: El Adaptador REST recibe la solicitud y la pasa al Caso de Uso (ProfessionUseCase), que a su vez llama a la abstracción del Puerto de Salida (ProfessionOutputPort) implementada por el adaptador de persistencia seleccionado.
<img width="902" height="498" alt="image" src="https://github.com/user-attachments/assets/7b850d76-58fa-404c-af26-7e4bea7ad2ea" />

2. Obtener una Profesión Específica (Read - Single)
Verbo HTTP: GET

Path: /api/v1/profession/{database}/{identification}/{name}

Propósito: Consultar los detalles de una única profesión utilizando su clave primaria compuesta.

Parámetros (Path, required):

database: Base de datos de consulta.

identification: La parte numérica del identificador de la profesión.

name: El nombre de la profesión (la segunda parte de la PK).

Respuesta (Código 200 OK): Devuelve el objeto Profession si es encontrado.

Contexto Hexagonal: Este endpoint demuestra cómo la estructura de la URL se adapta a la identidad del dominio (Profession), en este caso, una clave compuesta, independientemente de cómo se almacene físicamente en los adaptadores de salida (JPA en MariaDB o documentos en MongoDB).
<img width="893" height="565" alt="image" src="https://github.com/user-attachments/assets/fac963d6-64a3-44b9-90ee-1736fa55c739" />

3. Crear una Nueva Profesión (Create)
Verbo HTTP: POST

Path: /api/v1/profession

Propósito: Registrar una nueva profesión en el sistema.

Request Body: Objeto JSON que debe contener los campos requeridos, incluyendo identification, name y el campo de control database.

Respuesta (Código 200 OK): Devuelve la profesión recién creada con el campo status indicando el éxito.

Contexto Hexagonal: El POST es un punto de entrada que dispara la Lógica de Negocio Pura en el ProfessionUseCase. Esta lógica (p. ej., validaciones de formato) se ejecuta antes de persistir los datos a través del Puerto de Salida.
<img width="907" height="530" alt="image" src="https://github.com/user-attachments/assets/56ddc890-e7ff-48a2-ac26-3b456c676dad" />

4. Actualizar una Profesión Existente (Update)
Verbo HTTP: PUT

Path: /api/v1/profession/{database}

Propósito: Modificar los datos de una profesión existente (identificada por su clave compuesta en el cuerpo de la petición).

Parámetros (Path):

database (path, required): Base de datos donde se aplicará la actualización.

Request Body: JSON con los datos actualizados. Es crucial que el identification y name estén presentes para localizar el registro.

Respuesta (Código 200 OK): Devuelve el objeto Profession con los datos actualizados.

Contexto Hexagonal: La acción de actualización es gestionada por el Caso de Uso, garantizando que el proceso de mapeo y persistencia a cualquiera de las bases de datos (MariaDB o MongoDB) se realice de manera transparente, cumpliendo con la Independencia de Framework.
<img width="897" height="594" alt="image" src="https://github.com/user-attachments/assets/8e68a0a7-c793-4682-8634-e877ee32ba82" />

5. Eliminar una Profesión (Delete)
Verbo HTTP: DELETE

Path: /api/v1/profession/{database}/{identification}/{name}

Propósito: Eliminar una profesión del sistema.

Parámetros (Path, required): Se utilizan los componentes de la clave primaria compuesta (identification y name) para identificar con precisión el registro a borrar.

Respuesta (Código 200 OK): Devuelve un objeto de confirmación con status, description y date.

Contexto Hexagonal: Esta operación finaliza el ciclo CRUD. Si la arquitectura está bien implementada, el ProfessionUseCase llama al delete() del Puerto de Salida, sin tener que preocuparse por las diferencias en las sentencias de borrado entre SQL (MariaDB) y NoSQL (MongoDB). Esto simplifica el Testing, ya que el caso de uso puede probarse con un mock del Puerto de Salida.
<img width="894" height="539" alt="image" src="https://github.com/user-attachments/assets/07f2e26c-5b86-4b55-ac4b-4cee81d44697" />




📱 Explicación de las Operaciones CRUD para Phone
El controlador expone sus servicios en la ruta base /api/v1/phone/. Al igual que en los controladores anteriores, el parámetro {database} en el path es clave para habilitar la Intercambiabilidad de Tecnología entre MariaDB y MongoDB en la Capa de Adaptadores de Salida.

1. Obtener Todos los Teléfonos (Read - All)
Verbo HTTP: GET

Path: /api/v1/phone/{database}

Propósito: Recuperar la lista completa de todos los números de teléfono registrados.

Parámetros:

database (path, required): Especifica el Adaptador de Salida de persistencia a utilizar (MariaDB o MongoDB).

Respuesta (Código 200 OK): Devuelve un array de objetos Phone, cada uno conteniendo el number, company, el ID del dueño (ownerId), database y status.

Contexto Hexagonal: El Adaptador REST invoca el PhoneUseCase (Caso de Uso), el cual usa el Puerto de Salida (PhoneOutputPort) para obtener los datos. El caso de uso no se preocupa por la sintaxis SQL o NoSQL; solo se enfoca en la orquestación.
<img width="903" height="510" alt="image" src="https://github.com/user-attachments/assets/370f6333-a90b-48bb-aa6b-a0b33e5dc520" />

2. Obtener un Teléfono Específico (Read - Single)
Verbo HTTP: GET

Path: /api/v1/phone/{database}/{number}

Propósito: Consultar los detalles de un teléfono específico.

Parámetros (Path, required):

database: Base de datos de consulta.

number: El número de teléfono, que es la clave primaria de la entidad.

Respuesta (Código 200 OK): Devuelve el objeto Phone solicitado.

Contexto Hexagonal: Este endpoint utiliza el identificador natural del dominio (number) para realizar la búsqueda, que será traducida por el Adaptador de Salida seleccionado a la consulta específica de la base de datos subyacente.
<img width="902" height="596" alt="image" src="https://github.com/user-attachments/assets/d796661a-a651-4912-8918-fa4f7f3e3e9c" />

3. Crear un Nuevo Teléfono (Create)
Verbo HTTP: POST

Path: /api/v1/phone

Propósito: Registrar un nuevo número de teléfono.

Request Body: JSON con los detalles del teléfono a crear. Regla de Negocio clave: según tu modelo, un teléfono DEBE tener un dueño (ownerId), lo que requiere una validación en el PhoneUseCase antes de la persistencia.

Respuesta (Código 200 OK): Devuelve el objeto creado.

Contexto Hexagonal: El POST es la puerta de entrada a la Lógica de Negocio. El PhoneUseCase debe verificar que el ownerId proporcionado sea válido (una persona existente), y luego llama al Puerto de Salida para guardarlo, garantizando la Integridad Referencial lógica.
<img width="894" height="533" alt="image" src="https://github.com/user-attachments/assets/b7a574ff-3088-4da9-8895-0ce5cf1dbf75" />

4. Actualizar un Teléfono Existente (Update)
Verbo HTTP: PUT

Path: /api/v1/phone/{database}

Propósito: Modificar los detalles de un número de teléfono (p. ej., cambiar la compañía o reasignar el dueño).

Parámetros (Path):

database (path, required): Base de datos para la operación.

Request Body: JSON con los datos actualizados. El number en el cuerpo identifica el registro a actualizar.

Respuesta (Código 200 OK): Devuelve el objeto Phone actualizado.

Contexto Hexagonal: Muestra la capacidad del sistema para manejar actualizaciones CRUD utilizando la misma interfaz REST, con el beneficio de la Independencia de Framework, ya que el código del PhoneUseCase permanece inalterado sin importar si la actualización ocurre en un EntityManager de JPA (MariaDB) o en una MongoRepository (MongoDB).
<img width="903" height="511" alt="image" src="https://github.com/user-attachments/assets/a7084bd1-5548-4e39-86b6-ccf668490dd2" />


5. Eliminar un Teléfono (Delete)
Verbo HTTP: DELETE

Path: /api/v1/phone/{database}/{number}

Propósito: Eliminar un registro de teléfono.

Parámetros (Path, required):

database: La base de datos de destino.

number: La clave primaria utilizada para identificar el teléfono a eliminar.

Respuesta (Código 200 OK): Devuelve una confirmación de éxito.

Contexto Hexagonal: El PhoneUseCase ejecuta la eliminación. Es importante notar la Regla de Negocio de la entidad Phone: "Si se elimina una persona, se eliminan sus teléfonos (CASCADE)". Aunque esta operaci borra un solo teléfono, en el caso de la eliminación de la persona (Person), el PersonUseCase orquestaría el borrado en cascada de sus teléfonos, lo que es un ejemplo más avanzado de la Lógica de Negocio en la Capa de Aplicación.

<img width="902" height="494" alt="image" src="https://github.com/user-attachments/assets/af280e58-493b-49e4-b943-4f97096f3515" />



🧑 Explicación de las Operaciones CRUD para Person
El controlador utiliza la ruta base /api/v1/persona/. El parámetro {database} sigue siendo el mecanismo clave para invocar el Principio de Intercambiabilidad de Tecnología de la Arquitectura Hexagonal, permitiendo que el PersonUseCase utilice el Adaptador de Salida de MariaDB o MongoDB.

1. Obtener Todas las Personas (Read - All)
Verbo HTTP: GET

Path: /api/v1/persona/{database}

Propósito: Recuperar la lista completa de personas.

Parámetros:

database (path, required): Determina si se consulta la implementación de MONGO (ejemplo en la imagen) o MARIA.

Respuesta (Código 200 OK): Devuelve un array de objetos Person, mostrando los atributos clave como dni, firstName, lastName, sex, age, database y status.

Contexto Hexagonal: Esta operación ilustra el concepto de Multi-Database en acción. La misma solicitud GET puede ser satisfecha por dos adaptadores de persistencia completamente diferentes (SQL vs. NoSQL), gracias a que ambos implementan el mismo Puerto de Salida (PersonOutputPort).
<img width="876" height="864" alt="image" src="https://github.com/user-attachments/assets/410577ed-66b9-4c75-a914-a482e1bcc36d" />


2. Obtener una Persona Específica (Read - Single)
Verbo HTTP: GET

Path: /api/v1/persona/{database}/{dni}

Propósito: Consultar los detalles de una persona específica.

Parámetros (Path, required):

database: Base de datos de consulta (MARIA en el ejemplo).

dni: El número de identificación de la persona (la clave primaria).

Respuesta (Código 200 OK): Devuelve el objeto Person encontrado (ejemplo: dni: 1001, firstName: "Fulano", lastName: "Villalobos").

Contexto Hexagonal: El Adaptador de Entrada recoge el DNI, y el PersonUseCase ejecuta la lógica para obtener la entidad del Dominio. La respuesta muestra que incluso los datos de la base de datos MariaDB (SQL) se han mapeado a un formato JSON estándar que sigue la estructura de la entidad de Dominio.
<img width="911" height="633" alt="image" src="https://github.com/user-attachments/assets/81a2a575-ff5f-4e46-9dfc-f58e174e618d" />



3. Crear una Nueva Persona (Create)

Verbo HTTP: POST

Path: /api/v1/persona

Propósito: Registrar una nueva persona en el sistema.

Request Body: JSON con los datos de la persona a crear. Es importante notar que el sex es un enum en tu modelo, lo que implica que el PersonUseCase debe validar este valor (una de las Reglas de Negocio).

Respuesta (Código 200 OK): Devuelve el objeto Person creado.

Contexto Hexagonal: Este POST desencadena el flujo: Adaptador REST $\rightarrow$ Capa de Aplicación (PersonUseCase) $\rightarrow$ Puerto de Salida. El caso de uso es responsable de aplicar las reglas, como la validación de la edad (age) si está presente (isValidAge()).
<img width="904" height="566" alt="image" src="https://github.com/user-attachments/assets/1de2ccfc-9ba5-4d24-82ff-aea761f30358" />



4. Actualizar una Persona Existente (Update)
Verbo HTTP: PUT

Path: /api/v1/persona/{database}

Propósito: Modificar los datos de una persona existente.

Parámetros (Path):

database (path, required): Base de datos para la actualización.

Request Body: JSON que contiene el dni (para identificar) y los campos a modificar.

Respuesta (Código 200 OK): Devuelve el objeto Person actualizado.

Contexto Hexagonal: La actualización a través del PUT reafirma la separación de responsabilidades. El Dominio se actualiza y el Mapeo Flexible se encarga de traducir el objeto actualizado de vuelta al formato requerido por el Adaptador de Persistencia (fila SQL o documento NoSQL).
<img width="907" height="879" alt="image" src="https://github.com/user-attachments/assets/37a2aad9-e0e1-4013-9c81-38b415ee1444" />



5. Eliminar una Persona (Delete)
Verbo HTTP: DELETE

Path: /api/v1/persona/{database}/{dni}

Propósito: Eliminar el registro de una persona.

Parámetros (Path, required):

database: La base de datos de destino (MARIA en el ejemplo).

dni: La clave primaria de la persona a eliminar.

Respuesta (Código 200 OK): Devuelve una respuesta booleana simple (true en la imagen), indicando el éxito de la operación.

Contexto Hexagonal (Borrado en Cascada): Este DELETE es crítico debido a las Reglas de Negocio de las entidades relacionadas: si se elimina una persona, sus teléfonos y sus registros de estudio también deben ser eliminados (CASCADE). El PersonUseCase es el encargado de coordinar este borrado complejo, asegurando la integridad de los datos en TODOS los Adaptadores de Salida necesarios, ya sea MariaDB o MongoDB.
<img width="913" height="822" alt="image" src="https://github.com/user-attachments/assets/eb6d4536-c6e7-416b-b123-9bf97312fe33" />


# Configuración

Instalar MariaDB en puerto 3307
Instalar MongoDB en puerto 27017

Ejecutar los scripts en las dbs

el adaptador rest corre en el puerto 3000
el swagger en http://localhost:3000/swagger-ui.html

Son dos adaptadores de entrada, 2 SpringApplication diferentes

Deben configurar el lombok en sus IDEs

Pueden hacer Fork a este repo, no editar este repositorio
