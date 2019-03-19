# Sistema-Votaciones
Sistema de votaciones implementado en Java

Servidor
Este componente corresponde al servidor al cual estarán conectados los proxys de manera inmediata. 
Tiene dos hilos, uno que se encarga de cargar las consultas desde un archivo y las carga según la etiqueta de tiempo 
correspondiente, y un segundo hilo que se encarga de recibir las solicitudes del socket continuamente. 
Cuenta con una clase Server Connection, que se encarga de procesar las solicitudes que lleguen a través del socket del proxy, 
si se trata de un cliente este tendrá las opciones de registrar su ID, ver las consultas disponibles, votar y desconectarse. 
Para las entidades, permite registrar su ID, consultar las consultas activas que tiene y desconectarse. 
También cuenta con una clase Consulta que corresponde a las consultas que se van cargando desde cierto archivo y 
son en las que puede votar el usuario.

Proxy
Este componente sirve como medio de conexión entre el servidor y el cliente, su función principal es retransmitir 
las solicitudes del usuario al servidor y hacer lo correspondiente con la respuesta desde el servidor al usuario. 
Cuenta con un cache que mediante el uso de un reloj global va almacenando temporalmente los datos del servidor y 
son con los que pueden interactuar en determinado tiempo los clientes.

Directorio
Este componente corresponde al registro de proxys con su respectiva IP y puerto, además asigna los correspondientes 
usuarios al proxy en el que se encuentran conectados. El directorio tiene utilidad en caso de que un Proxy se desconecte 
y de igual manera para manejar correspondientemente las cargas de cada uno evitando sobrecargas en solo un proxy.

Cliente
El componente cliente corresponde al usuario que puede participar en las consultas mediante un voto. Los clientes pueden 
registrar su ID, ver las consultas disponibles, votar y desconectarse, de igual manera se registran en el directorio 
con el correspondiente Proxy al que se encuentren conectados. Las solicitudes realizadas por el cliente se hacen a través 
del proxy correspondiente y las procesa el servidor.

Entidad
El componente entidad corresponde al usuario que puede consultar las consultas que ha realizado. 
Las entidades pueden registrar su ID, ver el estado en el que se encuentran las correspondientes consultas y 
desconectarse, de igual manera se registran en el directorio con el correspondiente Proxy al que se encuentren conectados. 
Las solicitudes realizadas por la entidad se hacen a través del proxy correspondiente y las procesa el servidor.

Para mas informacion, revisa la wiki.
