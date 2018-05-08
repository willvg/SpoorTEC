package willvarela.sportec;

public class Entidad {

    private String image, descripcion, fecha;

    public Entidad(String image, String descripcion, String fecha) {
        this.image = image;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
