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

public class StringStringMapDeserializer extends JsonDeserializer<Map<String, String>>
        implements ContextualDeserializer {

    public StringStringMapDeserializer() {
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        return new StringStringMapDeserializer();
    }

    @Override
    public Map<String, String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
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
