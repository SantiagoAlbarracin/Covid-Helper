package com.example.tp2_grupo04;

public class Hospital {
    private String name;
    private String address;
    private String telephone;
    private String coordinates;

    public Hospital(String name, String address, String telephone, String coordinates) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.coordinates = coordinates;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setGeolocation(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getGeolocation() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", geolocation='" + coordinates + '\'' +
                '}';
    }
}
