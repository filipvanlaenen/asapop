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
 * Unit tests on the <code>StringToStringMapDeserializer</code> class.
 */
public class StringToStringMapDeserializerTest {
    /**
     * Verifies that a JSON object with a string to string map is deserialized correctly.
     *
     * @throws JsonProcessingException Thrown if there's a JSON processing exception.
     * @throws JsonMappingException    Thrown if there's a JSON mapping exception.
     */
    @Test
    void shouldDeserializeIntegerToStringMap() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(Include.NON_NULL);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Map.class, new StringToStringMapDeserializer());
        mapper.registerModule(module);
        Map<String, String> result = mapper.readValue("F: \"Foo\"\nB: \"Bar\"", Map.class);
        assertTrue(Map.<String, String>of("F", "Foo", "B", "Bar").containsSame(result));
    }
}
