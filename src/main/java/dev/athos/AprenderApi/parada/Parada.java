package dev.athos.AprenderApi.parada;

public class Parada {

    private final int numero;       // De 1 a 10 (não muda nunca depois de criada)
    private int embarques;          // Quantos subiram nesta parada
    private int desembarques;       // Quantos desceram nesta parada
    private int passageirosABordo;  // Número interno usado para as contas

    // Construtor: cria a parada zerada no início da viagem
    public Parada(int numero) {
        this.numero = numero;
        this.embarques = 0;
        this.desembarques = 0;
        this.passageirosABordo = 0;
    }

    // --- GETTERS (O que o Spring Boot lê para montar o JSON de resposta) ---

    public int getNumero() {
        return numero;
    }

    public int getEmbarques() {
        return embarques;
    }

    public int getDesembarques() {
        return desembarques;
    }

    // O SEGREDO ESTÁ AQUI:
    // Em vez de devolver um número puro que pareça editável, devolvemos uma String informativa!
    public String getStatusLotacao() {
        if (passageirosABordo == 1) {
            return "1 passageiro a bordo";
        }
        return passageirosABordo + " passageiros a bordo";
    }

    // --- SETTERS (Usados APENAS internamente pelo nosso sistema para atualizar os valores) ---

    public void setEmbarques(int embarques) {
        this.embarques = embarques;
    }

    public void setDesembarques(int desembarques) {
        this.desembarques = desembarques;
    }

    // Este método NÃO está exposto para o usuário; só a nossa regra de negócio vai chamá-lo
    public void setPassageirosABordo(int passageirosABordo) {
        this.passageirosABordo = passageirosABordo;
    }

    // Método auxiliar interno para a nossa lógica de cálculo ler o número puro
    int calcularSaldoInterno() {
        return passageirosABordo;
    }
}