import java.io.*;
import java.util.*;

public class ShortJob
{
	private File file = null;
        private FileReader reader = null;
        private BufferedReader buffer = null;
	private Vector<Vector<String>> processList = new Vector<Vector<String>>(0);	

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

				processList.addElement(temporalVector);
                	}//cierre while
		}// cierre try
        	catch(Exception e)
		{

        	}//cierre catch
			
	}

	public void algorithm()
	{
		Vector<Vector<String>> waitingQueue = new Vector<Vector<String>>(0);
		Vector<String> runningProcess = processList.firstElement();
		processList.removeElementAt(0);
		Vector<String> nextProcess = null;

		for(double i = 0.0; !processList.isEmpty() || !waitingQueue.isEmpty() || runningProcess != null; i += 0.5)
		{
			nextProcess = processReady(i);

			if(runningProcess.elementAt(2).equals("0.0") && nextProcess != null)
			{
				
			}
			else if(nextProcess != null)
			{
				if(Double.parseDouble(runningProcess.elementAt(2)) > Double.parseDouble(nextProcess.elementAt(2)))
				{	
					waitingQueue.addElement((Vector<String>)runningProcess.clone());
					runningProcess = nextProcess;
				}
				else
				{
					waitingQueue.addElement((Vector<String>)nextProcess.clone());
				}
			}
			else if(runningProcess.elementAt(2).equals("0.0"))
			{
				if(waitingQueue.isEmpty())
				{
					runningProcess = null;
				}
				else
				{
					runningProcess = waitingQueue.firstElement(); 
					waitingQueue.removeElementAt(0);
				}
			}
			else
			{
				runningProcess.setElementAt(Double.toString( Double.parseDouble(runningProcess.elementAt(2) ) - 0.5 ),2);
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
				process = processList.elementAt(i);
				break;
			}
		}

		return process;
	}

	private void sort (Vector<Vector<String>> queue)
	{
		for (int i = 0; i < queue.size(); i++)
        	{
                	for (int j = queue.size() - 1; j > i; j--)
                	{
                        	if (Double.parseDouble(queue.elementAt(j - 1).elementAt(2)) > Double.parseDouble(queue.elementAt(j).elementAt(2)))
				{
					 
				}
                	}
        	}
	}
}



