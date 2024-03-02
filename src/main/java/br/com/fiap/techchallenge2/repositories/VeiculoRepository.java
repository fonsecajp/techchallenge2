package br.com.fiap.techchallenge2.repositories;

import br.com.fiap.techchallenge2.entities.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, String> {
    Optional<Veiculo> findByPlaca(String placa);
}
