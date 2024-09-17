package Logic;

public class Board {
    private Cell[][] cells;
    private List<LaserEmitter> emitters;
    private final List<LaserReceiver> receivers;
    private final int rows;
    private final int cols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        cells = new Cell[rows][cols];
        emitters = new ArrayList<>();
        receivers = new ArrayList<>();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    //TODO: Crear listener
    //TODO: Añadir un handler para que segun el nivel cree el tablero
    public void addBlock(block Block) {

    }
    public void addEmitter(LaserEmitter emitter) {
        emitters.add(emitter);
    }
    public void addReceiver(LaserReceiver receiver) {
        receivers.add(receiver);
    }
    public void removeEmitter(LaserEmitter emitter) {
        emitters.remove(emitter);
    }
}
