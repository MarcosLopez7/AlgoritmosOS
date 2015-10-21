import java.io.*;
import java.util.*;

public class ShortJob
{
	private File file = null;
    private FileReader reader = null;
    private BufferedReader buffer = null;
	private Vector<Vector<String>> processList = new Vector<Vector<String>>(0);	
	private Vector<Vector<String>> waitingQueue = new Vector<Vector<String>>(0);
	private Vector<Vector<String>> finishedProcess = new Vector<Vector<String>>(0);

	public void readFile()
	{	
		try
		{
       		        file = new File ("procesos.txt");
                	reader = new FileReader(file);
                	buffer = new BufferedReader(reader);

			String line = buffer.readLine();
			
			while(line != null)
                	{
                		line = buffer.readLine();       		
		
			 	StringTokenizer token = new StringTokenizer(line, " ");
				Vector<String> temporalVector = new Vector<String>(0);                        

				for(int i=0; i<3; i++)
                        	{
     					temporalVector.addElement(token.nextToken());
	                   	}
					temporalVector.addElement("0.0");
					
				processList.addElement(temporalVector);
                	}//cierre while


		}// cierre try
        	catch(Exception e)
		{

        	}//cierre catch
			
	}

	public void algorithm()
	{
		Vector<String> runningProcess = null;
		Vector<String> nextProcess = null;

		for(double i = 0.0; !processList.isEmpty() || !waitingQueue.isEmpty() || runningProcess != null; i += 0.5)
		{
			nextProcess = processReady(i);

			if(runningProcess == null)
			{
				runningProcess = (Vector<String>)nextProcess.clone();
                                nextProcess = null;

			}
			else
			{
				if(runningProcess.elementAt(2).equals("0.0") && nextProcess != null)
				{
					if(waitingQueue.isEmpty())
					{
						finishedProcess.addElement((Vector<String>)runningProcess.clone());
						runningProcess = (Vector<String>)nextProcess.clone();
						nextProcess = null;
					}
					else
					{
						if(Double.parseDouble(waitingQueue.elementAt(0).elementAt(2)) > Double.parseDouble(nextProcess.elementAt(2)))
						{
							finishedProcess.addElement((Vector<String>)runningProcess.clone());
							runningProcess = (Vector<String>)nextProcess.clone();
	                                        	nextProcess = null;
						}
						else
						{
							finishedProcess.addElement((Vector<String>)runningProcess.clone());
							runningProcess = (Vector<String>)waitingQueue.firstElement().clone();
	                                        	waitingQueue.removeElementAt(0);
							waitingQueue.addElement((Vector<String>)nextProcess.clone());
		                                        sort();
						}
					}
				}
				else if(nextProcess != null)
				{
					if(Double.parseDouble(runningProcess.elementAt(2)) > Double.parseDouble(nextProcess.elementAt(2)))
					{	
						waitingQueue.addElement((Vector<String>)runningProcess.clone());
						runningProcess =(Vector<String>)nextProcess.clone();
						nextProcess = null;
						sort();
					}
					else
					{
						waitingQueue.addElement((Vector<String>)nextProcess.clone());
						nextProcess = null;
						sort();
					}
				}
				else if(runningProcess.elementAt(2).equals("0.0"))
				{
					if(waitingQueue.isEmpty())
					{
						finishedProcess.addElement((Vector<String>)runningProcess.clone());
						runningProcess = null;
					}
					else
					{
						finishedProcess.addElement((Vector<String>)runningProcess.clone());
						runningProcess = (Vector<String>)waitingQueue.firstElement().clone(); 
						waitingQueue.removeElementAt(0);
					}
				}
			}

			if(runningProcess != null)
			{	
				runningProcess.set(2, Double.toString( Double.parseDouble(runningProcess.elementAt(2) ) - 0.5 ));
				sumWait();
				print(runningProcess.elementAt(0), i, true);
			}
			else
			{
				System.out.println("----------" + i);
				print(null, 0, false);
			}

			try 
			{
			    Thread.sleep(100);                 
			} 
			catch(InterruptedException ex) 
			{
    			Thread.currentThread().interrupt();
			}	
		}
	}

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



