package br.com.fiap.techchallenge2.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "veiculo")
public class Veiculo {

    @Id
    private String placa;
    private String tipo;
    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RegistroParquimetro> registros = new ArrayList<>();

    public Veiculo() {
    }
    public Veiculo(String placa, String tipo, List<RegistroParquimetro> registros) {
        this.placa = placa;
        this.tipo = tipo;
        this.registros= registros;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<RegistroParquimetro> getRegistroParquimetroList() {
        return registros;
    }

    public void setRegistroParquimetroList(List<RegistroParquimetro> registros) {
        this.registros = registros;
    }
}
