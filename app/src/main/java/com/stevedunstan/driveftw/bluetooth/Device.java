package com.stevedunstan.driveftw.bluetooth;

public class Device {
    public final String address;
    public final String name;
    public Device(String address, String name) {
        this.address = address;
        this.name = name;
    }
    public String toString() { return name; }
}
