package com.ServiceToQADConnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.ServiceToQADConnect.Configs.Configuracion;

@SpringBootApplication
@PropertySource("file:service.properties")
public class ServiceToQADConnectApplication {

	public static void main(String[] args) {		
		Configuracion.setConfiguracion("service.properties");
		SpringApplication.run(ServiceToQADConnectApplication.class, args);
	}

}
