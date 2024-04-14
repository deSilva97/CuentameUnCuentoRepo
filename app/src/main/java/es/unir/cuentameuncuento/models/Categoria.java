package es.unir.cuentameuncuento.models;

public class Categoria {
    private int id;
    private String nombre;
    private String descripcion;
    private int imagenId;

    public Categoria(int id, String nombre, String descripcion, int imagenId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenId = imagenId;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getImagenId() {
        return imagenId;
    }
}