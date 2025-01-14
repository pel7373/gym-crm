package org.gym.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.gym.entity.Identifiable;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class RestoreBackupDataFromToFile<T extends Identifiable> {
    private final ObjectMapper objectMapper;

    @Autowired
    public RestoreBackupDataFromToFile(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Trainer> restoreTrainers(String file) throws IOException {
        return objectMapper.readValue(new File(file), new TypeReference<>(){});
    }

    public List<Trainee> restoreTrainees(String file) throws IOException {
        return objectMapper.readValue(new File(file), new TypeReference<>(){});
    }

    public List<Training> restoreTrainings(String file) throws IOException {
        return objectMapper.readValue(new File(file), new TypeReference<>(){});
    }

    public long calculateNextId(List<T> list) {
        return list.stream()
                .mapToLong(T::getId)
                .max().getAsLong()
                + 1;
    }

    public void backupToFile(String file, List<T> list) {
        try {
            objectMapper.writeValue(new File(file), list);
            LOGGER.info("backupToFile saved data from storage to file {}", file);
        } catch (IOException e) {
            LOGGER.warn("backupToFile couldn't write or convert data to file {}", file);
        }
    }
}
