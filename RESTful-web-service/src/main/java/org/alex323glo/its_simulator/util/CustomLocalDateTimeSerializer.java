package org.alex323glo.its_simulator.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom JSON serializer for LocalDateTime objects serialization.
 *
 * @author Alexey_O
 * @version 0.1
 */
public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value       Value to serialize; can <b>not</b> be null.
     * @param gen         Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     */
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

//        Map<String, Object> chronologyMap = new LinkedHashMap<>();
//        chronologyMap.put("id", value.getChronology().getId());
//        chronologyMap.put("calendarType", value.getChronology().getCalendarType());
//
//        Map<String, Object> resultMap = new LinkedHashMap<>();
//        resultMap.put("hour", value.getHour());
//        resultMap.put("minute", value.getMinute());
//        resultMap.put("nano", value.getNano());
//        resultMap.put("second", value.getSecond());
//        resultMap.put("dayOfMonth", value.getDayOfMonth());
//        resultMap.put("dayOfWeek", value.getDayOfWeek());
//        resultMap.put("dayOfYear", value.getDayOfYear());
//        resultMap.put("month", value.getMonth());
//        resultMap.put("monthValue", value.getMonthValue());
//        resultMap.put("year", value.getYear());
//        resultMap.put("chronology", chronologyMap);
//
//        String result = new Gson().toJson(resultMap);

        //String result = "" value.toEpochSecond(ZoneOffset.UTC);

        gen.writeStartObject();
        gen.writeNumberField("year", value.getYear());
        gen.writeNumberField("month", value.getMonthValue());
        gen.writeNumberField("dayOfMonth", value.getDayOfMonth());
        gen.writeNumberField("hour", value.getMonthValue());
        gen.writeEndObject();
    }
}
