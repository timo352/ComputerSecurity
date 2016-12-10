package CipherGUI;

public class FrequencyString {

	private String string;
	private int numTimesAppears;
        private double percentageUsed;

	public FrequencyString(String str) {
		string = str.toUpperCase();
		numTimesAppears = 0;
                percentageUsed = 0;
	}
	
	public void incrTimes(){
		numTimesAppears++;
	}
	
	public String string(){
		return string;
	}
	
	public int numTimesAppears(){
		return numTimesAppears;
	}
        
        public void setPercent(int size){
            percentageUsed = (double)numTimesAppears / (double)(size);
        }
        
        public double percentageUsed(){
            return percentageUsed;
        }
}
