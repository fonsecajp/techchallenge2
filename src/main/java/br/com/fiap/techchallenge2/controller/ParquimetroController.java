package br.com.fiap.techchallenge2.controller;

import br.com.fiap.techchallenge2.dto.ParquimetroEntryResponseDTO;
import br.com.fiap.techchallenge2.dto.ParquimetroRequestDTO;
import br.com.fiap.techchallenge2.dto.ParquimetroResponseDTO;
import br.com.fiap.techchallenge2.entities.RegistroParquimetro;
import br.com.fiap.techchallenge2.mappers.ParquimetroMapper;
import br.com.fiap.techchallenge2.service.impl.ParquimetroServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parquimetro")
public class ParquimetroController {


    private final ParquimetroServiceImpl parquimetroServiceImpl;

    public ParquimetroController(ParquimetroServiceImpl parquimetroServiceImpl) {
        this.parquimetroServiceImpl = parquimetroServiceImpl;
    }

    @PostMapping
    public ResponseEntity<ParquimetroEntryResponseDTO> entrar(@RequestBody ParquimetroRequestDTO parquimetroRequestDTO) {
        final RegistroParquimetro registroParquimetro = parquimetroServiceImpl.entrar(parquimetroRequestDTO);
        return ResponseEntity.ok(ParquimetroMapper.entradaNoEstacionamento(registroParquimetro));
    }

    @PostMapping("/{placa}/sair")
    public ResponseEntity<ParquimetroResponseDTO> sair(@PathVariable String placa, ParquimetroRequestDTO parquimetroRequestDTO) {
        final RegistroParquimetro registroParquimetro = parquimetroServiceImpl.sair(parquimetroRequestDTO);
        return ResponseEntity.ok(ParquimetroMapper.saidaDoEstacionamento(registroParquimetro));
    }

}
