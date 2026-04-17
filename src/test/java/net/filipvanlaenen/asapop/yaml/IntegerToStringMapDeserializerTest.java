package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import net.filipvanlaenen.kolektoj.Map;

/**
 * Unit tests on the <code>IntegerToStringMapDeserializer</code> class.
 */
public class IntegerToStringMapDeserializerTest {
    /**
     * Verifies that a JSON object with an integer to string map is deserialized correctly.
     *
     * @throws JsonProcessingException Thrown if there's a JSON processing exception.
     * @throws JsonMappingException    Thrown if there's a JSON mapping exception.
     */
    @Test
    void shouldDeserializeIntegerToStringMap() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(Include.NON_NULL);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Map.class, new IntegerToStringMapDeserializer());
        mapper.registerModule(module);
        Map<Integer, String> result = mapper.readValue("1: \"Foo\"\n2: \"Bar\"", Map.class);
        assertTrue(Map.<Integer, String>of(1, "Foo", 2, "Bar").containsSame(result));
    }
}
