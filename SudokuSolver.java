import java.util.HashSet;
import java.util.Set;

public class SudokuSolver {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    
    public static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            
                            if (solveSudoku(board)) {
                                return true;
                            }
                            
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isValid(int[][] board, int row, int col, int num) {
        // Verificar linha
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num) {
                return false;
            }
        }
        
        // Verificar coluna
        for (int x = 0; x < SIZE; x++) {
            if (board[x][col] == num) {
                return false;
            }
        }
        
        // Verificar quadrante 3x3
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;
        
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public static boolean isComplete(int[][] board) {
        // Primeiro verifica se não há células vazias
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        
        // Depois verifica se o Sudoku está correto
        return isValidSudoku(board);
    }
    
    public static boolean isValidSudoku(int[][] board) {
        // Verificar todas as linhas
        for (int row = 0; row < SIZE; row++) {
            Set<Integer> seen = new HashSet<>();
            for (int col = 0; col < SIZE; col++) {
                int num = board[row][col];
                if (num != 0 && !seen.add(num)) {
                    return false;
                }
            }
        }
        
        // Verificar todas as colunas
        for (int col = 0; col < SIZE; col++) {
            Set<Integer> seen = new HashSet<>();
            for (int row = 0; row < SIZE; row++) {
                int num = board[row][col];
                if (num != 0 && !seen.add(num)) {
                    return false;
                }
            }
        }
        
        // Verificar todos os quadrantes 3x3
        for (int boxRow = 0; boxRow < SUBGRID_SIZE; boxRow++) {
            for (int boxCol = 0; boxCol < SUBGRID_SIZE; boxCol++) {
                Set<Integer> seen = new HashSet<>();
                for (int row = boxRow * SUBGRID_SIZE; row < (boxRow + 1) * SUBGRID_SIZE; row++) {
                    for (int col = boxCol * SUBGRID_SIZE; col < (boxCol + 1) * SUBGRID_SIZE; col++) {
                        int num = board[row][col];
                        if (num != 0 && !seen.add(num)) {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    public static int countEmptyCells(int[][] board) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public static String getDifficultyLevel(int[][] board) {
        int emptyCells = countEmptyCells(board);
        if (emptyCells <= 30) return "Fácil";
        else if (emptyCells <= 45) return "Médio";
        else if (emptyCells <= 55) return "Difícil";
        else return "Extremo";
    }
}
