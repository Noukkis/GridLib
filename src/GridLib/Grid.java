package GridLib;

import com.sun.javafx.UnmodifiableArrayList;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Bidimensional non-resizable array with methods to get the row, column,
 * adjacents cells etc. This class use a nested "Cell" class containing the
 * desired object. These cells contains informations about their location in the
 * grid.
 *
 * @author Jordan Vésy
 * @since 1.0
 * @see ArrayList
 * @param <E> the class that this grid can contains
 */
public class Grid<E> {

    private final UnmodifiableArrayList<Cell<E>> cells;
    private final int height;
    private final int width;

    /**
     * the emplacement of the cell above the specified one in the returned
     * ArrayList of the {@link #getAdjacents(GridLib.Grid.Cell, boolean)}
     * and {@link #getAdjacents(int, int, boolean)} methods
     */
    public final static int TOP = 0;

    /**
     * the emplacement of the cell to the right of the specified one in the
     * returned ArrayList of the
     * {@link #getAdjacents(GridLib.Grid.Cell, boolean)} and
     * {@link #getAdjacents(int, int, boolean)} methods
     */
    public final static int RIGHT = 1;

    /**
     * the emplacement of the cell below the specified one in the returned
     * ArrayList of the {@link #getAdjacents(GridLib.Grid.Cell, boolean)}
     * and {@link #getAdjacents(int, int, boolean)} methods
     */
    public final static int BOTTOM = 2;

    /**
     * the emplacement of the cell to the left of the specified one in the
     * returned ArrayList of the
     * {@link #getAdjacents(GridLib.Grid.Cell, boolean)} and
     * {@link #getAdjacents(int, int, boolean)} methods
     */
    public final static int LEFT = 3;

    /**
     * the emplacement of the cell on the top-left corner of the specified one in
     * the returned ArrayList of the
     * {@link #getDiagonalsAdjacents(GridLib.Grid.Cell, boolean)} and
     * {@link #getDiagonalsAdjacents(int, int, boolean)} methods
     */
    public final static int TOP_LEFT = 0;

    /**
     * the emplacement of the cell on the top-left corner of the specified one in
     * the returned ArrayList of the
     * {@link #getDiagonalsAdjacents(GridLib.Grid.Cell, boolean)} and
     * {@link #getDiagonalsAdjacents(int, int, boolean)} methods
     */
    public final static int TOP_RIGHT = 1;

    /**
     * the emplacement of the cell on the top-left corner of the specified one in
     * the returned ArrayList of the
     * {@link #getDiagonalsAdjacents(GridLib.Grid.Cell, boolean)} and
     * {@link #getDiagonalsAdjacents(int, int, boolean)} methods
     */
    public final static int BOTTOM_RIGHT = 2;

    /**
     * the emplacement of the cell on the top-left corner of the specified one in
     * the returned ArrayList of the
     * {@link #getDiagonalsAdjacents(GridLib.Grid.Cell, boolean)} and
     * {@link #getDiagonalsAdjacents(int, int, boolean)} methods
     */
    public final static int BOTTOM_LEFT = 3;

    /**
     * Construct a Grid with the specified height and width and fill the cells
     * with the provided generator.
     *
     * @param height the height of this grid
     * @param width the width of this grid
     * @param generator a tool that is use to generate the content of each cell
     */
    public Grid(int height, int width, CellValueGenerator<E> generator) {

        Cell<E>[] temp = (Cell<E>[]) new Cell[height * width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                temp[row * width + column] = new Cell<E>(row, column, generator.generate(row, column));
            }
        }
        cells = new UnmodifiableArrayList<>(temp, height * width);
        this.height = height;
        this.width = width;
    }

    /**
     * Construct a Grid with the specified height and width and fill the cells
     * with the provided item.
     *
     * @param initial the initial value of each cell
     * @param height the height of this grid
     * @param width the width of this grid
     */
    public Grid(E initial, int height, int width) {
        Cell<E>[] temp = (Cell<E>[]) new Cell[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                temp[i * width + j] = new Cell<E>(i, j, initial);
            }
        }
        cells = new UnmodifiableArrayList<>(temp, height * width);
        this.height = height;
        this.width = width;
    }

    /**
     * Construct a Grid with the specified height and width and fill the cells
     * with nulls.
     *
     * @param height the height of this grid
     * @param width the width of this grid
     */
    public Grid(int height, int width) {
        this(null, height, width);
    }

    /**
     * Reset and generate the value of each cell with the provided generator.
     *
     * @param generator a tool that is use to generate the content of each cell
     */
    public void regenerateCells(CellValueGenerator<E> generator) {
        for (Cell<E> cell : cells) {
            cell.set(generator.generate(cell.row, cell.column));
        }
    }

    /**
     * Put an item in the specified row and column.
     *
     * @param item the item to put
     * @param row the row where the item is put
     * @param column the column where the item is put
     * @return true if the provided row and column match the height and width of
     * this Grid, false otherwise
     */
    public boolean put(E item, int row, int column) {
        if (row >= 0 && row < height && column >= 0 && column < width) {
            cells.get(width * row + column).set(item);
            return true;
        }
        return false;
    }

    /**
     * Replace the item in the specified row and column by the provided one. Does
     * nothing if there is no precedent item
     *
     * @param item the item to put
     * @param row the row where the item is replaced
     * @param column the column where the item is replaced
     * @return true if there was a precedent item in the specified emplacement,
     * false otherwise
     */
    public boolean replace(E item, int row, int column) {
        int index = width * row + column;
        if (index < cells.size() && !cells.get(index).isEmpty()) {
            return put(item, row, column);
        }
        return false;
    }

    /**
     * Remove the item in the specified row and column.
     *
     * @param row the row where the item is deleted
     * @param column the column where the item is deleted
     * @return true if there was an item to delete in the specified emplacement,
     * false otherwise
     */
    public boolean remove(int row, int column) {
        Cell c = cells.get(width * row + column);
        if (c.isEmpty()) {
            c.clear();
            return true;
        }
        return false;
    }

    /**
     * Get the cell in the specified row and column.
     *
     * @param row the specified row
     * @param column the specified column
     * @return the cell specified cell
     * @throws IndexOutOfBoundsException if the row and/or column are out of
     * range
     */
    public Cell<E> get(int row, int column) {
        if (row < 0 || column < 0 || row >= width() || column >= height()) {
            throw new IndexOutOfBoundsException();
        }
        return cells.get(width * row + column);
    }

    /**
     * get the adjacents cells of the specified one.
     *
     * @param row the row of the specified cell
     * @param column the column of the specified cell
     * @param deleteNulls if set to true, delete the non-existant cells (when
     * the specified cell is in a border or a corner)
     *
     * @return a list containing the adjacents cells in the order defined by the
     * {@link #TOP}, {@link #RIGHT}, {@link #BOTTOM} and {@link #LEFT}
     * constants. Beware that these cosntants become obsoletes if
     * <code>deleteNulls</code> is true
     * @throws IndexOutOfBoundsException if the row and/or column are out of
     * range
     */
    public ArrayList<Cell<E>> getAdjacents(int row, int column, boolean deleteNulls) {
        if (row < 0 || column < 0 || row >= width() || column >= height()) {
            throw new IndexOutOfBoundsException();
        }
        ArrayList<Cell<E>> res = new ArrayList<>();
        res.add((row > 0) ? get(row - 1, column) : null); //add up cell
        res.add((column < width - 1) ? get(row, column + 1) : null); //add right cell
        res.add((row < height - 1) ? get(row + 1, column) : null); //add down cell
        res.add((column > 0) ? get(row, column - 1) : null); //add left cell

        if (deleteNulls) {
            res.removeIf(Objects::isNull);
        }
        return res;
    }

    /**
     * get the diagonal-adjacents cells of the specified one.
     *
     * @param row the row of the specified cell
     * @param column the column of the specified cell
     * @param deleteNulls if set to true, delete the non-existant cells (when
     * the specified cell is in a border or a corner)
     *
     * @return a list containing the diagonal-adjacents cells in the order
     * defined by the
     * {@link #TOP_LEFT}, {@link #TOP_RIGHT}, {@link #BOTTOM_RIGHT} and
     * {@link #BOTTOM_LEFT} constants. Beware that these cosntants become
     * obsoletes if <code>deleteNulls</code> is true
     * @throws IndexOutOfBoundsException if the row and/or column are out of
     * range
     */
    public ArrayList<Cell<E>> getDiagonalsAdjacents(int row, int column, boolean deleteNulls) {
        if (row < 0 || column < 0 || row >= width() || column >= height()) {
            throw new IndexOutOfBoundsException();
        }
        ArrayList<Cell<E>> res = new ArrayList<>();
        res.add((row > 0 && column > 0) ? get(row - 1, column - 1) : null); //add up-left cell
        res.add((row > 0 && column < width - 1) ? get(row - 1, column + 1) : null); //add up-right cell
        res.add((row < height - 1 && column < width - 1) ? get(row + 1, column + 1) : null); //add down-right cell
        res.add((row < height - 1 && column > 0) ? get(row + 1, column - 1) : null); //add down-left cell

        if (deleteNulls) {
            res.removeIf(Objects::isNull);
        }
        return res;
    }

    /**
     * Get the specified row of cells.
     *
     * @param row the specified row
     * @return a list containing the cells of the specified row from right to left
     * @throws IndexOutOfBoundsException if the row is out of range
     */
    public ArrayList<Cell<E>> getRow(int row) {
        if (row < 0 || row >= width()) {
            throw new IndexOutOfBoundsException();
        }
        ArrayList<Cell<E>> res = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            res.add(get(row, i));
        }
        return res;
    }

    /**
     * Get the specified column of cells.
     *
     * @param column the specified column
     * @return a list containing the cells of the specified column from top to
     * bottom
     * @throws IndexOutOfBoundsException if the column is out of range
     */
    public ArrayList<Cell<E>> getColumn(int column) {
        if (column < 0 || column >= height()) {
            throw new IndexOutOfBoundsException();
        }
        ArrayList<Cell<E>> res = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            res.add(get(i, column));
        }
        return res;
    }

    /**
     * Get the specified descending diagonal of cells
     *
     * @param row the specified row
     * @param column the specified column
     * @return a list containing the cells of the specified descending diagonal
     * from the top-left cell to the bottom-right one
     * @throws IndexOutOfBoundsException if the row and/or column are out of
     * range
     */
    public ArrayList<Cell<E>> getDiagonalDesc(int row, int column) {
        if (row < 0 || column < 0 || row >= width() || column >= height()) {
            throw new IndexOutOfBoundsException();
        }
        ArrayList<Cell<E>> res = new ArrayList<>();
        int rowStart = (column > row) ? 0 : row - column;
        int colStart = (row > column) ? 0 : column - row;
        int i = width * rowStart + colStart;
        do {
            res.add(cells.get(i));
            i += width + 1;
        } while (i < cells.size() && i % width != 0);
        return res;
    }

    /**
     * Get the specified ascending diagonal of cells
     *
     * @param row the specified row
     * @param column the specified column
     * @return a list containing the cells of the specified ascending diagonal
     * from the bottom-left cell to the top-right one
     * @throws IndexOutOfBoundsException if the row and/or column are out of
     * range
     */
    public ArrayList<Cell<E>> getDiagonalAsc(int row, int column) {
        if (row < 0 || column < 0 || row >= width() || column >= height()) {
            throw new IndexOutOfBoundsException();
        }
        ArrayList<Cell<E>> res = new ArrayList<>();
        int r = height - row - 1;
        int rowStart = (column > r) ? height - 1 : height - 1 - (r - column);
        int colStart = (r > column) ? 0 : column - r;
        int i = width * rowStart + colStart;
        do {
            res.add(cells.get(i));
            i -= width - 1;
        } while (i >= 0 && i % width != 0);
        return res;
    }

    /**
     * get the adjacents cells of the specified one.
     *
     * @param cell the specified cell
     * @param deleteNulls if set to true, delete the non-existant cells (when
     * the specified cell is in a border or a corner)
     *
     * @return a list containing the adjacents cells in the order defined by the
     * {@link #TOP}, {@link #RIGHT}, {@link #BOTTOM} and {@link #LEFT}
     * constants. Beware that these cosntants become obsoletes if
     * <code>deleteNulls</code> is true
     * @throws IndexOutOfBoundsException if the row and/or column of the given
     * cell are out of range
     */
    public ArrayList<Cell<E>> getAdjacents(Cell<E> cell, boolean deleteNulls) {
        return getAdjacents(cell.row, cell.column, deleteNulls);
    }

    /**
     * get the diagonal-adjacents cells of the specified one.
     *
     * @param cell the specified cell
     * @param deleteNulls if set to true, delete the non-existant cells (when
     * the specified cell is in a border or a corner)
     *
     * @return a list containing the diagonal-adjacents cells in the order
     * defined by the
     * {@link #TOP_LEFT}, {@link #TOP_RIGHT}, {@link #BOTTOM_RIGHT} and
     * {@link #BOTTOM_LEFT} constants. Beware that these cosntants become
     * obsoletes if <code>deleteNulls</code> is true
     * @throws IndexOutOfBoundsException if the row and/or column of the given
     * cell are out of range
     */
    public ArrayList<Cell<E>> getDiagonalsAdjacents(Cell<E> cell, boolean deleteNulls) {
        return getDiagonalsAdjacents(cell.row, cell.column, deleteNulls);
    }

    /**
     * Get the row of the specified cells.
     *
     * @param cell the specified cell
     * @return a list containing the cells of the specified row from right to left
     * @throws IndexOutOfBoundsException if the row is out of range
     */
    public ArrayList<Cell<E>> getRow(Cell<E> cell) {
        return getRow(cell.row);
    }

    /**
     * Get the column of the specified cells.
     *
     * @param cell the specified cell
     * @return a list containing the cells of the specified row from right to left
     * @throws IndexOutOfBoundsException if the column is out of range
     */
    public ArrayList<Cell<E>> getColumn(Cell<E> cell) {
        return getColumn(cell.column);
    }

    /**
     * Get the specified descending diagonal of containing the specified cell.
     *
     * @param cell the specified cell
     * @return a list containing the cells of the descending diagonal that
     * contains the specified cell from the top-left cell to the bottom-right one
     * @throws IndexOutOfBoundsException if the row and/or column are out of
     * range
     */
    public ArrayList<Cell<E>> getDiagonalDesc(Cell<E> cell) {
        return getDiagonalDesc(cell.row, cell.column);
    }

    /**
     * Get the specified ascending diagonal of containing the specified cell.
     *
     * @param cell the specified cell
     * @return a list containing the cells of the ascending diagonal that
     * contains the specified cell from the bottom-left cell to the top-right one
     * @throws IndexOutOfBoundsException if the row and/or column are out of
     * range
     */
    public ArrayList<Cell<E>> getDiagonalAsc(Cell<E> cell) {
        return getDiagonalAsc(cell.row, cell.column);
    }

    /**
     * Return the number of rows in this Grid.
     *
     * @return the number of rows in this Grid
     */
    public int height() {
        return height;
    }

    /**
     * Return the number of columns in this Grid.
     *
     * @return the number of columns in this Grid
     */
    public int width() {
        return width;
    }

    /**
     * Return the cells of this Grid.
     *
     * @return the cells of this Grid
     */
    public UnmodifiableArrayList<Cell<E>> getCells() {
        return cells;
    }

    /**
     * Wrap an item of a Grid and handle some fancy methods.
     *
     * @see Grid
     * @param <E> the class that this Cell can contains
     */
    public static class Cell<E> {

        private E value;
        private final int row;
        private final int column;
        private final ArrayList<CellListener> listeners;

        private Cell(int row, int column, E value) {
            this.row = row;
            this.column = column;
            this.value = value;
            this.listeners = new ArrayList<>();
        }

        /**
         * Returns true if the value of this Cell is null.
         *
         * @return true if the value of this Cell is null
         */
        public boolean isEmpty() {
            return value == null;
        }

        /**
         * A convenience method that make the value of this Cell null.
         */
        public void clear() {
            value = null;
        }

        /**
         * Get the row number of this Cell.
         *
         * @return the row number of this Cell
         */
        public int getRow() {
            return row;
        }

        /**
         * Get the column number of this Cell.
         *
         * @return the column number of this Cell
         */
        public int getColumn() {
            return column;
        }

        /**
         * Get the value of this Cell.
         *
         * @return the value of this Cell
         */
        public E get() {
            return value;
        }

        /**
         * Set the item in this Cell.
         *
         * @param value the new value
         */
        public void set(E value) {
            E oldValue = this.value;
            this.value = value;
            for (CellListener listener : listeners) {
                listener.changed(this, oldValue, value);
            }
        }

        /**
         * Registers an event listener to this Cell. The listener is called when
         * the value of this cell is modified.
         * 
         * @param listener the listener to register
         */
        public void addCellListener(CellListener listener) {
            listeners.add(listener);
        }

        /**
         * Unregisters a previously registered listener from this Cell.
         *
         * @param listener the listener to unregister
         * @return true if this Grid contained the specified element
         */
        public boolean removeCellListener(CellListener listener) {
            return listeners.remove(listener);
        }

        /**
         * A listener that is called when the value of a registered Cell is
         * modified.
         *
         * @param <E> the class that this CellListener can handle
         */
        public interface CellListener<E> {
            void changed(Cell<E> cell, E oldValue, E newValue);
        }
    }

    /**
     * A generator that generate a Cell value by its row and column number.
     * 
     * @see GridLib.Grid.Cell
     * @param <E> the class of object this CellValueGenerator can generate
     */
    public interface CellValueGenerator<E> {

        /**
         * Generate a value for a Cell.
         *
         * @param row the row of the Cell
         * @param column the column of the Cell
         * @return the generated value
         */
        E generate(int row, int column);
    }
}
