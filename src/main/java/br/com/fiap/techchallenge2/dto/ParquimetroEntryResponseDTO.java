package br.com.fiap.techchallenge2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record ParquimetroEntryResponseDTO(UUID uuid,
                                          String placa,
                                          br.com.fiap.techchallenge2.entities.TipoVeiculo tipoVeiculo,
                                          @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                          LocalDateTime dataEntrada) {
}
