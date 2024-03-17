package lab03;
import java.util.ArrayList;

class Transaction {
	ArrayList<String> operationList;
	String name ="";
	
	public Transaction(String TName) {
		operationList = new ArrayList<String>();
		name = TName; 
	} 
	public Transaction(ArrayList<String> hardcodeList, String TName) {
		operationList = hardcodeList;
		name = TName; 
	}
	public void addTransaction(String operation) {
		operationList.add(operation);
	}
	
	public boolean containOperation(String operation) {
		return operationList.contains(operation);
	}
	
	public String toString() {
		String result = "------Transaction " + name + "------\n";
		for(int i = 0; i < operationList.size(); i++) {
			result += "Operation " + i + ": " + operationList.get(i) + "\n"; 
		}
		//result += "------END of transaction " + name+  "------";
		return result;
	}
}

 