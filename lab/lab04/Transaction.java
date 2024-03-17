package lab04;

import java.util.List;

public class Transaction {
	private List<Operation> operationList;

	public Transaction (List<Operation> operationList) {
		this.operationList = operationList;
	}
	
	
	public List<Operation> getOperationList() {
		return operationList;
	}
	
	
}
