package rent_a_car.classes;

public class Precario {
    private int ID;
    private String grupo;
    private double valor;

    public Precario(int ID, String grupo, double valor) {
        this.ID = ID;
        this.grupo = grupo;
        this.valor = valor;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
