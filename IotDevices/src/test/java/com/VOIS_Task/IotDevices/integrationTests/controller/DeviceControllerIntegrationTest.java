package com.VOIS_Task.IotDevices.integrationTests.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.VOIS_Task.IotDevices.dtos.DeviceRequestDTO;
import com.VOIS_Task.IotDevices.entities.Device;
import com.VOIS_Task.IotDevices.enumerators.DeviceStatus;
import com.VOIS_Task.IotDevices.repository.DeviceRepository;
import com.VOIS_Task.IotDevices.services.DeviceConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class DeviceControllerIntegrationTest {
    @Mock
    private DeviceRepository mockdeviceRepository;

    @InjectMocks
    private DeviceConfigurationService deviceConfigurationService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceRepository deviceRepository;

    private Device readyTestDevice;
    private Device activatedTestDevice;
    private Device unAvalabileTestDevice;

    @BeforeEach
    void setUp() {
        deviceRepository.deleteAll(); // clean database before each test

        readyTestDevice = new Device();
        readyTestDevice.setPincode("1234567");
        readyTestDevice.setStatus(DeviceStatus.READY);
        readyTestDevice.setAvailability(true);
        readyTestDevice.setTemperature(-1);
        readyTestDevice = deviceRepository.save(readyTestDevice);

        activatedTestDevice = new Device();
        activatedTestDevice.setPincode("1111111");
        activatedTestDevice.setStatus(DeviceStatus.ACTIVE);
        activatedTestDevice.setAvailability(true);
        activatedTestDevice.setTemperature(1);
        activatedTestDevice = deviceRepository.save(activatedTestDevice);

        unAvalabileTestDevice = new Device();
        unAvalabileTestDevice.setPincode("1111112");
        unAvalabileTestDevice.setStatus(DeviceStatus.READY);
        unAvalabileTestDevice.setAvailability(false);
        unAvalabileTestDevice.setTemperature(-1);
        unAvalabileTestDevice = deviceRepository.save(unAvalabileTestDevice);

    }

    @Test
    void configureDevice_ShouldActivateDevice() throws Exception {
        mockMvc.perform(put("/api/devices/" + readyTestDevice.getId() + "/configure")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Device configured successfully"))
                .andExpect(jsonPath("$.data.status").value(DeviceStatus.ACTIVE.getCode()))
                .andExpect(jsonPath("$.data.temperature").isNumber());
    }


    @Test
    void configureDevice_ShouldReturnError_WhenDeviceDoesNotExist() throws Exception {
        long nonExistentDeviceId = 999L;

        when(mockdeviceRepository.findById(nonExistentDeviceId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/devices/" + nonExistentDeviceId + "/configure")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Assuming you're using BadRequest for this error
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("DEVICE_DOES_NOT_EXIST"))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").value(org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void configureDevice_ShouldReturnError_WhenDeviceAlreadyActivated() throws Exception {

        mockMvc.perform(put("/api/devices/" + activatedTestDevice.getId() + "/configure")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Assuming you're using BadRequest for this error
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Device Already Activated"))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").value(org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void configureDevice_ShouldReturnError_WhenDeviceUnAvailable() throws Exception {

        mockMvc.perform(put("/api/devices/" + unAvalabileTestDevice.getId() + "/configure")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Assuming you're using BadRequest for this error
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Device Is Not Available"))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").value(org.hamcrest.Matchers.notNullValue()));
    }


    @Test
    void createDevice_ShouldCreateDevice() throws Exception {
        DeviceRequestDTO newDevice = new DeviceRequestDTO();
        newDevice.setPincode("7777777");
        newDevice.setStatus(DeviceStatus.READY.getCode());
        newDevice.setAvailability(true);
        newDevice.setTemperature(-1);

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newDevice)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Device created successfully"))
                .andExpect(jsonPath("$.data.status").value(newDevice.getStatus()))
                .andExpect(jsonPath("$.data.availability").value(newDevice.getAvailability()))
                .andExpect(jsonPath("$.timestamp").value(org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void createDevice_ShouldFail_WhenPincodeIsNot7Digits() throws Exception {
        DeviceRequestDTO invalidDevice = new DeviceRequestDTO();
        invalidDevice.setPincode("123"); // invalid pincode
        invalidDevice.setStatus(DeviceStatus.READY.getCode());
        invalidDevice.setAvailability(true);
        invalidDevice.setTemperature(-1);

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDevice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Pincode must be exactly 7 digits")))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createDevice_ShouldFail_WhenStatusIsActiveButTemperatureIsInvalid() throws Exception {
        DeviceRequestDTO invalidDevice = new DeviceRequestDTO();
        invalidDevice.setPincode("7777777");
        invalidDevice.setStatus(DeviceStatus.ACTIVE.getCode()); // 1
        invalidDevice.setAvailability(true);
        invalidDevice.setTemperature(-1); // Invalid for ACTIVE

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDevice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Active devices can't have temperature < 0")))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createDevice_ShouldFail_WhenStatusIsReadyButTemperatureIsNotMinusOne() throws Exception {
        DeviceRequestDTO invalidDevice = new DeviceRequestDTO();
        invalidDevice.setPincode("8888888");
        invalidDevice.setStatus(DeviceStatus.READY.getCode()); // 0
        invalidDevice.setAvailability(true);
        invalidDevice.setTemperature(0); // Invalid for READY

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDevice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("devices can't have temperature > 0")))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createDevice_ShouldFail_WhenPincodeAlreadyExists() throws Exception {

        Device existingDevice = new Device();
        existingDevice.setPincode("9999999");
        existingDevice.setStatus(DeviceStatus.READY);
        existingDevice.setAvailability(true);
        existingDevice.setTemperature(-1);
        deviceRepository.save(existingDevice);


        DeviceRequestDTO duplicateDevice = new DeviceRequestDTO();
        duplicateDevice.setPincode("9999999");
        duplicateDevice.setStatus(DeviceStatus.READY.getCode());
        duplicateDevice.setAvailability(true);
        duplicateDevice.setTemperature(-1);

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(duplicateDevice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Pincode already exists")))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deleteDevice_ShouldDeleteDevice() throws Exception {

        mockMvc.perform(delete("/api/devices/" + readyTestDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Device deleted successfully"))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").value(org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void deleteDevice_ShouldReturnError_WhenDeviceDoesNotExist() throws Exception {
        long nonExistentId = 999L;

        mockMvc.perform(delete("/api/devices/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("DEVICE_DOES_NOT_EXIST"))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").value(org.hamcrest.Matchers.notNullValue()));
    }


    @Test
    void updateDevice_ShouldUpdateDevice() throws Exception {
        DeviceRequestDTO duplicatedDevice = new DeviceRequestDTO();
        duplicatedDevice.setPincode("5555555");
        duplicatedDevice.setStatus(DeviceStatus.READY.getCode());
        duplicatedDevice.setAvailability(false);
        duplicatedDevice.setTemperature(-1);

        mockMvc.perform(put("/api/devices/" + readyTestDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(duplicatedDevice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Device updated successfully")))
                .andExpect(jsonPath("$.data.temperature").value(duplicatedDevice.getTemperature()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void updateDevice_ShouldReturnError_WhenDeviceDoesNotExist() throws Exception {
        long nonExistentId = 999L;
        DeviceRequestDTO duplicatedDevice = new DeviceRequestDTO();
        duplicatedDevice.setPincode("1234567");
        duplicatedDevice.setStatus(DeviceStatus.READY.getCode());
        duplicatedDevice.setAvailability(false);
        duplicatedDevice.setTemperature(-1);

        mockMvc.perform(put("/api/devices/" + 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(duplicatedDevice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("DEVICE_DOES_NOT_EXIST")))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void updateDevice_ShouldReturnError_WhenTryToUpdateReadyDeviceWithInCorrectTemperature() throws Exception {
        long nonExistentId = 999L;
        DeviceRequestDTO duplicatedDevice = new DeviceRequestDTO();
        duplicatedDevice.setPincode("5555555");
        duplicatedDevice.setStatus(DeviceStatus.READY.getCode());
        duplicatedDevice.setAvailability(false);
        duplicatedDevice.setTemperature(5);

        mockMvc.perform(put("/api/devices/" + readyTestDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(duplicatedDevice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Inactive devices can't have temperature > or = 0")))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void updateDevice_ShouldReturnError_WhenTryToUpdateDeviceWithExistedPinCode() throws Exception {

        DeviceRequestDTO duplicatedDevice = new DeviceRequestDTO();
        duplicatedDevice.setPincode("1111111");
        duplicatedDevice.setStatus(DeviceStatus.READY.getCode());
        duplicatedDevice.setAvailability(false);
        duplicatedDevice.setTemperature(-1);

        mockMvc.perform(put("/api/devices/" + readyTestDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(duplicatedDevice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Pin Code Already Exist")))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    private static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}