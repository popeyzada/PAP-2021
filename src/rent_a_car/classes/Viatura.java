package rent_a_car.classes;

import java.sql.Date;

public class Viatura {
    private int ID;
    private String grupo;
    private String marca;
    private String modelo;
    private int ano;
    private String combustivel;
    private String caixa_vel;
    private Date fim_garantia;
    private int portas;
    private int lugares;
    private String matricula;
    private Date prox_inspecao;
    private String tanque_combustivel;

    public Viatura(int ID, String grupo, String marca, String modelo, int ano, String combustivel, String caixa_vel, Date fim_garantia, int portas, int lugares, String matricula, Date prox_inspecao, String tanque_combustivel) {
        this.ID = ID;
        this.grupo = grupo;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.combustivel = combustivel;
        this.caixa_vel = caixa_vel;
        this.fim_garantia = fim_garantia;
        this.portas = portas;
        this.lugares = lugares;
        this.matricula = matricula;
        this.prox_inspecao = prox_inspecao;
        this.tanque_combustivel = tanque_combustivel;
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }

    public String getCaixa_vel() {
        return caixa_vel;
    }

    public void setCaixa_vel(String caixa_vel) {
        this.caixa_vel = caixa_vel;
    }

    public Date getFim_garantia() {
        return fim_garantia;
    }

    public void setFim_garantia(Date fim_garantia) {
        this.fim_garantia = fim_garantia;
    }

    public int getPortas() {
        return portas;
    }

    public void setPortas(int portas) {
        this.portas = portas;
    }

    public int getLugares() {
        return lugares;
    }

    public void setLugares(int lugares) {
        this.lugares = lugares;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Date getProx_inspecao() {
        return prox_inspecao;
    }

    public void setProx_inspecao(Date prox_inspecao) {
        this.prox_inspecao = prox_inspecao;
    }

    public String getTanque_combustivel() {
        return tanque_combustivel;
    }

    public void setTanque_combustivel(String tanque_combustivel) {
        this.tanque_combustivel = tanque_combustivel;
    }

}
