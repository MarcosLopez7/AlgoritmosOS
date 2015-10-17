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

		for(float i = 0.0; !processList.isEmpty() || !waitingQueue.isEmpty(); i += 0.5)
		{
			
		}
	}
}



