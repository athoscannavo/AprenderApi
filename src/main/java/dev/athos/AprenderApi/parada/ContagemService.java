package dev.athos.AprenderApi.parada;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContagemService {

    // Lista que guarda as 10 paradas na memória enquanto a API estiver rodando
    private final List<Parada> paradas = new ArrayList<>();

    // Construtor: executado uma única vez quando o Spring Boot inicia
    public ContagemService() {
        for (int i = 1; i <= 10; i++) {
            paradas.add(new Parada(i));
        }
    }

    // 1. Devolve a lista completa das 10 paradas
    public List<Parada> listarTodas() {
        return paradas;
    }

    // 2. Busca apenas uma parada específica (de 1 a 10)
    public Parada buscarPorNumero(int numero) {
        validarNumeroParada(numero);
        // Como a lista começa no índice 0, a Parada 1 está na posição 0 (numero - 1)
        return paradas.get(numero - 1);
    }

    // 3. Atualiza embarques e desembarques de uma parada e recalcula o trajeto inteiro
    public Parada registrarMovimentacao(int numero, MovimentacaoRequest dados) {
        validarNumeroParada(numero);

        if (dados.embarques() < 0 || dados.desembarques() < 0) {
            throw new IllegalArgumentException("Embarques e desembarques não podem ser negativos.");
        }

        // Guarda os valores antigos caso o novo cálculo dê erro (ex: saldo negativo)
        Parada paradaAtual = paradas.get(numero - 1);
        int embarquesAntigos = paradaAtual.getEmbarques();
        int desembarquesAntigos = paradaAtual.getDesembarques();

        // Aplica os novos valores
        paradaAtual.setEmbarques(dados.embarques());
        paradaAtual.setDesembarques(dados.desembarques());

        try {
            recalcularTrajeto();
        } catch (IllegalArgumentException erro) {
            // Se a conta ficar negativa em alguma parada seguinte, desfaz a alteração!
            paradaAtual.setEmbarques(embarquesAntigos);
            paradaAtual.setDesembarques(desembarquesAntigos);
            throw erro;
        }

        return paradaAtual;
    }

    // Percorre da Parada 1 até a 10 somando quem entra e subtraindo quem sai
    private void recalcularTrajeto() {
        int totalAcumulado = 0;

        for (Parada p : paradas) {
            int saldoNestaParada = totalAcumulado + p.getEmbarques() - p.getDesembarques();

            if (saldoNestaParada < 0) {
                throw new IllegalArgumentException(
                        "Erro na Parada " + p.getNumero() + ": não podem desembarcar mais passageiros do que existem a bordo."
                );
            }

            totalAcumulado = saldoNestaParada;
            p.setPassageirosABordo(totalAcumulado);
        }
    }

    // Garante que o usuário não peça a Parada 0, -1 ou 11
    private void validarNumeroParada(int numero) {
        if (numero < 1 || numero > 10) {
            throw new IllegalArgumentException("Número de parada inválido. Escolha entre 1 e 10.");
        }
    }
}