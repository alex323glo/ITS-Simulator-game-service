package org.alex323glo.its_simulator.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
        gen.writeStartObject();
        gen.writeNumberField("year", value.getYear());
        gen.writeNumberField("month", value.getMonthValue());
        gen.writeNumberField("dayOfMonth", value.getDayOfMonth());
        gen.writeNumberField("hour", value.getHour());
        gen.writeNumberField("minute", value.getMinute());
        gen.writeNumberField("second", value.getSecond());
        gen.writeNumberField("dayOfYear", value.getDayOfYear());
        gen.writeNumberField("zoneOffsetSeconds", ZonedDateTime.now().getOffset().getTotalSeconds());
        gen.writeEndObject();
    }
}
