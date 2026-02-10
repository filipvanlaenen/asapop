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
 * A JSON deserializer for <code>Map&lt;String, String&gt;</code>.
 */
public final class StringToStringMapDeserializer extends JsonDeserializer<Map<String, String>>
        implements ContextualDeserializer {
    /**
     * An explicit public default constructor.
     */
    public StringToStringMapDeserializer() {
    }

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext context, final BeanProperty property) {
        return new StringToStringMapDeserializer();
    }

    @Override
    public Map<String, String> deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext) throws IOException {
        ModifiableMap<String, String> map = ModifiableMap.<String, String>empty();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Iterator<String> i = node.fieldNames();
        while (i.hasNext()) {
            String key = i.next();
            String value = node.get(key).asText();
            map.put(key, value);
        }
        return map;
    }
}
