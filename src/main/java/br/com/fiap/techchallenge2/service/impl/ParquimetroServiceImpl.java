package br.com.fiap.techchallenge2.service.impl;

import br.com.fiap.techchallenge2.dto.ParquimetroRequestDTO;
import br.com.fiap.techchallenge2.entities.RegistroParquimetro;
import br.com.fiap.techchallenge2.entities.StatusPaquimetro;
import br.com.fiap.techchallenge2.entities.Veiculo;
import br.com.fiap.techchallenge2.exception.KeyMessages;
import br.com.fiap.techchallenge2.exception.NotFoundException;
import br.com.fiap.techchallenge2.mappers.ParquimetroMapper;
import br.com.fiap.techchallenge2.repositories.ParquimetroRepository;
import br.com.fiap.techchallenge2.repositories.VeiculoRepository;
import br.com.fiap.techchallenge2.service.ParquimetroService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParquimetroServiceImpl implements ParquimetroService {


    private final ParquimetroRepository parquimetroRepository;
    private final VeiculoRepository veiculoRepository;
    private final double TAXA_POR_HORA = 10.0;
    private final double TAXA_POR_15_MIN = 3.0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public ParquimetroServiceImpl(ParquimetroRepository parquimetroRepository, VeiculoRepository veiculoRepository) {
        this.parquimetroRepository = parquimetroRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional
    public RegistroParquimetro entrar(ParquimetroRequestDTO dto) {
        Veiculo veiculo = findOrCreateInDB(dto);
        verificaEntrada(veiculo, parquimetroRepository);
        RegistroParquimetro registroParquimetro = ParquimetroMapper.toParquimetroEntity(dto);
        registroParquimetro.setVeiculo(veiculo);
        parquimetroRepository.save(registroParquimetro);
        veiculo.getRegistroParquimetroList().add(registroParquimetro);
        veiculoRepository.save(veiculo);
        return registroParquimetro;
    }
    @Transactional
    public RegistroParquimetro sair(ParquimetroRequestDTO dto) {
        Veiculo veiculo = findInDBSaida(dto);
        verificaSaida(veiculo, parquimetroRepository);
        RegistroParquimetro registroParquimetro = veiculo.getRegistroParquimetroList().get(veiculo.getRegistroParquimetroList().size() -1);
        registroParquimetro.setDataSaida(LocalDateTime.now());
        registroParquimetro.setStatus(StatusPaquimetro.ENTROU_E_SAIU);
        parquimetroRepository.save(registroParquimetro);
        calculaValorDevido(registroParquimetro);
        return registroParquimetro;
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

    private void verificaEntrada(Veiculo veiculo, ParquimetroRepository parquimetroRepository) {
        Optional<List<RegistroParquimetro>> registros = parquimetroRepository.findAllByVeiculoPlaca(veiculo.getPlaca());
        if(consultaValidadeRegistro(registros , StatusPaquimetro.ENTROU_E_NAO_SAIU)){
            throw new NotFoundException(KeyMessages.VEICULO_JA_ESTA_ESTACIONADO.getValue());
        }
    }

    private void verificaSaida(Veiculo veiculo, ParquimetroRepository parquimetroRepository) {
        Optional<List<RegistroParquimetro>> registros = parquimetroRepository.findAllByVeiculoPlaca(veiculo.getPlaca());
        if(consultaValidadeRegistro(registros , StatusPaquimetro.ENTROU_E_SAIU)){
            throw new NotFoundException(KeyMessages.VEICULO_JA_SAIU.getValue());
        }
    }

    private Boolean consultaValidadeRegistro(Optional<List<RegistroParquimetro>> registros, StatusPaquimetro statusPaquimetro){
        if(registros.isPresent()){
            if(registros.get().size()>1) {
                if(registros.get().get(registros.get().size() - 1).getStatus() == statusPaquimetro){
                    return true;
                }
            } else if (registros.get().size() == 1 && registros.get().get(0).getStatus() == statusPaquimetro) {
                return true;
            }
        }
        return false;
    }
}
