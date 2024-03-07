package lab03Code;
import java.util.ArrayList;
import java.util.List;

public class main {

    public static ArrayList<Schedule> createSchedule(ArrayList<Transaction> transactions) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        ArrayList<ArrayList<Transaction>> permutations = permute(transactions);
        for (ArrayList<Transaction> perm : permutations) {
            Schedule schedule = new Schedule();
            for (Transaction transaction : perm) {
                schedule.addSchedule(transaction);
            }
            schedules.add(schedule);
        }
        return schedules;
    }

    
    
    public static ArrayList<ArrayList<Transaction>> permute(ArrayList<Transaction> transactions) {
        ArrayList<ArrayList<Transaction>> result = new ArrayList<>();
        permuteHelper(result, transactions, 0);
        return result;
    }

    private static void permuteHelper(ArrayList<ArrayList<Transaction>> result, ArrayList<Transaction> transactions, int index) {
        if (index >= transactions.size()) {
            result.add(new ArrayList<>(transactions));
            return;
        }
        for (int i = index; i < transactions.size(); i++) {
            swap(transactions, index, i);
            permuteHelper(result, transactions, index + 1);
            swap(transactions, index, i);
        }
    }

    private static void swap(ArrayList<Transaction> transactions, int i, int j) {
        Transaction temp = transactions.get(i);
        transactions.set(i, transactions.get(j));
        transactions.set(j, temp);
    }
    
    public static ArrayList<Transaction> Qb() {
    	Transaction T1 = new Transaction(new ArrayList() {{
    		add("readX");
    		add("writeX");
    		add("readY");
    		add("writeY");
    	}}, "T1" );
    	
    	Transaction T2 = new Transaction(new ArrayList() {{
    		add("readZ");
    		add("readY");
    		add("writeY");
    		add("readX");
    		add("writeX");
    	}}, "T2" );
    	
    	Transaction T3 = new Transaction(new ArrayList() {{
    		add("readY");
    		add("readZ");
    		add("writeY");
    		add("writeZ");
    	}}, "T3" );
    	
    	return new ArrayList<Transaction>() {{
    		add(T1);
    		add(T2);
    		add(T3);
    	}};
    }


    // Your other methods and classes remain unchanged

    public static void main(String[] args) {
        ArrayList<Transaction> listOfTransaction = Qb();
        ArrayList<Schedule> schedules = createSchedule(listOfTransaction);
        /*
        for (int i = 0; i < schedules.size(); i++) {
        	System.out.println("\n\n\n");
        	System.out.println("schedule #: " +  i);
            System.out.println(schedules.get(i));
        }
        
        System.out.println(schedules.size());
        */
        int counter = 1;
        for(Schedule s : schedules) {
            PrecedenceGraph graph = new PrecedenceGraph(s);
            graph.checkAlgo();
            System.out.println("In Schedule " + counter);
            System.out.println(graph);
            
            counter++;
        }
  
     
    }

    // Your other methods and classes remain unchanged
}


