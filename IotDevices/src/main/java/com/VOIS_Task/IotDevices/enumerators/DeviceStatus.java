package com.VOIS_Task.IotDevices.enumerators;

import java.util.Objects;

public enum DeviceStatus {
    READY(0, "Ready"),
    ACTIVE(1, "Active");

    private final Integer code;
    private final String label;

    DeviceStatus(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public Integer getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static DeviceStatus fromCode(Integer code) {
        for (DeviceStatus status : DeviceStatus.values()) {
            if (Objects.equals(status.code, code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code for DeviceStatus: " + code);
    }

    public static DeviceStatus fromLabel(String label) {
        for (DeviceStatus status : DeviceStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid label for DeviceStatus: " + label);
    }
}
