package com.VOIS_Task.IotDevices.enumerators;

public enum DeviceStatus {
    READY(0),
    ACTIVE(1);

    private final int code;

    DeviceStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static DeviceStatus fromCode(int code) {
        for (DeviceStatus status : DeviceStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code for DeviceStatus: " + code);
    }
}
