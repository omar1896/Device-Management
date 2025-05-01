package com.VOIS_Task.IotDevices.repository;

import com.VOIS_Task.IotDevices.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    public Device getById(long id);

    public Device getByPincode(String pincode);

    List<Device> findAllByAvailabilityTrueOrderByPincodeAsc();
}