package com.pete.bibliogere.api;

import com.pete.bibliogere.modelo.excepcoes.InvalidPesquisaException;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PesquisaUtil {

    public void handleInvalidPesquisa(String param, String message) {
        if (param.trim().isEmpty()) throw new InvalidPesquisaException(message);
    }

}
