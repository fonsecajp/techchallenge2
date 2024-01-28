package br.com.fiap.techchallenge2.exception;

public enum KeyMessages {
    PLACA_NOT_FOUND("Placa não encontrada."),
    VEICULO_JA_ESTA_ESTACIONADO("Veiculo não possui saída para uma entrada cadastrada."),
    VEICULO_JA_SAIU("Veiculo não possui entrada cadastrada.");

    String value;

    KeyMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
