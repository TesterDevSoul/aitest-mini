package com.ceshiren.aitestmini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.ceshiren.aitestmini.dao")
public class AitestMiniApplication {

	public static void main(String[] args) {
		SpringApplication.run(AitestMiniApplication.class, args);
	}

}
