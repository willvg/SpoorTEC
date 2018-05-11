package willvarela.sportec;

import java.io.Serializable;
import java.util.ArrayList;

public class EntidadEquipo implements Serializable {
    String nombre, imagen, activo;
    ArrayList<String> integrantes;
    int ganados;

    public EntidadEquipo(String nombre, String imagen, String activo, ArrayList<String> integrantes, int ganados) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.activo = activo;
        this.integrantes = integrantes;
        this.ganados = ganados;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public ArrayList<String> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(ArrayList<String> integrantes) {
        this.integrantes = integrantes;
    }

    public int getGanados() {
        return ganados;
    }

    public void setGanados(int ganados) {
        this.ganados = ganados;
    }
}
