package br.com.fiap.techchallenge2.dto;

import br.com.fiap.techchallenge2.entities.TipoVeiculo;

public record ParquimetroRequestDTO(String placa,
                                    TipoVeiculo tipoVeiculo) {
}
