/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.ualberta.cmput301.t03.trading.serialization;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.trading.TradeState;
import ca.ualberta.cmput301.t03.trading.TradeStateAccepted;
import ca.ualberta.cmput301.t03.trading.TradeStateCancelled;
import ca.ualberta.cmput301.t03.trading.TradeStateCompleted;
import ca.ualberta.cmput301.t03.trading.TradeStateComposing;
import ca.ualberta.cmput301.t03.trading.TradeStateDeclined;
import ca.ualberta.cmput301.t03.trading.TradeStateOffered;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateDeserialization;

/**
 * class TradeStateDeserialize implements {@link JsonDeserializer<TradeState>}
 * <p>
 * This class gives Gson the ability to deserialize a TradeState object.
 *
 * @see {@link TradeStateSerializer}
 * @see <a href="https://github.com/google/gson">https://github.com/google/gson</a>
 * @see <a href="https://sites.google.com/site/gson/gson-user-guide#TOC-Custom-Serialization-and-Deserialization">Gson Custom Serialization and Deserialization</a>
 */
public class TradeStateDeserializer implements JsonDeserializer<TradeState> {

    private static TradeState fromString(String str) throws IllegalTradeStateDeserialization {
        switch (str) {
            case TradeStateCompleted.stateString:
                return new TradeStateCompleted();
            case TradeStateComposing.stateString:
                return new TradeStateComposing();
            case TradeStateCancelled.stateString:
                return new TradeStateCancelled();
            case TradeStateOffered.stateString:
                return new TradeStateOffered();
            case TradeStateAccepted.stateString:
                return new TradeStateAccepted();
            case TradeStateDeclined.stateString:
                return new TradeStateDeclined();
            default:
                throw new IllegalTradeStateDeserialization();
        }
    }

    public TradeState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            return TradeStateDeserializer.fromString(json.getAsString());
        } catch (IllegalTradeStateDeserialization illegalTradeStateDeserialization) {
            illegalTradeStateDeserialization.printStackTrace();
            return null;
        }
    }
}