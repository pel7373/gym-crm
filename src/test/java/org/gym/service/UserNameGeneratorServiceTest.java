package org.gym.service;

import org.gym.dao.TraineeDao;
import org.gym.dao.TrainerDao;
import org.gym.exception.NullEntityException;
import org.gym.service.impl.UserNameGeneratorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserNameGeneratorServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private UserNameGeneratorServiceImpl userNameGeneratorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userNameGeneratorService
                = new UserNameGeneratorServiceImpl(trainerDao, traineeDao);
    }

    @Test
    void generateUserNameWithoutSuffix() {
        when(traineeDao.isUserNameExists("Maria.Ivanenko")).thenReturn(false);
        when(trainerDao.isUserNameExists("Maria.Ivanenko")).thenReturn(false);

        String firstName = "Maria";
        String lastName = "Ivanenko";
        String expectedResult = "Maria.Ivanenko";
        String actualResult = userNameGeneratorService.generate(firstName, lastName);

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void generateUserNameWithSuffix() {
        when(traineeDao.isUserNameExists("John.Doe")).thenReturn(false);
        when(trainerDao.isUserNameExists("John.Doe")).thenReturn(true);

        when(traineeDao.isUserNameExists("John.Doe0")).thenReturn(false);
        when(trainerDao.isUserNameExists("John.Doe0")).thenReturn(false);

        String firstName = "John";
        String lastName = "Doe";
        String expectedResult = "John.Doe0";
        String actualResult = userNameGeneratorService.generate(firstName, lastName);

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void generateUserNameIfFirstNameNull() {
        String firstName = null;
        String lastName = "Doe";

        assertThrows(NullEntityException.class, () -> userNameGeneratorService.generate(firstName, lastName),
                "generate: firstName or/and lastName can't be null, blank or empty");
        verify(trainerDao, never()).isUserNameExists(any(String.class));
        verify(traineeDao, never()).isUserNameExists(any(String.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void generateUserNameIfFirstNameEmptyOrBlank(String firstName) {
        String lastName = "Doe";

        assertThrows(NullEntityException.class, () -> userNameGeneratorService.generate(firstName, lastName),
                "generate: firstName or/and lastName can't be null, blank or empty");
        verify(trainerDao, never()).isUserNameExists(any(String.class));
        verify(traineeDao, never()).isUserNameExists(any(String.class));
    }

    @Test
    void generateUserNameIfLastNameNull() {
        String firstName = "John";
        String lastName = null;

        assertThrows(NullEntityException.class, () -> userNameGeneratorService.generate(firstName, lastName),
                "generate: firstName or/and lastName can't be null, blank or empty");
        verify(trainerDao, never()).isUserNameExists(any(String.class));
        verify(traineeDao, never()).isUserNameExists(any(String.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void generateUserNameIfLastNameEmptyOrBlank(String lastName) {
        String firstName = "John";

        assertThrows(NullEntityException.class, () -> userNameGeneratorService.generate(firstName, lastName),
                "generate: firstName or/and lastName can't be null, blank or empty");
        verify(trainerDao, never()).isUserNameExists(any(String.class));
        verify(traineeDao, never()).isUserNameExists(any(String.class));
    }
}
