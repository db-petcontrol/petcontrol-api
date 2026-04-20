package br.com.db.petcontrol.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Dados de resposta para erros da API")
public record ErrorResponseDTO(
    @Schema(description = "Lista de mensagens que detallha os erros ocorridos")
        List<String> mensagens) {}
