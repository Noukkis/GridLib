package GridLib;

import java.util.ArrayList;
import javafx.scene.Node;
import GridLib.Grid.Cell;
import GridLib.Grid.Cell.CellListener;
import javafx.scene.layout.GridPane;

/**
 *
 * A {@link Grid} that can only contains {@link javafx.scene.Node} elements or
 * but can be bound to a {@link javafx.scene.layout.GridPane} to modify it
 * dynamically.
 *
 * @author Jordan Vésy
 * @since 1.0
 * @param <E> the class that this grid can contains
 * @see javafx.scene.Node
 * @see javafx.scene.layout.GridPane
 */
public class NodeGrid<E extends Node> extends Grid<E> {

    private final ArrayList<GridPane> boundPanes;

    /**
     * Construct a NodeGrid with the choosen height and width and fill the cells
     * with nulls.
     *
     * @param height the height of this grid
     * @param width the width of this grid
     */
    public NodeGrid(int height, int width) {
        super(height, width);
        boundPanes = new ArrayList<>();
    }

    /**
     * Construct a NodeGrid with the choosen height and width and fill the cells
     * with the provided generator.
     *
     * @param height the height of this grid
     * @param width the width of this grid
     * @param generator a tool that is use to generate the content of each cell
     */
    public NodeGrid(int height, int width, CellValueGenerator<E> generator) {
        super(height, width, generator);
        boundPanes = new ArrayList<>();
        for (Cell cell : getCells()) {
            cell.addCellListener((CellListener<E>) (Cell<E> c, E oldValue, E newValue) -> {
                for (GridPane gridPane : boundPanes) {
                    gridPane.getChildren().remove(oldValue);
                    gridPane.add(newValue, c.getColumn(), c.getRow());
                }
            });
        }
    }

    /**
     * Bind the choosen {@link javafx.scene.layout.GridPane} to this Grid.
     *
     * @param p the GridPane to bind
     */
    public void bind(GridPane p) {
        for (Cell<E> cell : getCells()) {
            p.add(cell.get(), cell.getColumn(), cell.getRow());
        }
        boundPanes.add(p);
    }

    /**
     * Unbind the choosen {@link javafx.scene.layout.GridPane} to this Grid.
     *
     * @param p the GridPane to unbind
     * 
     * @return true if the GridPane was bound, false otherwise
     */
    public boolean unbind(GridPane p) {
        return boundPanes.remove(p);
    }
}
