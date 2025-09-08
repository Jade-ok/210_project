package persistence;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.json.JSONObject;

import model.TimeTable;

// CITATION: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// This class is based on the JSON class from the demo application above.
// Represents a writer that writes JSON representation of TimeTable to file
public class JsonWriter {
    private PrintWriter writer;
    private String destination;

    // EEFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: open writer. throw FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of TimeTable to file
    public void write(TimeTable tt) {
        JSONObject json = tt.toJson();
        saveToFile(json.toString(4));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

}
