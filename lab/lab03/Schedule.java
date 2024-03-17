package lab03;

import java.util.ArrayList;

class Schedule {
	ArrayList<Transaction> transactionList;	
	
	public Schedule() {
		transactionList = new ArrayList<Transaction>();
	}
	public void addSchedule(Transaction transaction) {
		transactionList.add(transaction);
	}
	public String toString() {
		String result = "********Schedule*******\n";
		for (Transaction t : transactionList) {
			result += t + "\n";
		}
		result += "********Schedule END*****\n\n";
		return result;
	}
}