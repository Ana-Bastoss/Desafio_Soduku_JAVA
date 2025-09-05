import java.util.Scanner;
import java.util.InputMismatchException;

public class SudokuGame {
    private static final int SIZE = 9;
    private int[][] board;
    private boolean[][] originalBoard;
    private Scanner scanner;
    private int movesCount;
    
    public SudokuGame() {
        this.board = new int[SIZE][SIZE];
        this.originalBoard = new boolean[SIZE][SIZE];
        this.scanner = new Scanner(System.in);
        this.movesCount = 0;
        initializeGame();
    }
    
    public SudokuGame(String[] puzzleData) {
        this.board = new int[SIZE][SIZE];
        this.originalBoard = new boolean[SIZE][SIZE];
        this.scanner = new Scanner(System.in);
        this.movesCount = 0;
        
        if (puzzleData != null && puzzleData.length > 0) {
            loadPuzzleFromArgs(puzzleData);
        } else {
            initializeGame();
        }
    }
    
    private void loadPuzzleFromArgs(String[] puzzleData) {
        try {
            // Limpar tabuleiro
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    board[i][j] = 0;
                    originalBoard[i][j] = false;
                }
            }
            
            String allData = String.join(" ", puzzleData);
            String[] entries = allData.split(" ");
            
            // Parse formato: linha,coluna;valor,isOriginal
            for (String data : entries) {
                if (data.trim().isEmpty()) continue;
                
                String[] parts = data.split(";");
                if (parts.length == 2) {
                    String[] position = parts[0].split(",");
                    String[] valueInfo = parts[1].split(",");
                    
                    if (position.length == 2 && valueInfo.length == 2) {
                        int row = Integer.parseInt(position[0]);
                        int col = Integer.parseInt(position[1]);
                        int value = Integer.parseInt(valueInfo[0]);
                        boolean isOriginal = Boolean.parseBoolean(valueInfo[1]);
                        
                        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && value >= 1 && value <= 9) {
                            board[row][col] = value;
                            originalBoard[row][col] = isOriginal;
                        }
                    }
                }
            }
            
            if (SudokuSolver.isValidSudoku(board)) {
                System.out.println("Puzzle carregado e validado com sucesso dos argumentos!");
                System.out.println("Tabuleiro carregado com " + SudokuSolver.countEmptyCells(board) + " células vazias");
            } else {
                System.out.println("Puzzle carregado mas contém erros de validação!");
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao carregar puzzle dos argumentos: " + e.getMessage());
            System.out.println("Usando puzzle padrão.");
            initializeGame();
        }
    }
    
    private void initializeGame() {
        int[][] puzzle = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = puzzle[i][j];
                originalBoard[i][j] = (puzzle[i][j] != 0);
            }
        }
        this.movesCount = 0;
    }
    
    public void displayBoard() {
        System.out.println("\n╔═══════════════════════════╗");
        System.out.println("║       SUDOKU BOARD        ║");
        System.out.println("╚═══════════════════════════╝");
        System.out.println("    1 2 3   4 5 6   7 8 9");
        System.out.println("  ┌───────┬───────┬───────┐");
        
        for (int i = 0; i < SIZE; i++) {
            if (i == 3 || i == 6) {
                System.out.println("  ├───────┼───────┼───────┤");
            }
            System.out.print((i + 1) + " │ ");
            
            for (int j = 0; j < SIZE; j++) {
                if (j == 3 || j == 6) {
                    System.out.print("│ ");
                }
                
                if (board[i][j] == 0) {
                    System.out.print("· ");
                } else {
                    if (originalBoard[i][j]) {
                        System.out.print(board[i][j] + " ");
                    } else {
                        System.out.print(board[i][j] + " ");
                    }
                }
            }
            System.out.println("│");
        }
        System.out.println("  └───────┴───────┴───────┘");
        
        int emptyCells = SudokuSolver.countEmptyCells(board);
        String difficulty = SudokuSolver.getDifficultyLevel(board);
        System.out.println("Células vazias: " + emptyCells + " | Dificuldade: " + difficulty + " | Movimentos: " + movesCount);
    }
    
    public void placeNumber() {
        try {
            System.out.print("Digite a linha (1-9): ");
            int row = getValidInput(1, 9) - 1;
            System.out.print("Digite a coluna (1-9): ");
            int col = getValidInput(1, 9) - 1;
            System.out.print("Digite o número (1-9): ");
            int num = getValidInput(1, 9);
            
            if (originalBoard[row][col]) {
                System.out.println("Esta célula não pode ser modificada!");
                return;
            }
            
            if (SudokuSolver.isValid(board, row, col, num)) {
                board[row][col] = num;
                movesCount++;
                System.out.println("Número colocado com sucesso!");
                
                if (SudokuSolver.isComplete(board)) {
                    System.out.println("PARABÉNS! Você completou o Sudoku em " + movesCount + " movimentos!");
                }
            } else {
                System.out.println("Movimento inválido! Este número já existe na linha, coluna ou quadrante.");
            }
        } catch (Exception e) {
            System.out.println("Entrada inválida!");
        }
    }
    
    public void removeNumber() {
        try {
            System.out.print("Digite a linha (1-9): ");
            int row = getValidInput(1, 9) - 1;
            System.out.print("Digite a coluna (1-9): ");
            int col = getValidInput(1, 9) - 1;
            
            if (originalBoard[row][col]) {
                System.out.println("Esta célula não pode ser modificada!");
                return;
            }
            
            if (board[row][col] != 0) {
                board[row][col] = 0;
                movesCount++;
                System.out.println("Número removido com sucesso!");
            } else {
                System.out.println("Esta célula já está vazia!");
            }
        } catch (Exception e) {
            System.out.println("Entrada inválida!");
        }
    }
    
    private int getValidInput(int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.print("Por favor, digite um número entre " + min + " e " + max + ": ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Por favor, digite um número válido: ");
                scanner.next(); // Clear invalid input
            }
        }
    }
    
    public void checkStatus() {
        if (SudokuSolver.isComplete(board)) {
            System.out.println("PARABÉNS! Você completou o Sudoku corretamente!");
            System.out.println("Estatísticas finais:");
            System.out.println("   • Movimentos realizados: " + movesCount);
            System.out.println("   • Status: COMPLETO");
        } else {
            int emptyCells = SudokuSolver.countEmptyCells(board);
            boolean isValid = SudokuSolver.isValidSudoku(board);
            
            System.out.println("Status atual do jogo:");
            System.out.println("   • Células vazias: " + emptyCells);
            System.out.println("   • Movimentos realizados: " + movesCount);
            System.out.println("   • Configuração válida: " + (isValid ? "Sim" : "Não"));
            System.out.println("   • Progresso: " + String.format("%.1f", ((81.0 - emptyCells) / 81.0) * 100) + "%");
            
            if (!isValid) {
                System.out.println("Atenção: Há conflitos no tabuleiro atual!");
            }
        }
    }
    
    public void showSolution() {
        int[][] solutionBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                solutionBoard[i][j] = board[i][j];
            }
        }
        
        if (SudokuSolver.solveSudoku(solutionBoard)) {
            System.out.println("\n SOLUÇÃO DO SUDOKU:");
            displaySolution(solutionBoard);
        } else {
            System.out.println("Não foi possível resolver este Sudoku!");
        }
    }
    
    private void displaySolution(int[][] solution) {
        System.out.println("    1 2 3   4 5 6   7 8 9");
        System.out.println("  ┌───────┬───────┬───────┐");
        
        for (int i = 0; i < SIZE; i++) {
            if (i == 3 || i == 6) {
                System.out.println("  ├───────┼───────┼───────┤");
            }
            System.out.print((i + 1) + " │ ");
            
            for (int j = 0; j < SIZE; j++) {
                if (j == 3 || j == 6) {
                    System.out.print("│ ");
                }
                System.out.print(solution[i][j] + " ");
            }
            System.out.println("│");
        }
        System.out.println("  └───────┴───────┴───────┘");
    }
    
    public void clearBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!originalBoard[i][j]) {
                    board[i][j] = 0;
                }
            }
        }
        movesCount = 0;
        System.out.println("Tabuleiro limpo! Apenas os números originais foram mantidos.");
    }
    
    public void newGame() {
        initializeGame();
        System.out.println("Novo jogo iniciado!");
    }
    
    public int[][] getBoard() {
        return board;
    }
}
