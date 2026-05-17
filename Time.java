/**
 * Time.java
 * @author Jason Zhou
 * @since (date) 02/09/2026
 * This class outlines time behaviors and attributes
*/
/*
 * Time class defines characteristics of time objects (month, day, year, hour, minute, second)
 * and functions of the time objects (toString)
*/
public class Time {

	//instance variables
	private int month;
	private int day;
	private int year;
	private int hour;
	private int minute;
	private int second;
	//constructors
	/*
	 * Time constructor intializes month, day, year, hour, minute, second with abbreviated parameters associated
	 * with the values (mon, d, y, h, min, s)
	*/
	public Time(int mon, int d, int y, int h, int min, int s) {
		month = mon;
		day = d;
		year = y;
		hour = h;
		minute = min;
		second = s;
	}	
	//methods
	/*
	 * toString method prints out the attributes of time (month, day, year, hour, minute, second); String return
	*/
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
