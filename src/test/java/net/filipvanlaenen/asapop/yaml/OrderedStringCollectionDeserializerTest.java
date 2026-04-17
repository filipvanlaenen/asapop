package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the <code>OrderedStringCollectionDeserializer</code> class.
 */
public class OrderedStringCollectionDeserializerTest {
    /**
     * Verifies that a JSON object with a string array is deserialized correctly.
     *
     * @throws JsonProcessingException Thrown if there's a JSON processing exception.
     * @throws JsonMappingException    Thrown if there's a JSON mapping exception.
     */
    @Test
    void shouldDeserializeStringArray() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OrderedCollection.class, new OrderedStringCollectionDeserializer());
        mapper.registerModule(module);
        OrderedCollection<String> result = mapper.readValue("[\"Foo\", \"Bar\"]", OrderedCollection.class);
        assertTrue(OrderedCollection.of("Foo", "Bar").containsSame(result));
    }
}
