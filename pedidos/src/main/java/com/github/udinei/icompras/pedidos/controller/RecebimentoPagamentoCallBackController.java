package com.github.udinei.icompras.pedidos.controller;

import com.github.udinei.icompras.pedidos.dto.RecebimentoCallBackPagamentoDTO;
import com.github.udinei.icompras.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/callback-pagamentos")
@RequiredArgsConstructor
public class RecebimentoPagamentoCallBackController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Object> atualizarStatusPagamento(
        @RequestBody RecebimentoCallBackPagamentoDTO body,
        @RequestHeader(required = true, name = "apiKey") String apiKey) {
            pedidoService.atualizarStatusPagamento(
                    body.codigo(),
                    body.chavePagamento(),
                    body.status(),
                    body.observacoes()
            );

            return ResponseEntity.ok().build();

        }


    }


