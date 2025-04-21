package viewModel.modelDTO;


public class MedicamentDTO {
    //aplicatii distribuite -> DTO, Nu intre view si presenter
    private int id;
    private String name;
    private String producer;
    private float price;
    private String imagePath;

    public MedicamentDTO(int id, String name, String producer, float price, String imagePath) {
        this.id = id;
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.imagePath = imagePath;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

