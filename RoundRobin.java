import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;


public class RoundRobin
{
    //NOTA: Para mayor flexibilidad se manejo el programa
    // por milisegundos
    public static int TOTAL_TIME     = 0;
    public static int INTERVAL_TIME  = 500;
    private       int quantum        = 0;
    private       int currentProcess = 0;


    //Atributos de los procesos
    private Vector<String>  names    = new Vector<>();
    private Vector<Integer> start    = new Vector<>();
    private Vector<Integer> duration = new Vector<>();
    private Vector<Integer> wait     = new Vector<>();

    //Vector referente a la cola del thread
    private Vector<Integer> queue = new Vector<>();

    //Vector referente al diagrama de grantt 
    private Vector<Integer> grantt = new Vector<>();
    private int lastDuration = 0;

    public RoundRobin()
    {
        readData();
    }

    //Lee datos del archivo procesos.txt
    public void readData()
    {
        List<String> lines = new Vector<>();
        try {
            lines = Files.readAllLines(Paths.get("procesos.txt"));
        } catch (Exception e) {
        }

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

    public void executeAction(int time)
    {

        Vector<Integer> newProcesses = new Vector<Integer>();
        for (int i = 1; i < start.size(); i++)
            if (start.get(i) == time) newProcesses.add(i);


        //Ordena para desempatar en caso de que haya más de un proceso
        //al mismo tiempo
        for (int j = 0; j < newProcesses.size(); j++) {
            for (int i = 0; i < newProcesses.size(); i++) {
                if (duration.get(newProcesses.get(i)) > duration.get(newProcesses.get(j))) {
                    int auxI = newProcesses.get(i);
                    newProcesses.set(i, newProcesses.get(j));
                    newProcesses.set(j, auxI);
                }
            }
        }

        //Los agrega ordenados
        for (int i = 0; i < newProcesses.size(); i++) {
            queue.add(0, newProcesses.get(i));
            System.out.printf("%06d", time);    //  -->  "00461012"
            System.out.println(
                    " <" + currentProcess + "> = " + "Entra " + newProcesses.get(i) + " " +
                    getStrQueue());
        }


        //Ejecuta un movimiento en caso de que se termine el quantum o que se termine el proceso actual
        if (duration.get(currentProcess) <= 0 || lastDuration % quantum == 0) {

            //Fin quantum
            if (lastDuration % quantum == 0) {
                System.out.printf("%06d", time);
                System.out.println(" <" + currentProcess + "> = Fin quantum");
            }

            if (duration.get(currentProcess) > 0) {
                queue.add(0, currentProcess);
            } else { //Fin proceso
                System.out.printf("%06d", time);
                System.out.println(" <" + currentProcess + "> = FIN PROCESO " + currentProcess);
            }
            //Agrega un nuevo proceso
            if (queue.size() != 0) {
                currentProcess = queue.lastElement();
                queue.remove(queue.size() - 1);
                lastDuration = 0;
                System.out.printf("%06d", time);
                System.out.println(
                        " <" + currentProcess + "> = NUEVO PROCESO " + currentProcess + " " +
                        getStrQueue());
            } else { //En caso de que no haya un proceso disponible
                currentProcess = 0;
                System.out.printf("%06d", time);
                System.out.println(" = Sin proceso");
            }
        }

        //Aumento en un intervalo el tiempo de espera
        for (int i = 0; i < queue.size(); i++) {
            wait.set(queue.get(i), wait.get(queue.get(i)) + INTERVAL_TIME);
        }
        System.out.println("-");
        duration.set(currentProcess, duration.get(currentProcess) - INTERVAL_TIME);
        lastDuration += INTERVAL_TIME;
        grantt.add(currentProcess);
    }

    //Obtiene un string de la cola para fines de demostracion
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


    //Muestra las estadisticas
    public void showStats()
    {
        int auxProc = 0;
        System.out.println();
        //Gantt
        System.out.println("--- Gantt ---");
        for (int i = 0; i < TOTAL_TIME; i += INTERVAL_TIME) {
            if (auxProc != grantt.get(i / INTERVAL_TIME)) {
                System.out.printf("%06d", i);
                System.out.println("ms -> " + names.get(grantt.get(i / INTERVAL_TIME)));
                auxProc = grantt.get(i / INTERVAL_TIME);
            }
        }

        //Tiempos de espera
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
