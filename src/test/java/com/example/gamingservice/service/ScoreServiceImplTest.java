package com.example.gamingservice.service;

import com.example.gamingservice.dao.PlayerScoresDao;
import com.example.gamingservice.dto.PlayerScore;
import com.example.gamingservice.dto.ScoreMessage;
import com.example.gamingservice.service.impl.ScoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
class ScoreServiceImplTest {

    @Mock
    private PlayerScoresDao playerScoresDao;

    @InjectMocks
    private ScoreServiceImpl scoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTopPlayers() throws Exception {
        List<PlayerScore> expectedPlayers = Arrays.asList(
                new PlayerScore("Player1", 100),
                new PlayerScore("Player2", 90),
                new PlayerScore("Player3", 80)
        );

        when(playerScoresDao.getTopPlayers(anyInt())).thenReturn(expectedPlayers);
        List<PlayerScore> actualPlayers = scoreService.getTopPlayers();
        assertEquals(expectedPlayers, actualPlayers);
    }

    @Test
    void testGetTopPlayersWithEmptyList() throws Exception {
        when(playerScoresDao.getTopPlayers(anyInt())).thenReturn(Collections.emptyList());
        List<PlayerScore> actualPlayers = scoreService.getTopPlayers();
        assertEquals(0, actualPlayers.size());
    }

    @Test
    void testInsertScore() throws Exception {
        ScoreMessage scoreMessage = new ScoreMessage("Player1", 100, "1234");
        scoreService.insertScore(scoreMessage);
        verify(playerScoresDao, times(1)).insertScore(scoreMessage);
    }

    @Test
    public void testInsertScore_ValidScoreMessage() {
        ScoreMessage scoreMessage = new ScoreMessage("playerName", 100, "1234");
        scoreService.insertScore(scoreMessage);

        // Verify that insertScore was called with the correct argument
        verify(playerScoresDao).insertScore(scoreMessage);
    }

    @Test
    public void testInsertScore_NullScoreMessage() {
        ScoreMessage scoreMessage = null;

        scoreService.insertScore(scoreMessage);

        // Verify that insertScore method was not called since scoreMessage is null
        verify(playerScoresDao, never()).insertScore(any());
    }

    @Test
    public void testInsertScore_InvalidScoreMessage() {
        ScoreMessage scoreMessage = new ScoreMessage("", 100, "1234");

        scoreService.insertScore(scoreMessage);
        verify(playerScoresDao, never()).insertScore(any());
    }

    @Test
    public void testInsertScore_DataAccessException() {
        ScoreMessage scoreMessage = new ScoreMessage("playerName", 100, "1234");
        doThrow(new DataAccessException("Mocked exception") {
        }).when(playerScoresDao).insertScore(scoreMessage);
        scoreService.insertScore(scoreMessage);

    }

}

