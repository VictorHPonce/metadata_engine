package com.devexup.fabric.infrastructure.external.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.devexup.fabric.domain.service.NotificationService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class TelegramClient implements NotificationService {

    // Spring busca estos nombres en application.properties automáticamente
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void sendMessage(String message) {
        try {
            // Limpiamos el mensaje para que Telegram no de error con caracteres especiales
            String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8);
            
            String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=Markdown",
                botToken, chatId, encodedMsg
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Enviamos de forma asíncrona para que el scraper no se detenga a esperar
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() != 200) {
                            System.err.println(">>> [TELEGRAM ERROR] Código: " + response.statusCode());
                        }
                    });

        } catch (Exception e) {
            System.err.println(">>> [TELEGRAM ERROR] " + e.getMessage());
        }
    }

    @Override
    public void notify(String message) {
        this.sendMessage(message); // Llamas a tu método existente
    }
}