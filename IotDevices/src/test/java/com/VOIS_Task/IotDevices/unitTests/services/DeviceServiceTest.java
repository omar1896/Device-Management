package com.VOIS_Task.IotDevices.unitTests.services;

import com.VOIS_Task.IotDevices.dtos.DeviceRequestDTO;
import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.entities.Device;
import com.VOIS_Task.IotDevices.enumerators.DeviceStatus;
import com.VOIS_Task.IotDevices.exceptionhandler.DeviceExceptionHandler;
import com.VOIS_Task.IotDevices.repository.DeviceRepository;
import com.VOIS_Task.IotDevices.services.DeviceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private DeviceRequestDTO validRequest;

    @BeforeEach
    void setup() {
        validRequest = new DeviceRequestDTO();
        validRequest.setPincode("12345");
        validRequest.setTemperature(-1);
        validRequest.setStatus(DeviceStatus.READY.getCode());
        validRequest.setAvailability(true);
    }

    @Test
    void shouldCreateDeviceSuccessfully() {
        when(deviceRepository.getByPincode("12345")).thenReturn(null);
        when(deviceRepository.save(any(Device.class))).thenAnswer(i -> i.getArgument(0));

        DeviceResponseDTO result = deviceService.createDevice(validRequest);

        assertThat(result).isNotNull();
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void shouldThrowWhenActiveDeviceHasNegativeTemperature() {
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setPincode("1234567");
        request.setTemperature(-1);
        request.setStatus(1);

        when(deviceRepository.getByPincode("1234567")).thenReturn(null);

        assertThrows(DeviceExceptionHandler.class, () -> deviceService.createDevice(request));
    }

    @Test
    void shouldThrowWhenInactiveDeviceHasNullPinCodeAndTemperature() {
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setPincode(null);
        request.setTemperature(null);
        request.setStatus(0);

        assertThrows(DeviceExceptionHandler.class, () -> deviceService.createDevice(request));
    }

    @Test
    void shouldThrowWhenInactiveDeviceHasPositiveTemperature() {
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setPincode("12345");
        request.setTemperature(10);
        request.setStatus(0);

        when(deviceRepository.getByPincode("12345")).thenReturn(null);

        assertThrows(DeviceExceptionHandler.class, () -> deviceService.createDevice(request));
    }

    @Test
    void shouldThrowWhenDuplicatePinCode() {
        Device existing = new Device();
        when(deviceRepository.getByPincode("12345")).thenReturn(existing);

        assertThatThrownBy(() -> deviceService.createDevice(validRequest))
                .isInstanceOf(DeviceExceptionHandler.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldUpdateDeviceSuccessfully() {
        Device existingDevice = new Device();
        existingDevice.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.getByPincode("12345")).thenReturn(null);

        DeviceResponseDTO updated = deviceService.updateDevice(validRequest, 1L);

        assertThat(updated).isNotNull();
        verify(deviceRepository).save(existingDevice);
    }

    @Test
    void shouldThrowWhenPinCodeBelongsToAnotherDeviceDuringUpdate() {
        // Arrange
        Long existingId = 1L;
        Long conflictingId = 2L;

        DeviceRequestDTO requestDTO = new DeviceRequestDTO();
        requestDTO.setPincode("12345");
        requestDTO.setStatus(1); // ACTIVE
        requestDTO.setTemperature(10); // valid for ACTIVE
        requestDTO.setAvailability(true);

        Device deviceInDb = new Device();
        deviceInDb.setId(existingId);
        deviceInDb.setStatus(DeviceStatus.READY); // from DB
        deviceInDb.setAvailability(false);
        deviceInDb.setTemperature(0);
        deviceInDb.setPincode("54321");

        Device conflictingDevice = new Device();
        conflictingDevice.setId(conflictingId); // conflict with current device

        when(deviceRepository.findById(existingId)).thenReturn(Optional.of(deviceInDb));
        when(deviceRepository.getByPincode("12345")).thenReturn(conflictingDevice); // Conflict here

        // Act & Assert
        DeviceExceptionHandler exception = assertThrows(DeviceExceptionHandler.class,
                () -> deviceService.updateDevice(requestDTO, existingId));

        assertThat(exception.getMessage()).contains("Pin Code Already Exist");
        verify(deviceRepository, never()).save(any()); // Make sure save was not called
    }


    @Test
    void shouldThrowWhenDeviceNotFoundForUpdate() {
        when(deviceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.updateDevice(validRequest, 99L))
                .isInstanceOf(DeviceExceptionHandler.class)
                .hasMessageContaining("DEVICE_DOES_NOT_EXIST");
    }

    @Test
    void shouldDeleteDeviceSuccessfully() {
        Device device = new Device();
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        deviceService.deleteDevice(1L);

        verify(deviceRepository).delete(device);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentDevice() {
        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.deleteDevice(2L))
                .isInstanceOf(DeviceExceptionHandler.class)
                .hasMessageContaining("DEVICE_DOES_NOT_EXIST");
    }

    @Test
    void getAllAvailableDevices_ShouldReturnReversedSortedDTOs() {

        Device device1 = new Device();
        device1.setPincode("1234567");
        device1.setAvailability(true);
        device1.setTemperature(1);
        device1.setStatus(DeviceStatus.ACTIVE);

        Device device2 = new Device();
        device2.setPincode("2345678");
        device2.setAvailability(true);
        device2.setTemperature(1);
        device2.setStatus(DeviceStatus.ACTIVE);
        List<Device> devices = List.of(device1, device2);
        Mockito.when(deviceRepository.findAllByAvailabilityTrueOrderByPincodeAsc()).thenReturn(devices);

        // When
        List<DeviceResponseDTO> result = deviceService.getAllAvailableDevices();

        // Then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(true, result.get(0).getAvailability());
        Assertions.assertEquals(true, result.get(1).getAvailability());

        Mockito.verify(deviceRepository).findAllByAvailabilityTrueOrderByPincodeAsc();
    }
    @Test
    void shouldThrowException_WhenActivatingWithNegativeTemperature() {
        Device existing = new Device();
        existing.setTemperature(-5);

        DeviceRequestDTO updateDto = new DeviceRequestDTO();
        updateDto.setStatus(1);  // Trying to activate
        updateDto.setTemperature(-10);

        DeviceExceptionHandler ex = Assertions.assertThrows(DeviceExceptionHandler.class, () ->
                deviceService.validateStatusAndTemperatureUpdate(existing, updateDto));

        Assertions.assertEquals("Active devices can't have temperature < 0", ex.getMessage());
    }

    @Test
    void shouldThrowException_WhenDeactivatingWithPositiveTemperature() {
        Device existing = new Device();
        existing.setTemperature(5);

        DeviceRequestDTO updateDto = new DeviceRequestDTO();
        updateDto.setStatus(0);  // Trying to deactivate
        updateDto.setTemperature(10);

        DeviceExceptionHandler ex = Assertions.assertThrows(DeviceExceptionHandler.class, () ->
                deviceService.validateStatusAndTemperatureUpdate(existing, updateDto));

        Assertions.assertEquals("Inactive devices can't have temperature > or = 0", ex.getMessage());
    }

    @Test
    void shouldPass_WhenActivatingWithPositiveTemperature() {
        Device existing = new Device();
        existing.setTemperature(5);

        DeviceRequestDTO updateDto = new DeviceRequestDTO();
        updateDto.setStatus(1);  // Activate
        updateDto.setTemperature(10);

        Assertions.assertDoesNotThrow(() ->
                deviceService.validateStatusAndTemperatureUpdate(existing, updateDto));
    }

    @Test
    void shouldPass_WhenDeactivatingWithZeroTemperature() {
        Device existing = new Device();
        existing.setTemperature(0);

        DeviceRequestDTO updateDto = new DeviceRequestDTO();
        updateDto.setStatus(0);  // Deactivate
        updateDto.setTemperature(0);

        Assertions.assertDoesNotThrow(() ->
                deviceService.validateStatusAndTemperatureUpdate(existing, updateDto));
    }

    @Test
    void shouldUseExistingTemp_IfNotProvidedInDto() {
        Device existing = new Device();
        existing.setTemperature(-5);

        DeviceRequestDTO updateDto = new DeviceRequestDTO();
        updateDto.setStatus(1);  // Activate, no temp in DTO

        DeviceExceptionHandler ex = Assertions.assertThrows(DeviceExceptionHandler.class, () ->
                deviceService.validateStatusAndTemperatureUpdate(existing, updateDto));

        Assertions.assertEquals("Active devices can't have temperature < 0", ex.getMessage());
    }

}
