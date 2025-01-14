package org.gym.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
public class Config {
    public static final String ID_CANT_BE_NULL_OR_NEGATIVE = "%s: id can't be null or negative";
    public static final String ENTITY_CANT_BE_NULL = "%s: entity can't be null";

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
        objectMapper.setDateFormat(df);
        return objectMapper;
    }
}
