package lab04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LogTable {

	public static void main(String[] args) {
		 List<Transaction> transactions = new ArrayList<>();

	        // Schedule 1: T1:W(1,5);T1:R(2);T1:W(2,3);T1:R(1);T1:C;
	        Transaction t1 = new Transaction(Arrays.asList(
	                new Operation(Action.WRITE, "1", 5),
	                new Operation(Action.READ, "2"),
	                new Operation(Action.WRITE, "2", 3),
	                new Operation(Action.READ, "1"),
	                new Operation(Action.COMMIT)
	        ), "T1");
	        transactions.add(t1);

	        // Schedule 2: T2:R(1);T2:W(1,2);T2:C
	        Transaction t2 = new Transaction(Arrays.asList(
	                new Operation(Action.READ, "1"),
	                new Operation(Action.WRITE, "1", 2),
	                new Operation(Action.COMMIT)
	        ), "T2");
	        transactions.add(t2);
	        // Create queues for each transaction
	        List<Queue<Operation>> queues = new ArrayList<>();
	        for (Transaction transaction : transactions) {
	            Queue<Operation> queue = new LinkedList<>(transaction.getOperationList());
	            queues.add(queue);
	        }
	}
	
	 

}
