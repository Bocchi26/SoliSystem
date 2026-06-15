package games.solisystem.presentation;

public class Main {
    public static void main(String[] args) {
        System.out.println("Inicializando componentes de SolySistem...");
        ConsoleController controller = new ConsoleController();
        controller.iniciar();
    }
}