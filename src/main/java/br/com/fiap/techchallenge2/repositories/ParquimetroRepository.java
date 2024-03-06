package br.com.fiap.techchallenge2.repositories;

import br.com.fiap.techchallenge2.entities.RegistroParquimetro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParquimetroRepository extends JpaRepository<RegistroParquimetro, UUID> {
    Optional<List<RegistroParquimetro>> findAllByVeiculoPlaca(String placa);
}
