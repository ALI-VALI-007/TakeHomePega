package com.read.reading.Converter;

import com.read.reading.model.UserBook.ReadStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ReadStatusConverter implements AttributeConverter<ReadStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ReadStatus status) {
        return status == ReadStatus.READ ? 1 : 0;
    }

    @Override
    public ReadStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return ReadStatus.UNREAD;
        return dbData==1 ? ReadStatus.READ : ReadStatus.UNREAD;
    }
}
