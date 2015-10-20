import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;


public class RoundRobin
{

    public static int TOTAL_TIME    = 0;
    public static int INTERVAL_TIME = 500;
    private int             quantum        = 0;
    private int             currentProcess = 0;
    private Vector<Integer> grantt         = new Vector<>();
    private Vector<String>  names          = new Vector<>();
    private Vector<Integer> start          = new Vector<>();
    private Vector<Integer> duration       = new Vector<>();
    private Vector<Integer> wait           = new Vector<>();
    private Vector<Integer> queue          = new Vector<>();

    public RoundRobin()
    {
        readData();
    }

    public void readData()
    {
        List<String> lines = new Vector<>();
        try {
            lines = Files.readAllLines(Paths.get("procesos.txt"));
        } catch (Exception e) {}

        quantum = Integer.parseInt(lines.get(0)) * 1000;
        names.add("");
        start.add(0);
        duration.add(0);
        wait.add(0);

        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(" ");
            names.add(values[0]);
            start.add((int) (Float.parseFloat(values[1]) * 1000));
            duration.add((int) (Float.parseFloat(values[2]) * 1000));
            TOTAL_TIME += (int) (Float.parseFloat(values[2]) * 1000);
            wait.add(0);
        }
    }

    private int lastDuration = 0;
    public void executeAction(int time)
    {
        for (int i = 1; i < start.size(); i++) {
            if (start.get(i) == time) {
                queue.add(0, i);
                System.out.printf("%06d", time);    //  -->  "00461012"
                System.out.println(
                        " <" + currentProcess + "> = " + "Entra " + i + " " + getStrQueue());
            }
        }

        if (duration.get(currentProcess) <= 0 || lastDuration % quantum == 0) {

            if (lastDuration % quantum == 0) {
                System.out.printf("%06d", time);
                System.out.println(" <" + currentProcess + "> = Fin quantum");
            }

            if (duration.get(currentProcess) > 0) {
                queue.add(0, currentProcess);
            } else {
                System.out.printf("%06d", time);
                System.out.println(" <" + currentProcess + "> = FIN PROCESO " + currentProcess);
            }
            if (queue.size() != 0) {
                currentProcess = queue.lastElement();
                queue.remove(queue.size() - 1);
                lastDuration = 0;
                System.out.printf("%06d", time);
                System.out.println(
                        " <" + currentProcess + "> = NUEVO PROCESO " + currentProcess + " " +
                        getStrQueue());
            } else {
                currentProcess = 0;
                System.out.printf("%06d", time);
                System.out.println(" = Sin proceso");
            }
        }

        for (int i = 0; i < queue.size(); i++) {
            wait.set(queue.get(i), wait.get(queue.get(i)) + INTERVAL_TIME);
        }
        System.out.println("-");
        duration.set(currentProcess, duration.get(currentProcess) - INTERVAL_TIME);
        lastDuration += INTERVAL_TIME;
        grantt.add(currentProcess);
    }

    public String getStrQueue()
    {
        String strQueue = " <";
        for (int i = 0; i < queue.size(); i++) {
            strQueue += queue.get(i) + "(" + (duration.get(queue.get(i)) / 1000) + ")";
            if (i < queue.size() - 1) strQueue += "->";
        }
        strQueue += ">";
        return strQueue;
    }


    public void showStats()
    {
        int auxProc = 0;
        System.out.println();
        System.out.println("--- Gantt ---");
        for (int i = 0; i < TOTAL_TIME; i += INTERVAL_TIME) {
            if (auxProc != grantt.get(i / INTERVAL_TIME)) {
                System.out.printf("%06d", i);
                System.out.println("ms -> " + names.get(grantt.get(i / INTERVAL_TIME)));
                auxProc = grantt.get(i / INTERVAL_TIME);
            }
        }

        int total = 0;
        System.out.println();
        System.out.println("--- Tiempos de espera ---");
        for (int i = 1; i < wait.size(); i++) {
            System.out.println(names.get(i) + " " + "(" + wait.get(i) + "ms)");
            total += wait.get(i);
        }
        System.out.println();
        System.out.println("--- Tiempo promedio: " + (total / wait.size()) + "ms");
    }
}
