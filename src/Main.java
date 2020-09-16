
public class Main {
    public static void main(String[] args) {
        System.out.println();
        boolean flag = false;
        do {
             flag = new Genetic().generateSchedule();
        } while (!flag);
    }
}
