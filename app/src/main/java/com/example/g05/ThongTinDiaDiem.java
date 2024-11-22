package com.example.g05;

public class ThongTinDiaDiem {
    private String id;
    private String name;
    private String type;
    private String address;
    private String imageUrl;
    private String sdt;

    public ThongTinDiaDiem() {
    }

    public ThongTinDiaDiem(String id, String name, String type, String address, String imageUrl, String sdt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.address = address;
        this.imageUrl = imageUrl;
        this.sdt = sdt;
    }

    public ThongTinDiaDiem(String name, String type, String address, String imageUrl, String sdt) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.imageUrl = imageUrl;
        this.sdt = sdt;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}

