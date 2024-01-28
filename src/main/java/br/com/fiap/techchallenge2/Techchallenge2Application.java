package br.com.fiap.techchallenge2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Techchallenge2Application {

	public static void main(String[] args) {
		SpringApplication.run(Techchallenge2Application.class, args);
	}

}
