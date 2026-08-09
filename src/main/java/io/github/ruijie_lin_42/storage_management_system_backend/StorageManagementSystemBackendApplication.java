package io.github.ruijie_lin_42.storage_management_system_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(
        basePackages = "io/github/ruijie_lin_42/storage_management_system_backend",
        annotationClass = org.apache.ibatis.annotations.Mapper.class
)
public class StorageManagementSystemBackendApplication {

	static void main(String[] args) {
		SpringApplication.run(StorageManagementSystemBackendApplication.class, args);
	}

}
