package willvarela.sportec;

public class EntidadResuldatos {

    String Equipo1, Equipo2;
    int resultado1, resultado2;

    public EntidadResuldatos(String equipo1, String equipo2, int resultado1, int resultado2) {
        Equipo1 = equipo1;
        Equipo2 = equipo2;
        this.resultado1 = resultado1;
        this.resultado2 = resultado2;
    }

    public String getEquipo1() {
        return Equipo1;
    }

    public void setEquipo1(String equipo1) {
        Equipo1 = equipo1;
    }

    public String getEquipo2() {
        return Equipo2;
    }

    public void setEquipo2(String equipo2) {
        Equipo2 = equipo2;
    }

    public int getResultado1() {
        return resultado1;
    }

    public void setResultado1(int resultado1) {
        this.resultado1 = resultado1;
    }

    public int getResultado2() {
        return resultado2;
    }

    public void setResultado2(int resultado2) {
        this.resultado2 = resultado2;
    }
}
