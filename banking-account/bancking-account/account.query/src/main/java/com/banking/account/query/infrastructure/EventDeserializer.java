package com.banking.account.query.infrastructure;

import com.banking.account.common.events.AccountOpenedEvent; // Importa tus eventos aquí
import com.banking.account.common.events.FundsDepositedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.HashMap;
import java.util.Map;

public class EventDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Class<?>> eventTypeMapping = new HashMap<>();

    public EventDeserializer() {
        // Registra los tipos de eventos que el deserializador debe manejar
        eventTypeMapping.put("AccountOpenedEvent", AccountOpenedEvent.class);
        eventTypeMapping.put("FundsDepositedEvent", FundsDepositedEvent.class);
        eventTypeMapping.put("FundsWithdrawnEvent", FundsDepositedEvent.class);
        // Agrega más tipos si tienes otros eventos
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            if (data == null || data.length == 0) {
                return null;
            }

            // Determinar el tipo de evento basado en el nombre del tópico
            Class<?> targetType = eventTypeMapping.get(topic);
            if (targetType == null) {
                throw new SerializationException("No se encontró un tipo de evento para el tópico: " + topic);
            }

            // Convertir los bytes a un objeto del tipo determinado
            return (T) objectMapper.readValue(data, targetType);
        } catch (Exception e) {
            throw new SerializationException("Error deserializando mensaje para el tópico: " + topic, e);
        }
    }

    @Override
    public void close() {
        // Cleanup si es necesario
    }
}
