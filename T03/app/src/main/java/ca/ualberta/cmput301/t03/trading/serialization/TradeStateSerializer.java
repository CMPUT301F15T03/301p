package ca.ualberta.cmput301.t03.trading.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.trading.TradeState;

/**
 * class TradeStateSerialize implements {@link JsonSerializer<TradeState>}
 * <p>
 * This class gives Gson the ability to serialize a TradeState object.
 *
 * @see {@link TradeStateDeserializer}
 * @see <a href="https://github.com/google/gson">https://github.com/google/gson</a>
 * @see <a href="https://sites.google.com/site/gson/gson-user-guide#TOC-Custom-Serialization-and-Deserialization">Gson Custom Serialization and Deserialization</a>
 */
public class TradeStateSerializer implements JsonSerializer<TradeState> {
    @Override
    public JsonElement serialize(TradeState src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
