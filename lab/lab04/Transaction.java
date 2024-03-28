package lab04;

import java.util.List;

public class Transaction {
	private String name;
	private List<Operation> operationList;

	public Transaction (List<Operation> operationList, String name) {
		this.operationList = operationList;
		this.name = name;
	}
	
	
	public List<Operation> getOperationList() {
		return operationList;
	}


	public String getName() {
		return name;
	}
	
	
}
