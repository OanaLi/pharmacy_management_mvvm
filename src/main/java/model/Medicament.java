package model;


public class Medicament {
    private int id;
    private String name;
    private Producer producer;
    private float price;
    private String image;

    public Medicament(int id, String name, Producer producer, float price, String image) {
        this.id = id;
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.image = image;
    }

    public String getProducer() {
        return producer.name(); //intoarce enum ca string
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
