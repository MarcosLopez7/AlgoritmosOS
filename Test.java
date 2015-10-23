import java.util.Scanner;

public class Test
{
	public static void main(String argv[])
	{
		//OPCIONES DEL MENU
		System.out.println("BIENVENIDO A LA TAREA DE ERICK Y MARCOS, MOY\n");
		System.out.println("DIME QUE ALGORITMO QUIERES CORRER: ");
		System.out.println("1.- Short Job First Apropiativo");
		System.out.println("2.- Round Robin");	

		//PEDIR DATO AL USUARIO
		Scanner scanIn = new Scanner(System.in);
		int option = Integer.parseInt(scanIn.nextLine());
	
		if(option == 1) //CORRER SHORT JOB FIRST
		{
			ShortJob algorithm = new ShortJob();
			algorithm.readFile();
			algorithm.algorithm();
		} 
		else if(option == 2) //CORRER ROUND ROBIN
		{
			RoundRobin rr = new RoundRobin();
	        	System.out.println();
       	 		System.out.println("--- LOG ---");
        		System.out.println("  T     P      Descripcion  (cola)");
        		for (int i = 0; i <= RoundRobin.TOTAL_TIME; i += RoundRobin.INTERVAL_TIME)
            		{	
				rr.executeAction(i);
				try
        	                {
  	                          Thread.sleep(100);                 // AQUI UTILIZAMOS UN SLEEP PARA VER MEJOR LOS RUNNING PROCESS EN .1 SEGUNDOS
                	        }
                        	catch(InterruptedException ex)
                        	{	
                        		Thread.currentThread().interrupt();
                        	}
			}
        		rr.showStats();
		}
		else //POR SI HAY ERROR
			System.out.println("Solo quiero 1 o 2, mas nada!");
		
		scanIn.close(); 
	}
}
