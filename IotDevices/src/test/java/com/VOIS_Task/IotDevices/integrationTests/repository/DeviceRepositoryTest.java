package com.VOIS_Task.IotDevices.integrationTests.repository;

import com.VOIS_Task.IotDevices.entities.Device;
import com.VOIS_Task.IotDevices.enumerators.DeviceStatus;
import com.VOIS_Task.IotDevices.repository.DeviceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    @DisplayName("Test saving and retrieving a device by pincode")
    void testFindByPincode() {
        Device device = new Device();
        device.setPincode("1234567");
        device.setStatus(DeviceStatus.READY);
        device.setTemperature(0);
        device.setAvailability(false);

        deviceRepository.save(device);

        Device found = deviceRepository.getByPincode("1234567");
        assertThat(found).isNotNull();
        assertThat(found.getPincode()).isEqualTo("1234567");
    }

    @Test
    @DisplayName("Test find all available devices sorted by pincode ascending")
    void testFindAllByAvailabilityTrueOrderByPincodeAsc() {
        Device device1 = new Device("1111111", DeviceStatus.ACTIVE, 10, true);
        Device device2 = new Device("2222222", DeviceStatus.ACTIVE, 12, true);
        Device device3 = new Device("3333333", DeviceStatus.READY, 0, false);

        deviceRepository.saveAll(List.of(device1, device2, device3));

        List<Device> availableDevices = deviceRepository.findAllByAvailabilityTrueOrderByPincodeAsc();

        assertThat(availableDevices).hasSize(2);
        assertThat(availableDevices.get(0).getPincode()).isEqualTo("1111111");
        assertThat(availableDevices.get(1).getPincode()).isEqualTo("2222222");
    }
}
