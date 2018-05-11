******************************************************
******************************************************
******************************************************
PROYECTO SPORTEC
CURSO:
	SOA4ID
ELABORADO:
	Wilberth Varela Guillen.
CARNE:
	2013053795
******************************************************
******************************************************
******************************************************

Indicaciones para ejecutar el programa.

1. Se necesita tener instalado Android Studio, en caso de no tenerlo adjunto el link de descarga.
Para efectos de este proyecto se trabajo con la version android-studio-ide-173.4720617-windows.exe,
la cual es la version recomendada por la pagina de Android Studio. 
	Link: https://developer.android.com/studio/?hl=es-419


2. Se necesita tener instalado Java Development Kit, en caso de no tenerlo adjunto el link para la descarga.
El utilizado para este proyecto fue jdk-8u161-windows-x64.exe.
	Link: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

3. Descargar el projecto en un zip o clonar el repositorio a su computadora.

4. Consideraciones a nivel del proyecto:
	** Este proyecto esta con la version 27 del SDK.
	** La minima version en la que puede funcionar el programa es en la 17 del SDK.
	** El proyecto se realizo en Windows 10.
 	** En la compilacion del proyecto para importar las bibliotecas utilizadas se utiza el implementation
ya que esta es la version mas nueva de Gradle: 3.0, que fue anunciado por Google.
	**En la base de datos de Firebase en algunos espacios se tuvo que crear manualmente, ya que eran espacios
necesarios para el proyecto y su resultado.

5. Para poder ligar el proyecto a las direntes secciones de Firebase, se necesitan las siguientes lineas en 
el build.gradle:
	implementation 'com.google.firebase:firebase-auth:11.4.0'
	implementation 'com.google.firebase:firebase-database:11.4.0'
	implementation 'com.google.firebase:firebase-storage:11.4.0'

Fuera de la linea de dendecias para utilizar firebase y Gmail se coloca:
	apply plugin: 'com.google.gms.google-services' 

En el caso del uso de Gmail se utiliza esta linea:
	implementation 'com.google.android.gms:play-services-auth:11.4.0'

6. Para sincronizar Gmail y poder utilizarlo en el programa siga las intrucciones de este link:
	Link: https://firebase.google.com/docs/auth/android/google-signin?hl=es-419

O tambien ingrese a este link: https://android.jlelse.eu/android-firebase-authentication-with-google-signin-3f878d9b7553

7. Para poder ligar el proyecto al firebase ingrese a este link:
	Link: https://firebase.google.com/docs/android/setup?authuser=0

En dado caso que entre a https://firebase.google.com/?hl=es-419, en este link le al crear un proyecto, 
este le va dando una guia de paso a paso que es lo que tiene que hacer para crear el proyecto.

En este link: https://www.youtube.com/watch?v=VM_6tnGf0fY, es un video el cual te puede ayudar a como configurar el proyecto,
Sin embargo no es una guia definitiva ya que puede que al configurar el proyecto de la forma del video, no tenga todas las 
funcionalidades que necesita el desarrollador en el proyecto.

8. Para poder utilizar la autentificacion de los diferente modos de Firebase ingresa al siguiente link:
	LinK: https://firebase.google.com/docs/auth/?hl=es-419

9. Para poder conectarse a la base de datos de Firebase ingrese al siguiente link: 
	Link: https://firebase.google.com/docs/database/?hl=es-419

10. Para poder utilizar el storage de Firebase ingrese el siguiente link:
	Link: https://firebase.google.com/docs/storage/?hl=es-419

Una vez hecho las configuraciones anteriores, puede ejecutar el poryecto o realizar uno nuevo de igual 
forma tomando las consideraciones que se plantean anteriormente.





