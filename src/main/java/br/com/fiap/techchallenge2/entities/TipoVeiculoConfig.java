package br.com.fiap.techchallenge2.entities;

import br.com.fiap.techchallenge2.exception.KeyMessages;
import br.com.fiap.techchallenge2.exception.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TipoVeiculoConfig {

    MOTOCICLETA(5.0,2.0,TipoVeiculo.MOTOCICLETA),
    CARRO(8.0,5.0,TipoVeiculo.CARRO),
    CAMINHONETE(10.0,6.5,TipoVeiculo.CAMINHONETE);

    private final Double valorHora;
    private final Double valorMinino;
    private final TipoVeiculo tipoVeiculo;

    public static TipoVeiculoConfig retornarConfigVeiculo(TipoVeiculo tipoVeiculo){
        return Arrays.stream(TipoVeiculoConfig.values())
                .filter(veiculo -> veiculo.getTipoVeiculo().equals(tipoVeiculo))
                .findFirst()
                .orElseThrow(()-> new NotFoundException(KeyMessages.TIPO_DE_VEICULO_NAO_CADASTRADO.getValue()));
    }
}
