package persistence;

import org.json.JSONObject;

// Represents an interface for objects that can be converted to a JSON representation
public interface Writable {
     // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
