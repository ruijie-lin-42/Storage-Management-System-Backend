package io.github.ruijie_lin_42.storage_management_system_backend;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;

@SpringBootApplication
@MapperScan("io/github/ruijie_lin_42/storage_management_system_backend")
public class StorageManagementSystemBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageManagementSystemBackendApplication.class, args);
	}

}
