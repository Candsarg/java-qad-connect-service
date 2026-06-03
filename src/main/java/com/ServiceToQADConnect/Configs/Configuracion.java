package com.ServiceToQADConnect.Configs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
 

public class Configuracion {
	

	
	/*
	 * constructor de la clase, se le pasa un String con el 
	 * lugar donde se se creará el archivo properties
	 */
	static public void setConfiguracion(String path) {
		File archivo = new File(path);
		File folder = new File("log");
		folder.mkdir();
		if (!archivo.exists()) {
			crear_archivo(archivo);
		}
	}
	
	
	/*
	 * metodo para crear el archivo.properties con la configuración predeterminada
	 */
	static public void crear_archivo(File archivo){

	    try {            
	        try (OutputStream outputStream = new FileOutputStream(archivo)) {
            
	        	/*
	        	 * Se agregan las propiedades nesesarias a nuestro objeto Properties
	        	 */
	        	Properties prop = new Properties();
	            prop.setProperty("Appserver.url", "AppServerDC://10.4.61.6:5162/as-qad-int-001");
	            prop.setProperty("Appserver.modo", "1");
				prop.setProperty("Appserver.maxthreads", "5");
	            prop.setProperty("Appserver.name", "as-qad-int-001");
	           //prop.setProperty("Appserver.conecctor", "xxsyncget");
	           // prop.setProperty("logging.path", "/var/log/");
	            prop.setProperty("server.port","43051");
	            prop.setProperty("certificado","/usr/dlc/certs/psccerts.jar");
	            prop.setProperty("server.error.include-message", "always");
	            /* 
	             * define la cantidad de hilos en el tomcat al mismo timpo si se ejecutan mas
	             * los deja a la espera de que se ejecuten los anteriores
	             */
	            //prop.setProperty("server.tomcat.max-threads", "4");
	            
	            // guardamos nuestase propiedades en el archivo .properties
	            prop.store(outputStream, "Config");
	        }
 
	    } catch (FileNotFoundException ex) {
	        System.out.println("Archivo no encontrado!");
	        ex.printStackTrace();
	    } catch (IOException ex) {
	        System.out.println("Error de entrada salida!");
	        ex.printStackTrace();
	    }        
	     
	}
	

}
