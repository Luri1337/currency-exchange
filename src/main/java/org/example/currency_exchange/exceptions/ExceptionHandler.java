package org.example.currency_exchange.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.ExceptionDto;

import java.io.IOException;

public class ExceptionHandler {
    public static void handleException(HttpServletResponse resp, int status, String message) throws IOException {
       resp.setStatus(status);
       resp.getWriter().write(new ObjectMapper().writeValueAsString(
               new ExceptionDto(message)
       ));
    }
}
