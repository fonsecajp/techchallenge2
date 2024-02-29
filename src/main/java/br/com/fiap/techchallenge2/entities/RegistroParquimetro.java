package br.com.fiap.techchallenge2.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "registros")
public class RegistroParquimetro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Double valor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;
    @Enumerated(EnumType.STRING)
    private StatusPaquimetro status ;

    public RegistroParquimetro(){
    }

    public RegistroParquimetro(UUID uuid, LocalDateTime dataEntrada, LocalDateTime dataSaida, Double valor, StatusPaquimetro status) {
        this.uuid = uuid;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.valor = valor;
        this.status = status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Double getValor() {
        return valor;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }

    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
    public Veiculo getVeiculo() {
        return veiculo;
    }
    public StatusPaquimetro getStatus() {
        return status;
    }
    public void setStatus(StatusPaquimetro status) {
        this.status = status;
    }
}
