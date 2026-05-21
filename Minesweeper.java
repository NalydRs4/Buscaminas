import java.util.Scanner;
public class Minesweeper {
    static int ROWS,COLUMNS,MINES, remainingCells;
    static char[][] grid;
    static int[] move = new int[2];
    static boolean[][] revealed;
    static boolean gameWon, gameLost;
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.format("%40s", "TechVidvan's Minesweeper Game\n"); // Game title
        System.out.println("\n----MENU---- ");  //Adding Menu to choose difficulty level of the game
        System.out.println(" 1. Hard\n 2. Medium\n 3. Easy\n 4. Custom\n");
        System.out.println("\nPlease choose the difficulty: ");
        int difficulty = sc.nextInt();
        grid_size(difficulty);
        play();
    }
    private static void grid_size(int difficulty) {
        switch (difficulty) {  // A menu based system for adjusting the difficulty.
            case 1:
                ROWS = 30;
                COLUMNS = 16;
                MINES = 99;
                break;
            case 2:
                ROWS = 16;
                COLUMNS = 16;
                MINES = 40;
                break;
            case 3:
                ROWS = 9;
                COLUMNS = 9;
                MINES = 10;
                break;
            case 4:
                System.out.println("Enter the number of rows, columns, and mines: \n");
                ROWS = sc.nextInt();
                COLUMNS = sc.nextInt();
                MINES = sc.nextInt();
                break;
            default:
                System.out.println("Invalid choice\n");
                System.exit(0);
        }
        grid = new char[ROWS][COLUMNS];
    }
    private static void initializeGrid() { // Initialize the game grid with empty cells.
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                grid[i][j] = '-';
            }
        }
        revealed = new boolean[ROWS][COLUMNS];   // Initialize the revealed array.
        remainingCells = ROWS * COLUMNS - MINES; // Calculate the number of remaining cells to be revealed.
    }
    private static void placeMines() { // Place mines randomly on the grid.
        int minesPlaced = 0;
        while (minesPlaced < MINES) {
            int row = (int) (Math.random() * ROWS);
            int col = (int) (Math.random() * COLUMNS);
            if (grid[row][col] != 'M') {
                grid[row][col] = 'M';
                minesPlaced++;
            }
        }
    }
    private static void calculateAdjacentMines() {  // It calculated the adjacent numbers along the mines for each cell.
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (grid[i][j] != 'M') {
                    int count = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if ((x != 0 || y != 0) && isValidCell(i + x, j + y) && grid[i + x][j + y] == 'M'){
                                count++;
                            }
                        }
                    }
                    if (count > 0) {
                        grid[i][j] = (char) (count + '0');
                    }
                }
            }
        }
    }
    private static boolean isValidCell(int row, int col) { // Used to check for a valid cell.
        return row >= 0 && row < ROWS && col >= 0 && col < COLUMNS;
    }
    private static void printGrid() { //This is the sole function to print the grid everytime over the output screen.
        System.out.print("     ");
        for (int j = 0; j < COLUMNS; j++) {
            System.out.printf(" %3s", j);
        }
        System.out.println(); // Print column numbers on top for reference
        for (int i = 0; i < ROWS; i++) {
            System.out.print("\t" + i);
            for (int j = 0; j < COLUMNS; j++) {
                if (grid[i][j] == 'F') {
                    System.out.printf("\tF");
                } else if (revealed[i][j]) {
                    if (grid[i][j] == '-') {
                        System.out.print("\t0");
                    } else {
                        System.out.print("\t" + grid[i][j]);
                    }
                } else {
                    System.out.printf("\t-");
                }
            }
            System.out.println(); // Print the grid
        }
    }
    private static void revealCell(int row, int col) {  //Reveals the cells which have been visited
        if (!isValidCell(row, col) || revealed[row][col]) {
            return;
        }
        revealed[row][col] = true;
        remainingCells--;
        if (grid[row][col] == '-') {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    revealCell(row + x, col + y);
                }
            }
        }
    }
    private static void revealAllCells() { // It is to reveal all the cells in output screen.
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                revealed[i][j] = true;
            }
        }
    }
    private static boolean checkWin() { //This is to check if the user has completed the game successfully or not.
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (grid[i][j] != 'M' && !revealed[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    private static void getPlayerMove() { // This method is defined to take input from the user.
        System.out.print("Enter row number: ");
        move[0] = sc.nextInt();
        System.out.print("Enter column number: ");
        move[1] = sc.nextInt();
    }
    public static void play() {
        initializeGrid(); // Game initialization
        printGrid();
        getPlayerMove();
        int row = move[0];
        int col = move[1];
        placeMines(); // Place mines after the first move
        calculateAdjacentMines();
        while (!gameWon && !gameLost) {
            if (grid[row][col] == 'M') { // Check if player stepped on a mine
                gameLost = true;
                revealAllCells();
                printGrid();
                System.out.println("Game Over! You stepped on a mine.");
                break; // Exit the loop
            } else {
                revealCell(row, col);
                remainingCells--;
                printGrid();
                if (checkWin()) { // Check if player cleared all non-mine cells
                    gameWon = true;
                    System.out.println("Congratulations! You cleared all the mines.");
                    break; // Exit the loop
                }
                getPlayerMove();
                row = move[0];
                col = move[1];
            }
        }
    }
}