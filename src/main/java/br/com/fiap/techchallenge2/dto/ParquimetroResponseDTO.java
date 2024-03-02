package br.com.fiap.techchallenge2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record ParquimetroResponseDTO(UUID uuid,
                                     String placa,
                                     br.com.fiap.techchallenge2.entities.TipoVeiculo tipoVeiculo,
                                     @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
                                     LocalDateTime dataEntrada,
                                     @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
                                     LocalDateTime dataSaida,
                                     Double valorDevido) {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    public String getValorDevido() {
        if (valorDevido != null) {
            return String.format("R$ %.2f", valorDevido);
        }
        return null;
    }

}
