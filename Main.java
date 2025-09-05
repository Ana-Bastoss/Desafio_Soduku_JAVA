import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        SudokuGame game;
        if (args.length > 0) {
            System.out.println("Argumentos recebidos: " + args.length + " parâmetros");
            game = new SudokuGame(args);
        } else {
            System.out.println("Nenhum argumento fornecido, usando puzzle padrão");
            game = new SudokuGame();
        }
        
        Scanner scanner = new Scanner(System.in);
        int option;
        
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║             SUDOKU                ║");
        System.out.println("║     Jogo Interativo em Java       ║");
        System.out.println("╚═══════════════════════════════════╝");
        
        game.displayBoard();
        
        do {
            System.out.println("\n┌───────────────────────────────────┐");
            System.out.println("│     Selecione uma das opções:       │");
            System.out.println("├─────────────────────────────────────┤");
            System.out.println("│ 1 - Iniciar um novo jogo            │");
            System.out.println("│ 2 - Colocar um número               │");
            System.out.println("│ 3 - Remover um número               │");
            System.out.println("│ 4 - Visualizar jogo atual           │");
            System.out.println("│ 5 - Verificar status do jogo        │");
            System.out.println("│ 6 - Limpar jogo                     │");
            System.out.println("│ 7 - Finalizar jogo (mostrar solução)│");
            System.out.println("│ 8 - Sair                            │");
            System.out.println("└─────────────────────────────────────┘");
            
            System.out.print("\n Opção: ");
            
            try {
                option = scanner.nextInt();
                
                switch (option) {
                    case 1:
                        game.newGame();
                        game.displayBoard();
                        break;
                    case 2:
                        game.placeNumber();
                        break;
                    case 3:
                        game.removeNumber();
                        break;
                    case 4:
                        game.displayBoard();
                        break;
                    case 5:
                        game.checkStatus();
                        break;
                    case 6:
                        game.clearBoard();
                        break;
                    case 7:
                        game.showSolution();
                        break;
                    case 8:
                        System.out.println(" Obrigado por jogar Sudoku!");
                        System.out.println("Até a próxima!");
                        break;
                    default:
                        System.out.println("Opção inválida! Por favor, escolha entre 1 e 8.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, digite um número válido!");
                scanner.next(); // Clear invalid input
                option = 0; // Continue loop
            }
            
        } while (option != 8);
        
        scanner.close();
    }
}
