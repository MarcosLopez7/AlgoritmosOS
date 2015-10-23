import java.io.*;
import java.util.*;

public class ShortJob
{
	/*
		Variables para desarrollo del algoritmo
	*/

	private File file = null; //Variable para capturar el archivo
	private FileReader reader = null;//Variable que lee el archivo
    	private BufferedReader buffer = null; //buffer para utilizar las líneas de texto del archivo
	private Vector<Vector<String>> processList = new Vector<Vector<String>>(0);	 // VECTOR DONDE SE ALMACENAS LOS PROCESOS DESDE EL ARCHIVO DE TEXTO
	private Vector<Vector<String>> waitingQueue = new Vector<Vector<String>>(0);	// VECTOR DE LOS PROCESOS QUE SE QUEDAN EN WAITING
	private Vector<Vector<String>> finishedProcess = new Vector<Vector<String>>(0); //VECTOR DE LOS PROCESOS QUE TERMINARON EN EJECUTARSE

	/*
		TODOS LOS VECTORES SON BIDIMENSIONALES DE STRING
	*/

	//FUNCION QUE SIRVE PARA OBTENER LOS PROCESOS DEL ARCHIVO DE TEXTO Y ALMACENARLOS EN EL VECTOR processList
	public void readFile()
	{	
		//EXCEPCIÓN PARA LA LECTURA DE ARCHIVOS
		try
		{
       		        file = new File ("procesos.txt");
                	reader = new FileReader(file);
                	buffer = new BufferedReader(reader);

			String line = buffer.readLine();
			
			while(line != null)
                	{
                		line = buffer.readLine();  //LEEMOS EL QUATUM, PERO LO TIRAMOS PORQUE NO NOS SIRVE EN ESTE ALGORITMO     		
		
			 	StringTokenizer token = new StringTokenizer(line, " "); //UTILIZAR EL TOKENIZER PARA DIVIDIR EL TEXTO POR CADA PALABRA
				Vector<String> temporalVector = new Vector<String>(0); //VECTOR TEMPORAL PARA LLENAR EL VECTOR DE PROCESOS

				for(int i=0; i<3; i++) 
                        	{
     					temporalVector.addElement(token.nextToken());
	                   	}
					temporalVector.addElement("0.0");//ESTE VA SER EL WAITING TIME DE CADA PROCESO
					
				processList.addElement(temporalVector);
                	}//cierre while


		}// cierre try
        	catch(Exception e)
		{

        	}//cierre catch
			
	}

	//FUNCION PARA CORRER EL ALROGITMO
	public void algorithm()
	{
		Vector<String> runningProcess = null;//ESTE VA SER EL PROCESO EN RUNNING
		Vector<String> nextProcess = null;//ESTE ES EL PROCESO EN QUE SU TIEMPO DE EJECUCION LLEGÓ

		for(double i = 0.0; !processList.isEmpty() || !waitingQueue.isEmpty() || runningProcess != null; i += 0.5) //ESTO ACABA CUANDO YA TODOS LOS PROCESOS ACABARON
		{
			nextProcess = processReady(i); //VERIFICA SI YA HAY UN PROCESO QUE YA LE HAYA LLEGADO SU TIEMPO DE LLEGADA, SI NO HAY NINGUNO, SE QUEDA NULL

			//EN CASO DE QUE NO HAYA PROCESOS CORRIENDO, SOLO APLICA PARA EL INICIO
			if(runningProcess == null)
			{
				runningProcess = (Vector<String>)nextProcess.clone();
                                nextProcess = null;

			}
			else
			{
				//SI Y YA ACABO EL BURST TIME DE UN ALGORITMO Y AL MISMO TIEMPO LLEGO UN NUEVO PROCESO
				if(runningProcess.elementAt(2).equals("0.0") && nextProcess != null)
				{
					//SI NO HAY PROCESO EN WAITING, QUE EL PROCESO QUE LLEGO SEA RUNNING
					if(waitingQueue.isEmpty())
					{
						finishedProcess.addElement((Vector<String>)runningProcess.clone());
						runningProcess = (Vector<String>)nextProcess.clone();
						nextProcess = null;
					}
					else //SI SI HAY PROCESOS EN WAITING
					{
						//SI EL BURST TIME DEL PROCESO CON MENOR BURST ES MAYOR QUE EL NUEVO PROCESO, EL NUEVO PROCESO SIGUE EN RUNNING
						if(Double.parseDouble(waitingQueue.elementAt(0).elementAt(2)) > Double.parseDouble(nextProcess.elementAt(2)))
						{
							finishedProcess.addElement((Vector<String>)runningProcess.clone());
							runningProcess = (Vector<String>)nextProcess.clone();
	                                        	nextProcess = null;
						}
						else //SI ES AL REVES, EL WAITING CON MENOR BURST PARA A RUNNING
						{
							finishedProcess.addElement((Vector<String>)runningProcess.clone());
							runningProcess = (Vector<String>)waitingQueue.firstElement().clone();
	                                        	waitingQueue.removeElementAt(0);
							waitingQueue.addElement((Vector<String>)nextProcess.clone());
		                                        sort();
						}
					}
				}
				else if(nextProcess != null) //SI LLEGO UN NUEVO PROCESO ENTONCES...
				{
					//SI EL RUNNING ES MAYOR QUE EL NUEVO PROCESSO, EL NUEVO PROCESO PASA A RUNNING Y EL QUE ERA RUNNING PASA A LA COLA
					if(Double.parseDouble(runningProcess.elementAt(2)) > Double.parseDouble(nextProcess.elementAt(2)))
					{	
						waitingQueue.addElement((Vector<String>)runningProcess.clone());
						runningProcess =(Vector<String>)nextProcess.clone();
						nextProcess = null;
						sort();
					}
					else// SI NO, SIMPLEMENTE EL NUEVO PROCESO PASA A LA COLA
					{
						waitingQueue.addElement((Vector<String>)nextProcess.clone());
						nextProcess = null;
						sort();
					}
				}
				else if(runningProcess.elementAt(2).equals("0.0")) //SI EL PROCESO YA ACABO SU BURST TIME
				{
					if(waitingQueue.isEmpty()) //SI NO HAY NADA EN LA COLA, ESTO INDICA QUE YA ACABO EL ALGORITMO
					{
						finishedProcess.addElement((Vector<String>)runningProcess.clone());
						runningProcess = null;
					}
					else //SI HAY, SIMPLEMENTE EL PRIMERO EN LA COLA PASA A RUNNING
					{
						finishedProcess.addElement((Vector<String>)runningProcess.clone());
						runningProcess = (Vector<String>)waitingQueue.firstElement().clone(); 
						waitingQueue.removeElementAt(0);
					}
				}
			}

			if(runningProcess != null) 
			{	
				runningProcess.set(2, Double.toString( Double.parseDouble(runningProcess.elementAt(2) ) - 0.5 )); //AQUI LE RESTA BUSRT TIME AL PROCESO EN RUNNING DE .5 
				sumWait();// AQUI INVOCA UNA FUNCION QUE LE SUMA WAITING TIME A LOS PROCESOS QUE ESTAN EN LA COLA DE .5 
				print(runningProcess.elementAt(0), i, true); //AQUI SE IMPRIME EL DIAGRAMA CON EL RUNNING TIME
			}
			else //ESTE ELSE ENTRA CUANDO YA PRACTICAMENTE CUANDO YA ACABO ESTO
			{
				System.out.println("----------" + i);
				print(null, 0, false); //AQUI IMPRIME LOS WAITING TIME DE TODOS LOS PROCESOS Y EL PRIMEDIO
			}

			try 
			{
			    Thread.sleep(100);                 // AQUI UTILIZAMOS UN SLEEP PARA VER MEJOR LOS RUNNING PROCESS EN .1 SEGUNDOS
			} 
			catch(InterruptedException ex) 
			{
    			Thread.currentThread().interrupt();
			}	
		}
	}

	//LA FUNCION QUE DEVUELVE CUANDO LLEGA UN NUEVO PROCESO
	private Vector<String> processReady(double timeProcess)
	{
		Vector<String> process = null;

		for(int i = 0; i < processList.size(); i++)
		{
			if(Double.parseDouble(processList.elementAt(i).elementAt(1)) == timeProcess)
			{
				process = (Vector<String>)processList.elementAt(i).clone();
				processList.removeElementAt(i);
				return (Vector<String>)process.clone();
			}
		}
		return process;
	}

	//AQUI SE ORDENA LOS PROCESOS EN WAITING CON MENOR TIEMPO DE BURST CON EL ALGORITMO BURBUJA
	private void sort ()
	{
		for(int i = 0; i < waitingQueue.size(); i++)
		{
    			for(int j = i + 1; j < waitingQueue.size(); j++)
			{
        			if(Double.parseDouble(waitingQueue.elementAt(i).elementAt(2)) > Double.parseDouble(waitingQueue.elementAt(j).elementAt(2)))
				{
            				Vector <String> temp = waitingQueue.elementAt(i);
           		 		waitingQueue.set(i, waitingQueue.elementAt(j));
        				waitingQueue.set(j, temp);
				}
				else if(Double.parseDouble(waitingQueue.elementAt(i).elementAt(2)) == Double.parseDouble(waitingQueue.elementAt(j).elementAt(2)))
				{
					//EL ELSE IF ANTERIOR ES EL QUE VE SI HAY LOS MISMO BURST TIME, EL SIGUIENTE IF HACE EL DESEMPATE CON EL MENOR TIEMPO DE LLEGADA
					if(Double.parseDouble(waitingQueue.elementAt(i).elementAt(1)) > Double.parseDouble(waitingQueue.elementAt(j).elementAt(1)))
					{
						Vector <String> temp = waitingQueue.elementAt(i);
                                        	waitingQueue.set(i, waitingQueue.elementAt(j));
                                        	waitingQueue.set(j, temp);
					}
				}
    			}

		}	

	}

	//SUMA DEL WAITING TIME
	private void sumWait()
	{
		for(int i = 0; i < waitingQueue.size(); i++)
			waitingQueue.elementAt(i).set(3, Double.toString( Double.parseDouble(waitingQueue.elementAt(i).elementAt(3) ) + 0.5 ));
	}

	private void print(String running, double time, boolean last)
	{
		/*for(int i = 0; i < waitingQueue.size(); i++)
		{
			for(int j = 0; j < waitingQueue.elementAt(i).size(); j++)
			{
				System.out.print(waitingQueue.elementAt(i).elementAt(j) + " ");
			}
			System.out.print("\n");
		}*/
		if(last)
		{
			System.out.println("----------" + time);
			System.out.println(running);
		}
		else
		{
			//AQUI SE MUESTRA LOS WAITING TIME DE CADA PROCESO Y EL CALCULO PROMEDIO DEL WAITING TIME
			double totalTime = 0;

			for (int i = 0; i < finishedProcess.size(); i++) 
			{
				System.out.println("Tiempo del proceso " + finishedProcess.elementAt(i).elementAt(0) + " es de " + finishedProcess.elementAt(i).elementAt(3));	
				totalTime += Double.parseDouble(finishedProcess.elementAt(i).elementAt(3));
			}

			System.out.println("\n El tiempo promedio de espera es de: " + totalTime/finishedProcess.size());
		}
	}
}



