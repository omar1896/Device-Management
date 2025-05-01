package com.VOIS_Task.IotDevices;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestIotDevicesApplication {

	public static void main(String[] args) {
		SpringApplication.from(IotDevicesApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
