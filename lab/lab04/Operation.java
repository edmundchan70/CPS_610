package lab04;

public class Operation {
	private Action action;
	private String recordId;
	private int value;
	
	public Operation (Action action, String recordId, int value) {
		if (action != Action.WRITE) 
	            throw new IllegalArgumentException("Invalid action for this constructor");
	            
		this.action = action;
		this.recordId = recordId;
		this.value = value;
	}
	
	public Operation (Action action, String recordId) {
		if (action != Action.READ) 
            throw new IllegalArgumentException("Invalid action for this constructor");
       
		this.action = action;
		this.recordId = recordId;
	}
	
	public Operation (Action action) {
		if(action != Action.COMMIT)
			 throw new IllegalArgumentException("Invalid action for this constructor");
	      this.action = action;
	}
	
	public Action getAction() {
		return action;
	}

	public String getRecordId() {
		return recordId;
	}

	 

	public int getValue() {
		return value;
	}

 
	
}
