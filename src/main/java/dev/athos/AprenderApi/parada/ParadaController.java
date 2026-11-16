package dev.athos.AprenderApi.parada;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paradas")
public class ParadaController {

    private final ContagemService contagemService;

    // O Spring Boot injeta o ContagemService automaticamente aqui
    public ParadaController(ContagemService contagemService) {
        this.contagemService = contagemService;
    }

    // GET http://localhost:8080/api/paradas
    // Lista todas as 10 paradas e o status de lotação de cada uma
    @GetMapping
    public ResponseEntity<List<Parada>> listarTodasAsParadas() {
        List<Parada> lista = contagemService.listarTodas();
        return ResponseEntity.ok(lista);
    }

    // GET http://localhost:8080/api/paradas/1 (ou qualquer número de 1 a 10)
    // Mostra os dados de uma parada específica
    @GetMapping("/{numero}")
    public ResponseEntity<?> buscarParada(@PathVariable int numero) {
        try {
            Parada parada = contagemService.buscarPorNumero(numero);
            return ResponseEntity.ok(parada);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.badRequest().body(erro.getMessage());
        }
    }

    // PUT http://localhost:8080/api/paradas/1/movimentacao
    // Recebe o JSON com embarques e desembarques e devolve a parada atualizada
    @PutMapping("/{numero}/movimentacao")
    public ResponseEntity<?> registrarMovimentacao(
            @PathVariable int numero,
            @RequestBody MovimentacaoRequest request) {
        try {
            Parada paradaAtualizada = contagemService.registrarMovimentacao(numero, request);
            return ResponseEntity.ok(paradaAtualizada);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.badRequest().body(erro.getMessage());
        }
    }
}