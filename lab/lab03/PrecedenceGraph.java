package lab03;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.alg.cycle.CycleDetector;
public class PrecedenceGraph {
	
	SimpleDirectedGraph<String, DefaultEdge> graph = new SimpleDirectedGraph(DefaultEdge.class);
	
	ArrayList<Transaction> schedule = new ArrayList<Transaction>();
	
	public PrecedenceGraph(Schedule schedule) {
		  //1 Add vertices
		
		for(Transaction t : schedule.transactionList) {
				graph.addVertex(t.name);	
		}
 		this.schedule = schedule.transactionList;
	}
	/*
	 * For each case in S where Tj exe readItemX AFTER  Ti write item, Ti -> Tj edge
	 * Should be called when current operation is readitem
	 * */
	
	public void checkAlgo() {
		for(Transaction ti : schedule) {
			for(String operation : ti.operationList) {
				// checkRule1(ti, operation);
				if(operation.contains("write")) {
					checkRule1(ti, operation);
					checkRule3(ti,operation);
				}
				if(operation.contains("read")) {
					checkRule2(ti, operation);
				}
				 
			}
		}
	}
	public String toString() {
		String result = "";
        result += ("Graph vertices: " + graph.vertexSet());
        result +=("\nGraph edges: " + graph.edgeSet());
        result += "\nShort notation for whole schedule:  ";
        for(Transaction i : schedule) {
        	for(String op : i.operationList) {
        		String shortForm = "";
        		
        		if(op.contains("read")) 
        			shortForm += "r" ;
        		 else if (op.contains("write"))
        			shortForm += "w" ;
        		
        		shortForm += i.name.substring(1) + "(";
        		 
        		shortForm += op.charAt(op.length()-1) + ")"; //the variable 
        		
        		result += shortForm;
        	}
        }
        result +="\n";
        //Add cycle detector and say it's serializable 
        if (checkCycle()) 
        	result += "Cycle happened. This graph is not serializable";
        else 
        	result += "No Cycle detected. This graph is serializabnle";
        return result;
	}
	
	public boolean checkCycle() {
		CycleDetector cyDetector = new CycleDetector(graph);
		return cyDetector.detectCycles();
	}
	
	private void checkRule1(Transaction ti, String operation) {
		//I just want to go through the transaction after Ti to check T1 
		int indexOfTi = schedule.indexOf(ti);
		String tiOperation = "write"+ operation.charAt(operation.length()-1);
		//check if Ti has write() inside transaction, if not contain, pass rule #1
		if (!ti.containOperation(tiOperation)) return ;
		
		//only loop the transaction after Ti
		for(int i = indexOfTi+ 1; i < schedule.size(); i++) {
			 Transaction tj = schedule.get(i);
			 if(tj.containOperation(operation)) {
				// System.out.println(""+ ti.name +" " +tj.name);
				 graph.addEdge(ti.name, tj.name);
			 }
		}
	}
	
	//check if Tj execute writeX after Ti exec readItem, then Ti -> Tj
	//Only when Tj exectue write
	private void checkRule2(Transaction ti, String operation) {
		//I just want to go through the transaction after Ti to check T1 
		int indexOfTi = schedule.indexOf(ti);
		String tiOperation = "read"+ operation.charAt(operation.length()-1);
		//check if Ti has write() inside transaction, if not contain, pass rule #1
		if (!ti.containOperation(tiOperation)) return ;
		
		//only loop the transaction after Ti
		for(int i = indexOfTi+ 1; i < schedule.size(); i++) {
			 Transaction tj = schedule.get(i);
			 if(tj.containOperation(operation)) {
				// System.out.println(""+ ti.name +" " +tj.name);
				 graph.addEdge(ti.name, tj.name);
			 }
		}
	}
	private void checkRule3(Transaction ti, String operation) {
		//I just want to go through the transaction after Ti to check T1 
		int indexOfTi = schedule.indexOf(ti);
		String tiOperation = "write"+ operation.charAt(operation.length()-1);
		//check if Ti has write() inside transaction, if not contain, pass rule #1
		if (!ti.containOperation(tiOperation)) return ;
		
		//only loop the transaction after Ti
		for(int i = indexOfTi+ 1; i < schedule.size(); i++) {
			 Transaction tj = schedule.get(i);
			 if(tj.containOperation(operation)) {
				// System.out.println(""+ ti.name +" " +tj.name);
				 graph.addEdge(ti.name, tj.name);
			 }
		}
	}
	

}
