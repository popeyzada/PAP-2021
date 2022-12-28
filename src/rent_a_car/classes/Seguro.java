package rent_a_car.classes;

public class Seguro {
    private int ID;
    private String nome;
    private double preco_diario;

    public Seguro(int ID, String nome, double preco_diario) {
        this.ID = ID;
        this.nome = nome;
        this.preco_diario = preco_diario;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco_diario() {
        return preco_diario;
    }

    public void setPreco_diario(double preco_diario) {
        this.preco_diario = preco_diario;
    }
}
