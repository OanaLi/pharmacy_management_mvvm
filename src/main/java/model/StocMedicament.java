package model;

import java.time.LocalDate;

public class StocMedicament {
    private int pharmacyId;
    private int medicamentId;
    private String name;
    private String producer;
    private float price;
    private String imagePath;
    private int stock;
    private LocalDate expirationDate;

    public StocMedicament(int pharmacyId, int medicamentId, int stock, LocalDate expirationDate) {
        this.pharmacyId = pharmacyId;
        this.medicamentId = medicamentId;
        this.stock = stock;
        this.expirationDate = expirationDate;
    }

    public StocMedicament(int pharmacyId, int medicamentId, String name, String producer, float price,
                          String imagePath, int stock, LocalDate expirationDate) {
        this.pharmacyId = pharmacyId;
        this.medicamentId = medicamentId;
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.imagePath = imagePath;
        this.stock = stock;
        this.expirationDate = expirationDate;
    }


    public int getPharmacyId() {
        return pharmacyId;
    }

    public int getMedicamentId() {
        return medicamentId;
    }

    public String getName() {
        return name;
    }

    public String getProducer() {
        return producer;
    }

    public float getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getStock() {
        return stock;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
}
