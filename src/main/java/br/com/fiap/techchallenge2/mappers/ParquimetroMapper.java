package br.com.fiap.techchallenge2.mappers;

import br.com.fiap.techchallenge2.dto.ParquimetroEntryResponseDTO;
import br.com.fiap.techchallenge2.dto.ParquimetroRequestDTO;
import br.com.fiap.techchallenge2.dto.ParquimetroResponseDTO;
import br.com.fiap.techchallenge2.entities.RegistroParquimetro;
import br.com.fiap.techchallenge2.entities.StatusPaquimetro;

import java.time.LocalDateTime;

public interface ParquimetroMapper {
    static RegistroParquimetro toParquimetroEntity(ParquimetroRequestDTO requestDTO) {
        return new RegistroParquimetro(null, LocalDateTime.now(), null, null, StatusPaquimetro.ENTROU_E_NAO_SAIU);
    }

    static ParquimetroEntryResponseDTO entradaNoEstacionamento(RegistroParquimetro registroParquimetro) {
        return new ParquimetroEntryResponseDTO(registroParquimetro.getUuid(),
                registroParquimetro.getVeiculo().getPlaca(),
                registroParquimetro.getVeiculo().getTipoVeiculo(),
                registroParquimetro.getDataEntrada());
    }

    static ParquimetroResponseDTO saidaDoEstacionamento(RegistroParquimetro registroParquimetro) {
        return new ParquimetroResponseDTO(
                registroParquimetro.getUuid(),
                registroParquimetro.getVeiculo().getPlaca(),
                registroParquimetro.getVeiculo().getTipoVeiculo(),
                registroParquimetro.getDataEntrada(),
                registroParquimetro.getDataSaida(),
                registroParquimetro.getValor());
    }

}
