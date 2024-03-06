package br.com.fiap.techchallenge2.service;

import br.com.fiap.techchallenge2.dto.ParquimetroRequestDTO;
import br.com.fiap.techchallenge2.dto.ParquimetroResponseDTO;
import br.com.fiap.techchallenge2.entities.RegistroParquimetro;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParquimetroService {
     RegistroParquimetro sair(ParquimetroRequestDTO dto);
     RegistroParquimetro entrar(ParquimetroRequestDTO dto);

    List<RegistroParquimetro> getByPlaca(String placa);
}
