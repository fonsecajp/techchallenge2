package br.com.fiap.techchallenge2.service;

import br.com.fiap.techchallenge2.dto.ParquimetroRequestDTO;
import br.com.fiap.techchallenge2.entities.RegistroParquimetro;
import org.springframework.stereotype.Service;

@Service
public interface ParquimetroService {
    RegistroParquimetro sair(ParquimetroRequestDTO dto);

    RegistroParquimetro entrar(ParquimetroRequestDTO dto);
}
