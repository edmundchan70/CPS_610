package lab04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;



public class main {
	
	//RecordID : VALUE 
	public static Map<String, Integer> tempDB = new HashMap<String, Integer>(); 
	//Transactoin : PRVIOUS INDEX 
	public static Map<String, Integer> previousRecord = new HashMap<String, Integer>();
	
	public static Map<LockTableKey, LockType> LockTable  = new HashMap<LockTableKey, LockType>();
	public static ArrayList<String> logTable = new ArrayList<String>();
	
	
	public static int tableTimeStamp = 0;
	public static int tranSactionId = 0;
 
	
	private static int getOldData(String recordID) {
		if(tempDB.containsKey(recordID))
			return tempDB.get(recordID);
		return -100;
	}
	
	private static int assignPreviousRecord(String tName, int timeStamp) {
		//if exist, the current op should point to that index
		if(previousRecord.containsKey(tName)) {
			int prevRecord = previousRecord.get(tName);
			previousRecord.put(tName, timeStamp);
			return prevRecord;
		}
		 
		else 
			previousRecord.put(tName, timeStamp);
		return -1;
	}
	
	//return false if on hold
	private static boolean opSuccess(Operation op, String T) {
		//add share lock
		if (op.getAction() == Action.READ) {
			//If no lock existed on that record from any transactions, a Shared Lock can be acquired on it by the current transaction
			//if not locked by exclusive, check if we can sharelock it
			if (!waitExclusiveLock(op, T)) {
				if(lockShareLock(op, T)) {
					LockTable.put(new LockTableKey(op.getRecordId(), T), LockType.SHARE);
				} 
				String successRead = "";
				/** 
				 * Timestamp; can be a simple counter starting from 0 and incremented by 1 for each log entry
					- The Transaction Id
					- The Record Id
					- The value read from that record
					- The timestamp of the previous log entry for this transaction (to be used for Roll-back)
				 */
				successRead += ("R: " + tableTimeStamp  + ", "+ T + ", " + op.getRecordId() + ", "+ getOldData(op.getRecordId()) + ", " + assignPreviousRecord(T, tableTimeStamp));
				logTable.add(successRead);
				tableTimeStamp ++;//increment success log
				return true; //can go on next
			}
			return false; //on hold since waitExclusiveLock returns false;
		}
		else if (op.getAction()== Action.WRITE) {
			//case 1, share lock upgrade
			/**
	 
			 */
			LockTableKey existLockOnTran = getOpLockTypeAndTransaction(op);
			LockType lockType = LockTable.get(existLockOnTran) ;
			String successWrite = "";
			/** 
			 * Timestamp; can be a simple counter starting from 0 and incremented by 1 for each log entry
				- The Transaction Id
				- The Record Id
				- The value read from that record
				- The timestamp of the previous log entry for this transaction (to be used for Roll-back)
			 */
			successWrite += ("W: " + tableTimeStamp + ", "+ T +  ", " + op.getRecordId() + ", "+ getOldData(op.getRecordId()) +  ", "+ op.getValue() + ", " + assignPreviousRecord(T, tableTimeStamp));
			//case 1 , no lock at all, add exlcusive lock
			if (existLockOnTran == null) {
				LockTable.put(new LockTableKey(op.getRecordId(), T), LockType.EXCLUSIVE); 
				tempDB.put(op.getRecordId(), op.getValue());
				logTable.add(successWrite);
				tableTimeStamp ++;
				return true;
			}
			//case 2, share lock upgrade if share lock by same transaction
			else if (lockType == LockType.SHARE) {
				LockTable.put(existLockOnTran, LockType.EXCLUSIVE); 
				tempDB.put(op.getRecordId(), op.getValue());
				logTable.add(successWrite);
				tableTimeStamp ++;
				return true; //opSuccess no need to hold
			}
			//case 3, if lock exlusive by same transaction, skip and proceed
			else if(lockType == LockType.EXCLUSIVE && T.equals(existLockOnTran.getTName())) {
				tempDB.put(op.getRecordId(), op.getValue());
				logTable.add(successWrite);
				tableTimeStamp ++;
				return true; 
			}
			//for every other case , like op locked by shared lock/ exclusive lock, wait
			return false;
		}
		else {
			LockTable.clear();
			String successCommit = "";
			successCommit += "C:"+  tableTimeStamp +  ", " + T +  ", "  + assignPreviousRecord(T, tableTimeStamp);
			 
			logTable.add(successCommit);
			tableTimeStamp ++;
			return true;
		}
	}
	//if it exist a locktype on the lockTable, return the locktype
	private static LockTableKey getOpLockTypeAndTransaction(Operation op) {
		for (LockTableKey key : LockTable.keySet()) {
			//same reord  && same TName 
			if (key.getRecordId().equals(op.getRecordId())) {
				return key;
			}
		}
		return null;
	}

	
	//if the current transaction can lock a share lock, return true
	// for share lock, if record is sharelocked by either itself or other, cannot re-lock
	private static boolean lockShareLock(Operation op, String T) {
		for (LockTableKey i : LockTable.keySet()) {
			if(i.getRecordId().equals(op.getRecordId())) { 
				return false; 
			}
		}
		return true;
	}
	
	//if locked by exclusive lock, return true for wait 
	private static boolean waitExclusiveLock(Operation op, String T) {
		for (LockTableKey i : LockTable.keySet()) {
			//if we can find the RecordId 
			if(i.getRecordId().equals(op.getRecordId())) { 
				//if locked by other exlusive. skip
				if (LockTable.get(i) == LockType.EXCLUSIVE && !T.equals(i.getTName()))  
					return true; 
					
			}
		}
		return false;
	}
	
	public static String printLockTable() {
		 StringBuilder result = new StringBuilder("{");
	        for (Map.Entry<LockTableKey, LockType> entry : LockTable.entrySet()) {
	            LockTableKey key = entry.getKey();
	            LockType value = entry.getValue();
	            result.append("(")
	                    .append("Transaction: ").append(key.getTName())
	                    .append(", Record ID: ").append(key.getRecordId())
	                    .append(", Lock Type: ").append(value)
	                    .append("), ");
	        }
	        // Remove the trailing comma and space
	        if (!LockTable.isEmpty()) {
	            result.setLength(result.length() - 2);
	        }
	        result.append("}");
	        return result.toString();
	  }
	
	public static void run2PL(List<Transaction> transactions,  List<Queue<Operation>> queues) {
		int round = 1; // round starts with 1
        boolean finishAllTran = false;

        // round robin
        while (!finishAllTran) {
            finishAllTran = true;
            System.out.print("Round: " + round + "-> ");
            for (int i = 0; i < transactions.size(); i++) {
                Queue<Operation> queue = queues.get(i);
                if (!queue.isEmpty()) {
                    Operation operation = queue.peek(); // peek operation from queue
                    boolean opSuccess = opSuccess(operation, transactions.get(i).getName());
                    if (opSuccess) {
                        System.out.print( transactions.get(i).getName() + " " +operation.toString() + " ");
                        queue.poll(); // remove operation from queue if successful
                    } else {
                        System.out.print("Record ID: " + operation.getRecordId() + " is locked. Operation: " + transactions.get(i).getName() + " " + operation.toString() + " Hold");
                    }
                }
            }	
            System.out.println();
            System.out.print("ROUND : " + round + " Lock table : " + printLockTable());
            System.out.println();
            //checking if all action clear
            for ( Queue<Operation> queue  : queues) {
            	if(!queue.isEmpty())
            		finishAllTran  = false;
            }
            round++;
            System.out.println();
            System.out.println();
 
        }
	}
	public static void part1() {
		   List<Transaction> transactions = new ArrayList<>();
	        transactions.add(new Transaction(
	                Arrays.asList(
	                        new Operation(Action.WRITE, "1", 5),
	                        new Operation(Action.COMMIT)
	                ), "T1"));
	        transactions.add(new Transaction(
	                Arrays.asList(
	                        new Operation(Action.READ, "9"),
	                        new Operation(Action.READ, "7"),
	                        new Operation(Action.COMMIT)
	                ), "T2"));
	        transactions.add(new Transaction(
	                Arrays.asList(
	                        new Operation(Action.READ, "1"),
	                        new Operation(Action.COMMIT)
	                ), "T3"));

	        // Create queues for each transaction
	        List<Queue<Operation>> queues = new ArrayList<>();
	        for (Transaction transaction : transactions) {
	            Queue<Operation> queue = new LinkedList<>(transaction.getOperationList());
	            queues.add(queue);
	        }

	        run2PL(transactions, queues);
	        

	        System.out.println();
	        System.out.println();
	        System.out.println("LOG TABLE: ");
	        for (String i : logTable) {
	        	System.out.println(i);
	        }
	}
	public static void part2() {
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
	        
	        run2PL(transactions, queues);
	        
	        System.out.println();
	        System.out.println();
	        System.out.println("LOG TABLE: ");
	        for (String i : logTable) {
	        	System.out.println(i);
	        }
	}
	
	public static void main(String[] args) {
        // Create transactions
		//part1();
        part2();
    }

	 
				
}


