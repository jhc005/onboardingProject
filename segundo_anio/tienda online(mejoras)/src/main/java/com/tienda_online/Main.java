package com.tienda_online;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tienda_online.config.DotenvLoader;

@SpringBootApplication

public class Main {

	static { DotenvLoader loader = new DotenvLoader(); }
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
