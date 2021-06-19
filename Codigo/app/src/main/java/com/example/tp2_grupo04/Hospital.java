package com.example.tp2_grupo04;

public class Hospital {
    private String name;
    private String address;
    private String telephone;
    private Double latitude;
    private Double longitude;
    public static final String TAG_NAME_HOSPITAL = "HospitalName";
    public static final String TAG_ADDRESS_HOSPITAL = "HospitalAddress";
    public static final String TAG_TELEPHONE_HOSPITAL = "HospitalTelephone";
    public static final String TAG_DISTANCE_HOSPITAL = "HospitalDistance";

    public Hospital(String name, String address, String telephone, String coordinates) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        coordinates = coordinates.replace("[", "");
        coordinates = coordinates.replace("]", "");
        String[] parts = coordinates.split(",");
        this.latitude = Double.valueOf(parts[0]);
        this.longitude = Double.valueOf(parts[1]);
    }

    public Hospital() {
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

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", latitude='" + latitude.toString() + '\'' +
                ", longitude='" + longitude.toString() + '\'' +
                '}';
    }
}
