package net.filipvanlaenen.asapop.yaml;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * A JSON deserializer for <code>Map&lt;Integer, String&gt;</code>.
 */
public final class IntegerToStringMapDeserializer extends JsonDeserializer<Map<Integer, String>>
        implements ContextualDeserializer {
    /**
     * An explicit public default constructor.
     */
    public IntegerToStringMapDeserializer() {
    }

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext context, final BeanProperty property) {
        return new IntegerToStringMapDeserializer();
    }

    @Override
    public Map<Integer, String> deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext) throws IOException {
        ModifiableMap<Integer, String> map = ModifiableMap.<Integer, String>empty();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Iterator<String> i = node.fieldNames();
        while (i.hasNext()) {
            String fieldName = i.next();
            Integer key = Integer.parseInt(fieldName);
            String value = node.get(fieldName).asText();
            map.put(key, value);
        }
        return map;
    }
}
