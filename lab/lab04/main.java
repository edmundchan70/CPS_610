package lab04;

import java.util.ArrayList;
import java.util.List;

public class main {

	public static void main(String[] args) {
		Transaction T1 = new Transaction(new ArrayList<Operation>(){{
			add(new Operation(Action.WRITE, "1", 5 ));
			add(new Operation(Action.READ, "2"));
			add(new Operation(Action.WRITE, "2", 3 ));
			add(new Operation(Action.READ, "1"));
			add(new Operation(Action.COMMIT));
		}});
		
		Transaction T2 = new Transaction(new ArrayList<Operation>(){{
			add(new Operation(Action.READ, "1"));
			add(new Operation(Action.WRITE, "1", 2));
			add(new Operation(Action.COMMIT));
		}});
		
	
	
	}
				
}


