package org.ToMar.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;
// VERSION 1.3
// June, 2010
// 1.3 adds a method that returns an image if you give it a url, pointer to an applet, and size
// June, 2013 - importing into NetBeans for use with applications

public class Functions
{
	public static tmLog log = new tmLog(tmLog.PROD);

	public static long getElapsedSeconds(long startSec)
	{
		return ((new Date()).getTime() - startSec)/1000;
	}
	public static long getMilliSeconds(long startSec)
	{
		return ((new Date()).getTime() - startSec);
	}
	public static String displayTime(long seconds)
	{
		// format seconds into 00:00
		int hrs = (int) seconds/3600;
		int mins = (int) ((seconds/60) % 60);
		int secs = (int) (seconds % 60);
		return (formatNumber(hrs,2) + ":" + formatNumber(mins,2) + ":" + formatNumber(secs,2));
	}
	public static int getRnd(int value)
	{
		return (int)(Math.random() * value);
	}

	public static String getNiceDate(Calendar sysDate)
	{
		String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		String[] months = {"January", "February", "March", "April", "May", "June", "July",
				"August", "September", "October", "November", "December"};
		String dayOfWeek = days[sysDate.get(Calendar.DAY_OF_WEEK) - 1];
		String month = months[sysDate.get(Calendar.MONTH)];
		String time = formatNumber(sysDate.get(Calendar.HOUR_OF_DAY), 2) + ":" +
		formatNumber(sysDate.get(Calendar.MINUTE), 2) + ":" +
		formatNumber(sysDate.get(Calendar.SECOND), 2) + " (E";
		if (sysDate.get(Calendar.DST_OFFSET) > 0)
		{
			time += "DT)";
		}
		else
		{
			time += "ST)";
		}
		return (dayOfWeek + ", " + month + " " + sysDate.get(Calendar.DATE) + ", " +
				sysDate.get(Calendar.YEAR) + " at " + time);
	}
	public static String formatNumber(int numberIn, int numberOfDigits)
	{
		String numberOut = "000000000" + numberIn;
		return numberOut.substring(numberOut.length() - numberOfDigits);
	}
	public static String formatDecimals(double numberIn,int numberOfDecimals)
	{
		String num = "" + numberIn;
		String beginning = "";
		String decimals = "";
		int len = num.length();
		int dec = num.indexOf(".");
		if (dec == -1)
		{
			return formatNumber((int) numberIn, len);
		}
		else if (dec > 0)
		{
			beginning = num.substring(0, dec);
		}
		decimals = num.substring(dec + 1) + "000000000";
		String decDigits = decimals.substring(0, numberOfDecimals);
		if ("56789".indexOf(decimals.substring(numberOfDecimals, numberOfDecimals + 1)) > -1)
		{
			if (numberOfDecimals == 0)
			{
				int begInt = Integer.parseInt(beginning) + 1;
				beginning = "" + begInt;
			}
			else
			{
				int decInt = Integer.parseInt(decDigits);
				decDigits = formatNumber(decInt + 1, numberOfDecimals);
			}
		}
		if (numberOfDecimals == 0)
		{
			return beginning;
		}
		return (beginning + "." + decDigits);
	}
	public static void arraySort(int[] arrayIn)
	{
		boolean flips = true;
		while (flips == true)
		{
			flips = false;
			for (int i = 0; i < arrayIn.length - 1; i++)
			{
				if (arrayIn[i] > arrayIn[i + 1])
				{
					int temp = arrayIn[i];
					int temp1 = arrayIn[i + 1];
					arrayIn[i] = temp1;
					arrayIn[i + 1] = temp;
					flips = true;
				}
			}
		}
	}
	public static String[] arraySort(String[] arrayIn)
	{
		boolean flips = true;
		while (flips == true)
		{
			flips = false;
			for (int i = 0; i < arrayIn.length - 1; i++)
			{
				if (arrayIn[i].compareTo(arrayIn[i + 1]) > 0)
				{
					String temp = arrayIn[i];
					String temp1 = arrayIn[i + 1];
//					log("flipping " + i + ": " + temp + " and " + temp1);
					arrayIn[i] = temp1;
					arrayIn[i + 1] = temp;
					flips = true;
				}
			}
 		}
		return arrayIn;
	}
	public static int[] randomPicks(int universeSize, int numberToPick)
	{
        int[] returnArray = new int[numberToPick];
		StringBuffer pickString = new StringBuffer("");
		for (int i = 0; i < universeSize; i++)
		{
			pickString.append(Functions.formatNumber(i,5));
		}
//        log("Functions.randompicks: pickString length is " + pickString.length());
		for (int i = 0; i < numberToPick; i++)
		{
			int idx = (Functions.getRnd(pickString.length()/5)) * 5; // index on pickString
//            log("Functions.randompicks: idx is " + idx);
        	returnArray[i] = Integer.parseInt((pickString.toString()).substring(idx, idx + 5));
        	pickString = new StringBuffer((pickString.toString()).substring(0, idx) + (pickString.toString()).substring(idx + 5));
        }
		return returnArray;
	}
    public static void arraySort(Object[] arrayIn, String methodIn)
	{
		boolean flips = true;
		while (flips)
		{
			String a = null;
			String b = null;
			flips = false;
			for (int i = 0; i < arrayIn.length - 1; i++)
			{
				try
				{
					a = (String) arrayIn[i].getClass().getMethod(methodIn, (Class<?>) null).invoke(arrayIn[i], (Object[]) null);
					b = (String) arrayIn[i+1].getClass().getMethod(methodIn, (Class<?>) null).invoke(arrayIn[i+1], (Object[]) null);
				}
				catch (Exception e)
				{
					System.out.println("ERROR!!! a = " + a + ", b = " + b + ", Error: " + e);
					break;
				}
				if (a.compareTo(b) > 0)
				{
					Object temp = arrayIn[i];
					Object temp1 = arrayIn[i + 1];
					arrayIn[i] = temp1;
					arrayIn[i + 1] = temp;
					flips = true;
				}
//				System.out.println("a = " + a + ", b = " + b + ", flips is " + flips);
			}
		}
	}
	public static String getDateTimeStamp()
	{
		Calendar sysDate = Calendar.getInstance();
		String month = formatNumber(sysDate.get(Calendar.MONTH) + 1, 2);
		String time = formatNumber(sysDate.get(Calendar.HOUR_OF_DAY), 2) +
			formatNumber(sysDate.get(Calendar.MINUTE), 2) + formatNumber(sysDate.get(Calendar.SECOND), 2);
		return (sysDate.get(Calendar.YEAR) + month + formatNumber(sysDate.get(Calendar.DATE),2) + time);
//		return sysDate.toString();
	}
	public static int absoluteValue(int numIn)
	{
		if (numIn < 0)
		{
			return numIn * -1;
		}
		return numIn;
	}
	public static String reverseString(String input)
	{
		StringBuffer sb = new StringBuffer("");
		for (int i = input.length() - 1; i > -1; i--)
		{
			sb.append(input.substring(i, i + 1));
		}
		return sb.toString();
	}
	public static int arrayListToTextFile(String fileName, ArrayList<String> al)
	{
		int counter = 0;
		try
		{
	        File file = new File(fileName);
			try (Writer output = new BufferedWriter(new FileWriter(file)))
			{
				for (int i = 0; i < al.size(); i++)
				{
					output.write(al.get(i) + "\n");
					counter += 1;
				}
			}
	    }
		catch(Exception e)
		{
			log.error("org.ToMar.Utils.Functions.arrayListToTextFile Exception = " + e);
			counter = 0;
		}
		return counter;
	}
	public static ArrayList<String> textFileToArrayList(String fileName)
	{
		ArrayList<String> al = new ArrayList<>();
		String s;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			while (true)
			{
				s = br.readLine();
				if  (s == null)
				{
					break;
				}
				al.add(s);
			}
		}
		catch   (Exception e)
		{
			log.error("org.ToMar.Utils.Functions.textFileToArrayList Exception = " + e);
		}
		return al;
	}
    public static void main(String[] args)
    {
//        int[] picks = Functions.randomPicks(10, 5);
//		String[] aa = {"Rat", "Against","Aardvark", "Settle", "Piece", "News", "Red","Tar","Any","Xx"};
//		ArrayList<String> al = new ArrayList<>();
//		for (int i = 0; i< aa.length; i++)
//		{
//			al.add(aa[i] + "\n");
//		}
//		int count = arrayListToTextFile("test1.txt", al);
		ArrayList al = textFileToArrayList("test1.txt");
		for (int i = 0; i< al.size(); i++)
		{
			System.out.println(al.get(i));
		}
    }
}