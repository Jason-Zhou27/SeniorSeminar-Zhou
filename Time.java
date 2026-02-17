public class Time {

	//variables
	private int month;
	private int day;
	private int year;
	private int hour;
	private int minute;
	private int second;
	//constructors
	public Time(int mon, int d, int y, int h, int min, int s) {
		month = mon;
		day = d;
		year = y;
		hour = h;
		minute = min;
		second = s;
	}	
	//methods
	public String toString(){
		String tToString = "\n";
		tToString = tToString + "month: " + month + "\n";
		tToString = tToString + "day: " + day + "\n";
		tToString = tToString + "year: " + year + "\n";
		tToString = tToString + "hour: " + hour + "\n";
		tToString = tToString + "minute: " + minute + "\n";
		tToString = tToString + "second: " + second + "\n";
		return tToString;
		
		
	}	




}
