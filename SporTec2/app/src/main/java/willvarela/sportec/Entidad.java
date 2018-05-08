package willvarela.sportec;

import java.io.Serializable;

public class Entidad implements Serializable{

    private String image, descripcion, fecha, tipo, titulo;

    public Entidad(String image, String descripcion, String fecha, String tipo , String titulo) {
        this.image = image;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipo = tipo;
        this.titulo = titulo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
