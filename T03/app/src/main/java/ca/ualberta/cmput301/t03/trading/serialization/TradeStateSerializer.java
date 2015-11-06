package ca.ualberta.cmput301.t03.trading.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.trading.TradeState;

public class TradeStateSerializer implements JsonSerializer<TradeState> {
    @Override
    public JsonElement serialize(TradeState src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
