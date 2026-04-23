import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
public class ColorfulCell extends ListCell<String>{
    private Color color;
    public ColorfulCell(Color c) {
        color = c;
    }
    @Override 
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) setText(item);
        else setText("");
        setTextFill(Color.WHITE);
        setBackground(new Background(
            new BackgroundFill(isSelected() ? color.brighter() : color, CornerRadii.EMPTY, Insets.EMPTY))
        );
        
    }    
}
