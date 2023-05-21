package com.github.goodfatcat.mangatelegrambot.repository.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ReadingStatusConverter implements AttributeConverter<ReadingStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ReadingStatus attribute) {
        return attribute.getStatus();
    }

    @Override
    public ReadingStatus convertToEntityAttribute(Integer dbData) {
        return ReadingStatus.findByStatusCode(dbData);
    }
}
