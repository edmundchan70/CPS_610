package lab04;

public class LockTableKey {
	private String TName;
	private String recordId;
	
	public LockTableKey(String recordId, String TName) {
		this.TName = TName; 
		this.recordId = recordId;
	}
	
	public String getTName() {
		return TName;
	}
 
	public String getRecordId() {
		return recordId;
	}
 
	
}
