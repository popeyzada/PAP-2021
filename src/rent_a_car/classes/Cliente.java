package rent_a_car.classes;

import java.sql.Date;

public class Cliente {
    private int ID;
    private String nome;
    private int contato;
    private Date data_nascimento;
    private String genero;
    private String cidade;
    private String morada;
    private String codigo_postal;
    private String pais;
    private String num_carta_conducao;
    private String carta_conducaoVAL;
    private String num_passaporte;
    private int contribuinte;
    private String email;
    private String empresa;

    public Cliente(int ID, String nome, int contato, Date data_nascimento, String genero, String cidade, String morada, String codigo_postal, String pais, String num_carta_conducao, String carta_conducaoVAL, String num_passaporte, int contribuinte, String email, String empresa) {
        this.ID = ID;
        this.nome = nome;
        this.contato = contato;
        this.data_nascimento = data_nascimento;
        this.genero = genero;
        this.cidade = cidade;
        this.morada = morada;
        this.codigo_postal = codigo_postal;
        this.pais = pais;
        this.num_carta_conducao = num_carta_conducao;
        this.carta_conducaoVAL = carta_conducaoVAL;
        this.num_passaporte = num_passaporte;
        this.contribuinte = contribuinte;
        this.email = email;
        this.empresa = empresa;
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
    public int getContato() {
        return contato;
    }

    public void setContato(int contato) {
        this.contato = contato;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNum_carta_conducao() {
        return num_carta_conducao;
    }

    public void setNum_carta_conducao(String num_carta_conducao) {
        this.num_carta_conducao = num_carta_conducao;
    }

    public String getCarta_conducaoVAL() {
        return carta_conducaoVAL;
    }

    public void setCarta_conducaoVAL(String carta_conducaoVAL) {
        this.carta_conducaoVAL = carta_conducaoVAL;
    }

    public String getNum_passaporte() {
        return num_passaporte;
    }

    public void setNum_passaporte(String num_passaporte) {
        this.num_passaporte = num_passaporte;
    }

    public int getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(int contribuinte) {
        this.contribuinte = contribuinte;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }


}
