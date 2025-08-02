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

public class IntegerStringMapDeserializer extends JsonDeserializer<Map<Integer, String>>
        implements ContextualDeserializer {

    public IntegerStringMapDeserializer() {
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        return new IntegerStringMapDeserializer();
    }

    @Override
    public Map<Integer, String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
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
