package com.example.gamingservice.consumer;

import com.example.gamingservice.dto.ScoreMessage;
import com.example.gamingservice.service.ScoreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ScoreConsumerTest {

    @Mock
    private ScoreService scoreService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ScoreConsumer scoreConsumer;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsumeValidMessage() throws Exception {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0, "key", "{\"player\":\"Alice\",\"score\":100}");
        ScoreMessage scoreMessage = new ScoreMessage("Alice", 100,"1234");
        when(objectMapper.readValue(anyString(), eq(ScoreMessage.class))).thenReturn(scoreMessage);
        scoreConsumer.consume(record);
        verify(scoreService, times(1)).insertScore(scoreMessage);
    }

    @Test
    public void testConsumeInvalidMessage() throws Exception {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0, "key", "Invalid JSON");
        when(objectMapper.readValue(anyString(), eq(ScoreMessage.class))).thenThrow(JsonProcessingException.class);
        scoreConsumer.consume(record);

    }
}
