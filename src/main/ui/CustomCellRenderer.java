package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

// A custom table cell renderer for displaying courses in different colors.
public class CustomCellRenderer extends DefaultTableCellRenderer {
    private static final Map<String, Color> COURSE_COLORS = new HashMap<>();
    private static final Color[] COLOR_PALETTE = {
            new Color(175, 175, 255), // purple
            new Color(162, 205, 90), // green
            new Color(255, 200, 0), // yellow
            new Color(255, 175, 175), // pink
            new Color(190, 190, 190), // purple
            new Color(139, 115, 85) // brown
    };
    private static final Set<Color> USED_COLORS = new HashSet<>();
    private static final Random random = new Random();
    private static final Color TIME_COLUMN_COLOR = new Color(224, 238, 238); // sky

    // EFFECTS: Initializes the cell renderer and sets text alignment to center.
    public CustomCellRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    // EFFECTS: Customizes table cells color
    //          - time column: light blue
    //          - Empty cells: white
    //          - Courses are assigned different color
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // time column
        if (column == 0) {
            cell.setBackground(TIME_COLUMN_COLOR);
            return cell;
        }

        // background cells
        if (value == null || value.toString().trim().isEmpty()) {
            cell.setBackground(Color.WHITE);
            return cell;
        }

        // course color
        String text = value.toString();
        String courseCode = text.split(" ")[0]; 

        if (!COURSE_COLORS.containsKey(courseCode)) {
            COURSE_COLORS.put(courseCode, getUniqueColor());
        }

        cell.setBackground(COURSE_COLORS.get(courseCode));
        return cell;
    }

    // EFFECTS: Returns a unique color that hasn't been used before.
    //          If all preset colors are used, returns a random color.
    private Color getUniqueColor() {
        for (Color color : COLOR_PALETTE) {
            if (!USED_COLORS.contains(color)) {
                USED_COLORS.add(color);
                return color;
            }
        }
        return COLOR_PALETTE[random.nextInt(COLOR_PALETTE.length)];
    }


}
