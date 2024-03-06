package br.com.fiap.techchallenge2.service.impl;

import br.com.fiap.techchallenge2.dto.ParquimetroRequestDTO;
import br.com.fiap.techchallenge2.entities.RegistroParquimetro;
import br.com.fiap.techchallenge2.entities.StatusPaquimetro;
import br.com.fiap.techchallenge2.entities.TipoVeiculo;
import br.com.fiap.techchallenge2.entities.TipoVeiculoConfig;
import br.com.fiap.techchallenge2.entities.Veiculo;
import br.com.fiap.techchallenge2.exception.KeyMessages;
import br.com.fiap.techchallenge2.exception.NotFoundException;
import br.com.fiap.techchallenge2.mappers.ParquimetroMapper;
import br.com.fiap.techchallenge2.repositories.ParquimetroRepository;
import br.com.fiap.techchallenge2.repositories.VeiculoRepository;
import br.com.fiap.techchallenge2.service.ParquimetroService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


    public ParquimetroServiceImpl(ParquimetroRepository parquimetroRepository, VeiculoRepository veiculoRepository) {
        this.parquimetroRepository = parquimetroRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional
    @CacheEvict(value = "registroParquimetroPorPlaca", key = "#dto.placa", allEntries = false)
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

    @Override
    @Cacheable(value = "registroParquimetroPorPlaca", key = "#placa")
    public List<RegistroParquimetro> getByPlaca(String placa) {
        Optional<List<RegistroParquimetro>> todosRegistros = parquimetroRepository.findAllByVeiculoPlaca(placa);
        if (todosRegistros.isPresent()) {
            return todosRegistros.get();
        }
        throw new NotFoundException(KeyMessages.PLACA_NOT_FOUND.getValue());
    }

    @Transactional
    @CacheEvict(value = "registroParquimetroPorPlaca", key = "#dto.placa", allEntries = false)
    public RegistroParquimetro sair(ParquimetroRequestDTO dto) {
        Veiculo veiculo = findInDB(dto);
        verificaSaida(veiculo, parquimetroRepository);
        RegistroParquimetro registroParquimetro = veiculo.getRegistroParquimetroList().get(veiculo.getRegistroParquimetroList().size() - 1);
        registroParquimetro.setDataSaida(LocalDateTime.now());
        registroParquimetro.setStatus(StatusPaquimetro.ENTROU_E_SAIU);
        parquimetroRepository.save(registroParquimetro);
        calculaValorDevido(veiculo.getTipoVeiculo(), registroParquimetro);
        return registroParquimetro;
    }

    private void calculaValorDevido(TipoVeiculo tipoVeiculo, RegistroParquimetro registroParquimetro) {
        Duration duration = Duration.between(registroParquimetro.getDataEntrada(), registroParquimetro.getDataSaida());
        TipoVeiculoConfig config = TipoVeiculoConfig.retornarConfigVeiculo(tipoVeiculo);
        registroParquimetro.setValor(((1 + duration.toHours()) * config.getValorHora()) + (config.getValorMinino()));
    }

    @Cacheable(value = "veiculo", key = "#dto.placa")
    public Veiculo findOrCreateInDB(ParquimetroRequestDTO dto) {
        final Veiculo veiculo = veiculoRepository.findByPlaca(dto.placa()).orElse(new Veiculo(dto.placa(), dto.tipoVeiculo(), new ArrayList<>()));
        return veiculo;
    }

    @Cacheable(value = "veiculo", key = "#dto.placa")
    public Veiculo findInDB(ParquimetroRequestDTO dto) {
        final Veiculo veiculo = veiculoRepository.findByPlaca(dto.placa()).orElseThrow(()
                -> new NotFoundException(KeyMessages.PLACA_NOT_FOUND.getValue()));
        return veiculo;
    }

    private void verificaEntrada(Veiculo veiculo, ParquimetroRepository parquimetroRepository) {
        Optional<List<RegistroParquimetro>> registros = parquimetroRepository.findAllByVeiculoPlaca(veiculo.getPlaca());
        if (consultaValidadeRegistro(registros, StatusPaquimetro.ENTROU_E_NAO_SAIU)) {
            throw new NotFoundException(KeyMessages.VEICULO_JA_ESTA_ESTACIONADO.getValue());
        }
    }

    private void verificaSaida(Veiculo veiculo, ParquimetroRepository parquimetroRepository) {
        Optional<List<RegistroParquimetro>> registros = parquimetroRepository.findAllByVeiculoPlaca(veiculo.getPlaca());
        if (consultaValidadeRegistro(registros, StatusPaquimetro.ENTROU_E_SAIU)) {
            throw new NotFoundException(KeyMessages.VEICULO_JA_SAIU.getValue());
        }
    }

    private Boolean consultaValidadeRegistro(Optional<List<RegistroParquimetro>> registros, StatusPaquimetro statusPaquimetro) {
        if (registros.isPresent()) {
            if (registros.get().size() > 1) {
                if (registros.get().get(registros.get().size() - 1).getStatus() == statusPaquimetro) {
                    return true;
                }
            } else if (registros.get().size() == 1 && registros.get().get(0).getStatus() == statusPaquimetro) {
                return true;
            }
        }
        return false;
    }
}
