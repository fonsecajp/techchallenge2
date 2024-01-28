package br.com.fiap.techchallenge2.service;

import br.com.fiap.techchallenge2.dto.ParquimetroEntryResponseDTO;
import br.com.fiap.techchallenge2.dto.ParquimetroRequestDTO;
import br.com.fiap.techchallenge2.dto.ParquimetroResponseDTO;
import br.com.fiap.techchallenge2.entities.RegistroParquimetro;
import br.com.fiap.techchallenge2.entities.StatusPaquimetro;
import br.com.fiap.techchallenge2.entities.Veiculo;
import br.com.fiap.techchallenge2.exception.KeyMessages;
import br.com.fiap.techchallenge2.exception.NotFoundException;
import br.com.fiap.techchallenge2.mappers.ParquimetroMapper;
import br.com.fiap.techchallenge2.repositories.ParquimetroRepository;
import br.com.fiap.techchallenge2.repositories.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class ParquimetroService {

    @Autowired
    private ParquimetroRepository parquimetroRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    private final double TAXA_POR_HORA = 10.0;
    private final double TAXA_POR_15_MIN = 3.0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Transactional
    public ParquimetroEntryResponseDTO entrar(ParquimetroRequestDTO dto) {
        Veiculo veiculo = findOrCreateInDB(dto);
        verificaEntrada(veiculo, parquimetroRepository);
        RegistroParquimetro registroParquimetro = ParquimetroMapper.toParquimetroEntity(dto);
        registroParquimetro.setVeiculo(veiculo);
        parquimetroRepository.save(registroParquimetro);
        veiculo.getRegistroParquimetroList().add(registroParquimetro);
        veiculoRepository.save(veiculo);
        return ParquimetroMapper.entradaNoEstacionamento(registroParquimetro);
    }
//
    private void verificaEntrada(Veiculo veiculo, ParquimetroRepository parquimetroRepository) {
        Optional<RegistroParquimetro> reg = parquimetroRepository.findByVeiculoPlaca(veiculo.getPlaca());
        if(reg.isPresent()){
            if(reg.get().getStatus() == StatusPaquimetro.ENTROU_E_NAO_SAIU){
                throw new NotFoundException(KeyMessages.VEICULO_JA_ESTA_ESTACIONADO.getValue());
            }
        }
    }
    private void verificaSaida(Veiculo veiculo, ParquimetroRepository parquimetroRepository) {
        Optional<RegistroParquimetro> reg = parquimetroRepository.findByVeiculoPlaca(veiculo.getPlaca());
        if(reg.isPresent()){
            if(reg.get().getStatus() == StatusPaquimetro.ENTROU_E_SAIU){
                throw new NotFoundException(KeyMessages.VEICULO_JA_ESTA_ESTACIONADO.getValue());
            }
        }
    }


    public ParquimetroResponseDTO sair(ParquimetroRequestDTO dto) {
        Veiculo veiculo = findInDBSaida(dto);
        verificaSaida(veiculo, parquimetroRepository);
        RegistroParquimetro registroParquimetro = veiculo.getRegistroParquimetroList().get(veiculo.getRegistroParquimetroList().size() -1);
        registroParquimetro.setDataSaida(LocalDateTime.now());
        registroParquimetro.setStatus(StatusPaquimetro.ENTROU_E_SAIU);
        parquimetroRepository.saveAndFlush(registroParquimetro);
        calculaValorDevido(registroParquimetro);
        return ParquimetroMapper.saidaDoEstacionamento(registroParquimetro);
    }

    private void calculaValorDevido(RegistroParquimetro registroParquimetro) {
        Duration duration = Duration.between(registroParquimetro.getDataEntrada(), registroParquimetro.getDataSaida());
        registroParquimetro.setValor((duration.toHours() * TAXA_POR_HORA) + (Math.ceil(duration.toMinutesPart() / 15.0) * TAXA_POR_15_MIN));
    }

    private Veiculo findOrCreateInDB(ParquimetroRequestDTO dto) {
        final Veiculo veiculo = veiculoRepository.findByPlaca(dto.placa()).orElse(new Veiculo(dto.placa(), dto.tipo(), new ArrayList<>()));
        return veiculo;
    }

    private Veiculo findInDBSaida(ParquimetroRequestDTO dto) {
        final Veiculo veiculo = veiculoRepository.findByPlaca(dto.placa()).orElseThrow(()
                -> new NotFoundException(KeyMessages.PLACA_NOT_FOUND.getValue()));
        return veiculo;
    }
}
