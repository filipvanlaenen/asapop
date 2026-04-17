package net.filipvanlaenen.asapop.yaml;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * A JSON deserializer for <code>OrderedCollection&lt;String&gt;</code>.
 */
public final class OrderedStringCollectionDeserializer extends JsonDeserializer<OrderedCollection<String>>
        implements ContextualDeserializer {
    /**
     * An explicit public default constructor.
     */
    public OrderedStringCollectionDeserializer() {
    }

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext context, final BeanProperty property) {
        return new OrderedStringCollectionDeserializer();
    }

    @Override
    public OrderedCollection<String> deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext) throws IOException {
        ModifiableOrderedCollection<String> collection = ModifiableOrderedCollection.<String>empty();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            collection.add(jsonParser.getText());
        }
        return OrderedCollection.of(collection);
    }
}
