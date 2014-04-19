package org.ToMar.Utils;

/**
 *
 * @author marie
 */
public class tmLog
{
	public static final int PROD = 0;			// only error messages will show
	public static final int TEST = 1;			// error and display messages will show
	public static final int TRACE = 2;			// error and display and debug messages will show
	private int errorLevel = 0;

	public tmLog() {}
	public tmLog(int errorLevel)
	{
		this.errorLevel = errorLevel;
	}
	public void error(String s)
	{
		log("(E) " + s);
	}
	public void display(String s)
	{
		if (errorLevel > PROD)
		{
			log("(D) " + s);
		}
	}
	public void debug(String s)
	{
		if (errorLevel > TEST)
		{
			log("(T) " + s);
		}
	}
	private void log(String s)
	{
		System.out.println(Functions.getDateTimeStamp()+ ": " + s);
	}
	public int getErrorLevel()
	{
		return errorLevel;
	}
	public void setErrorLevel(int errorLevel)
	{
		this.errorLevel = errorLevel;
	}




}
