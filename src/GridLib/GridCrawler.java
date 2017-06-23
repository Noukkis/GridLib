package GridLib;

import GridLib.Grid.Cell;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A tool used to crawl in a {@link Grid} a search specific cells in it.
 *
 * @author Jordan Vésy
 * @since 1.0
 * @see GridLib.Grid
 * @see GridLib.Grid.Cell
 * @param <E> the class that the grid that this crawler can handle can contains
 */
public abstract class GridCrawler<E> {

    private final Grid<E> grid;
    private final HashMap<Integer, ArrayList<Cell<E>>> cells;
    private int idMaker;
    private Thread t;

    /**
     * A global state that one can use however they want to modify the behavior
     * of this crawler and its subcrawlers
     */
    private int globalState;

    /**
     * Construct a GridCrawler that can crawl in the specified Grid.
     *
     * @param grid the grid that this GridCrawler can crawl in
     */
    public GridCrawler(Grid<E> grid) {
        this.grid = grid;
        cells = new HashMap<>();
        globalState = 0;
        idMaker = -10;
        t = new Thread();
    }

    /**
     * Returns the GlobalState of this Crawler.
     *
     * @return the GlobalState of this Crawler
     */
    public int getGlobalState() {
        return globalState;
    }

    /**
     * Set the GlobalState of this Crawler.
     * @param globalState the global state to set
     */
    public void setGlobalState(int globalState) {
        this.globalState = globalState;
    }

    /**
     * Flag the specified cell with the specified flag.
     *
     * @param flag the flag that you want to use
     * @param c the cell to flag
     */
    public void flag(int flag, Cell<E> c) {
        if (!cells.containsKey(flag)) {
            cells.put(flag, new ArrayList<>());
        }
        cells.get(flag).add(c);
    }

    /**
     * Flag the specified cells with the specified flag.
     *
     * @param flag the flag that you want to use
     * @param c a list of the cells to flag
     */
    public void flag(int flag, ArrayList<Cell<E>> c) {
        if (!cells.containsKey(flag)) {
            cells.put(flag, new ArrayList<>());
        }
        cells.get(flag).addAll(c);
    }

    /**
     * Get a list containing the cells flagged with the specified flag.
     *
     * @param flag the flag you want to get the cells flagged with
     * @return a list containing the cells flagged with the specified flag
     */
    public ArrayList<Cell<E>> flagged(int flag) {
        return cells.containsKey(flag) ? cells.get(flag) : new ArrayList<>();
    }

    /**
     * Use this method to crawl in the Grid and flag the cells you want.
     *
     * @param startingPoint the point where your search start
     */
    public abstract void crawl(Cell<E> startingPoint);

    /**
     * Start crawling in the grid in a new Thread.
     *
     * @param startingPoint the point where your search start
     */
    public void startCrawling(Cell<E> startingPoint) {
        t = new Thread(() -> {
            crawl(startingPoint);
        });
        t.start();
    }

    /**
     * Block until the crawl is finished.
     */
    public void blockUtilFinished() {
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(GridCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * A crawler that crawl in the same Grid that its parent
     * {@link GridLib.GridCrawler} and share the same flag list.
     */
    public abstract class Subcrawler {

        /**
         * A state that one can use however they want to modify the behavior of
         * this crawler
         */
        private int state;

        /**
         * The id of this subcrawler automatically generated at its
         * construction. Note that this id is a negative number so you can
         * easely use it as a flag without interfering other flags.
         */
        private final int id;

        /**
         * Construct a Subcrawler
         */
        public Subcrawler() {
            this.state = 0;
            this.id = idMaker;
            idMaker--;
        }

        /**
         * Returns the id of this Subcralwer
         *
         * @return the id of this Subcralwer
         */
        public int getId() {
            return id;
        }

        /**
         * Use this method to crawl in the Grid and flag the cells you want.
         *
         * @param startingPoint the point where your search start
         */
        public abstract void crawl(Cell<E> startingPoint);

        /**
         * Returns the state of this Subcralwer
         *
         * @return the state of this Subcralwer
         */
        public int getState() {
            return state;
        }

        /**
         * Set the state of this Subcralwer
         * @param state the state to set
         */
        public void setState(int state) {
            this.state = state;
        }

        /**
         * Start crawling in the grid.
         *
         * @param startingPoint the point where your search start
         */
        public void start(Cell<E> startingPoint) {
            crawl(startingPoint);
        }
    }
}
