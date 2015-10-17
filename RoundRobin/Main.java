public class Main
{

    public static void main(String[] args)
    {
        RoundRobin rr = new RoundRobin();
        System.out.println();
        System.out.println("--- LOG ---");
        System.out.println("  T     P      Descripcion  (cola)");
        for (int i = 0; i <= RoundRobin.TOTAL_TIME; i += RoundRobin.INTERVAL_TIME)
            rr.executeAction(i);
        rr.showStats();
    }
}
