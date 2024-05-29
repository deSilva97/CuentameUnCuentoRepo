package es.unir.cuentameuncuento.models;

public class Category {
    private final int id;
    private final String name;
    private final String description;
    private final int image_id;

    public Category(int id, String name, String description, int image_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image_id = image_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImage_id() {
        return image_id;
    }
}