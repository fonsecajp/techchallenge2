package br.com.fiap.techchallenge2.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "veiculo")
public class Veiculo {

    @Id
    private String placa;
    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipoVeiculo;
    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RegistroParquimetro> registros = new ArrayList<>();

    public Veiculo() {
    }
    public Veiculo(String placa, TipoVeiculo tipoVeiculo, List<RegistroParquimetro> registros) {
        this.placa = placa;
        this.tipoVeiculo = tipoVeiculo;
        this.registros= registros;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(TipoVeiculo tipo) {
        this.tipoVeiculo = tipo;
    }

    public List<RegistroParquimetro> getRegistroParquimetroList() {
        return registros;
    }

}
