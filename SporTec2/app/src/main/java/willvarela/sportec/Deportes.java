package willvarela.sportec;

import java.io.Serializable;

public class Deportes implements Serializable {

    String nombreDeporte;
    int imagen;

    public Deportes(String nombreDeporte, int imagen) {
        this.nombreDeporte = nombreDeporte;
        this.imagen = imagen;
    }

    public String getNombreDeporte() {
        return nombreDeporte;
    }

    public void setNombreDeporte(String nombreDeporte) {
        this.nombreDeporte = nombreDeporte;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
