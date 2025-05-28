package com.VOIS_Task.IotDevices.unitTests.services;

import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.entities.Device;
import com.VOIS_Task.IotDevices.enumerators.DeviceStatus;
import com.VOIS_Task.IotDevices.exceptionhandler.DeviceExceptionHandler;
import com.VOIS_Task.IotDevices.mapper.DeviceMapper;
import com.VOIS_Task.IotDevices.repository.DeviceRepository;
import com.VOIS_Task.IotDevices.services.DeviceConfigurationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceConfigurationServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceConfigurationService deviceConfigurationService;

    private Device testDevice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testDevice = new Device();
        testDevice.setId(1L);
        testDevice.setStatus(DeviceStatus.READY);
        testDevice.setAvailability(true);
    }

    @Test
    void testConfigureDevice_Success() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));

        DeviceResponseDTO response = deviceConfigurationService.configureDevice(1L);

        assertNotNull(response);
        assertEquals(DeviceStatus.ACTIVE, testDevice.getStatus());
        assertTrue(testDevice.getTemperature() >= 0 && testDevice.getTemperature() <= 10);

        verify(deviceRepository, times(1)).save(testDevice);
    }


    @Test
    void testConfigureDevice_DeviceNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        DeviceExceptionHandler exception = assertThrows(DeviceExceptionHandler.class, () -> {
            deviceConfigurationService.configureDevice(1L);
        });

        assertEquals("DEVICE_DOES_NOT_EXIST", exception.getMessage());
    }

    @Test
    void testConfigureDevice_AlreadyActivated() {
        testDevice.setStatus(DeviceStatus.ACTIVE);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));

        DeviceExceptionHandler exception = assertThrows(DeviceExceptionHandler.class, () -> {
            deviceConfigurationService.configureDevice(1L);
        });

        assertEquals("Device Already Activated", exception.getMessage());
    }

    @Test
    void testConfigureDevice_NotAvailable() {
        testDevice.setAvailability(false);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));

        DeviceExceptionHandler exception = assertThrows(DeviceExceptionHandler.class, () -> {
            deviceConfigurationService.configureDevice(1L);
        });

        assertEquals("Device Is Not Available", exception.getMessage());
    }
}
