package org.ToMar.Utils;

import java.util.*;
/*
 * Created on Jul 2, 2004
 * Imported into NetBeans on 7/8/2013
 */
public class tmEvaluator
{
	protected static final String OPERS = "+-*/";
	protected static final String ADDSUB = "+-";
	protected static final String MULTDIV = "*/";
	protected static final String PARENS = "()";
	protected static final String NUMS = "0123456789";
	protected static final String OK = "OK";
	public static final double ERRORFLAG = 99999.99;
	protected ArrayList<String> args;
	protected int[] parenCounts = new int[2];
	protected String message;
	protected String str;
	protected boolean numberReceived;
	protected int multiplier = 0;

	public double evaluate(String str)
	{
		this.str = str.trim();
		return this.evaluate();
	}

	public double evaluate()
	{
		numberReceived = false;
		args = new ArrayList<>();
		parenCounts[0] = parenCounts[1] = 0;
		multiplier = 1;
		StringBuffer numberInProgress = null;
		boolean operFlag = false;
		setMessage("OK");
		for (int i = 0; i < str.length(); i++)
		{
			String currentChar = str.substring(i, i+1);
//			log("Processing: " + currentChar + ", multiplier is " + multiplier);
			if (" ".equals(currentChar))
			{
				numberInProgress = dumpNumber(numberInProgress, args);
				continue;
			}
			if (PARENS.indexOf(currentChar) > -1)
			{
				numberInProgress = dumpNumber(numberInProgress, args);
				parenCounts[PARENS.indexOf(currentChar)] += 1;
				args.add(currentChar);
			}
			else if (OPERS.indexOf(currentChar) > -1)
			{
				if (operFlag)
				{
					if (!("-".equals(currentChar)))
					{
						setMessage("Operator not followed by operand");
						return ERRORFLAG;
					}
					else
					{
						multiplier *= -1;
						continue;
					}
				}
				operFlag = true;
				numberInProgress = dumpNumber(numberInProgress, args);
				if (!numberReceived)
				{
					if (!("-".equals(currentChar)))
					{
						setMessage("Operator not followed by operand");
						return ERRORFLAG;
					}
					else
					{
						multiplier *= -1;
						continue;
					}
				}
				args.add(currentChar);
			}
			else if (NUMS.indexOf(currentChar) > -1)
			{
				operFlag = false;
				if (numberInProgress != null)
				{
					numberInProgress.append(currentChar);
				}
				else
				{
					numberInProgress = new StringBuffer(currentChar);
				}
			}
			else
			{
				setMessage("Non-numeric character " + currentChar + ".");
				return ERRORFLAG;
			}
		}
		numberInProgress = dumpNumber(numberInProgress, args);
		if (operFlag)
		{
			setMessage("Operator not followed by operand");
			return ERRORFLAG;
		}
		// check for balanced parens
		if (parenCounts[0] != parenCounts[1])
		{
			setMessage("Out of balance - " + parenCounts[0] +
					" left parens and " + parenCounts[1] +
			" right parens.");
			log(this.message);
			return ERRORFLAG;
		}
//		displayArgs();
		// process stuff inside parentheses
		if(parenCounts[0] > 0)
		{
			for (int lp = args.size() - 1; lp >= 0; lp--) //work back from the end looking for left paren
			{
				if (!message.equals(OK))
				{
					break;
				}
				if ((args.get(lp)).equals("("))
				{
					for (int rp = lp + 1; rp < args.size(); rp++)
					{
						if ((args.get(rp)).equals(")"))
						{
							args = processElements(lp, rp);
//                            displayArgs(2);
							break;
						}
					}
				}
			}
		}
		// process what's left - result should be the only element left
		while(args.size() > 1 && message.equals(OK))
		{
			args = processElements(0, args.size() - 1);
//			displayArgs(3);
		}
		if (!message.equals(OK))
		{
			return ERRORFLAG;
		}
		return makeDouble(args.get(0)) * multiplier;
	}
	protected void displayArgs()
	{
		for (int i = 0; i < args.size(); i++)
		{
			log("arg[" + i + "] = " + args.get(i));
		}
	}
	protected int getNextElement(int i)
	{
		while (true)
		{
			if (args.get(i) != null)
			{
//				log("getNextElement: i is " + i + ", element is " + args.get(i));
				return i;
			}
			if (++i == args.size())
			{
				return (int) ERRORFLAG;
			}
		}
	}
	protected double makeDouble(String s)
	{
		double value = 0;
		try
		{
			value = (new Double(s)).doubleValue();
			if (("" + value).equals("Infinity"))
			{
				setMessage(str + " is undefined");
				return ERRORFLAG;
			}
			value = (Math.abs(value - Math.round(value)) < .0000001) ? Math.round(value) : value;
		}
		catch(Exception e)
		{
			if (e.toString().equalsIgnoreCase("java.lang.NumberFormatException: Infinity"))
			{
				setMessage(str + " is undefined");
				return ERRORFLAG;
			}
			log("Exception processing expression " + str + ", argument " + s + ": " + e);
//			displayArgs(4);
		}
		return value;
	}
	protected ArrayList<String> processElements(int firstElement, int lastElement)
	{
//        log("processElements from " + firstElement + " to " + lastElement);
		double number1 = 0, number2 = 0, result = 0;
		int pos1 = 0, pos2 = 0;
		int numberOfElements = lastElement - firstElement + 1;
		int operatorFound = 0;
		int operandFound = 0;
		// first pass, process * and /
		for (int i = getNextElement(firstElement); i < lastElement + 1; i++)  // this is what i changed
		{
			String s = "" + args.get(i);
//			System.out.println("1 argument is " + s + ", i is " + i + ", numberOfElements is " + numberOfElements);
			if (s == null || ADDSUB.indexOf(s) > -1)	// skip + and -
			{
				continue;
			}
			if (PARENS.indexOf(s) > -1)	// null out parentheses
			{
                args.set(i, null);
//                log("paren nulled out at " + i);
				continue;
			}
			if (MULTDIV.indexOf(s) == -1) // it's a number if true
			{
				number1 = makeDouble(s);
				pos1 = i;
				operandFound++;
//				log("operand " + s);
			}
			else
			{
				operatorFound++;
//				log("operator " + s);
				pos2 = getNextElement(i + 1);
				number2 = makeDouble(args.get(pos2));
				if (s.equals("*"))
				{
					result = processMultiply(number1, number2);
				}
				else
				{
					if (number2 == 0.0)
					{
						result = ERRORFLAG;
						message = "Division by zero";
						break;
					}
					else
					{
						result = processDivide(number1, number2);
					}
				}
				args.set(pos1, "" + result);
				for (i = pos1 + 1; i < pos2 + 1; i++)
				{
					args.set(i, null);
					numberOfElements -= 1;
				}
				i--;
				number1 = result;
			}
		}
  //      displayArgs(5);
		if (!message.equals(OK))
		{
			return new ArrayList<>();
		}
		// second pass, process + and -
		for (int i = getNextElement(firstElement); numberOfElements > 1 && i < lastElement + 1; i++)
		{
			String s = args.get(i);
//			System.out.println("2 argument is " + s + ", i is " + i + ", numberOfElements is " + numberOfElements);
			if (s == null)	// skip parentheses, + and -
			{
				continue;
			}
			if (OPERS.indexOf(s) == -1) // it's a number if true
			{
				number1 = makeDouble(s);
				pos1 = i;
				operandFound++;
//				log("operand " + s);
			}
			else
			{
				operatorFound++;
//				log("operator " + s);
				pos2 = getNextElement(i + 1);
				number2 = makeDouble(args.get(pos2));
				if (s.equals("-"))
				{
					result = processSubtract(number1, number2);
				}
				else
				{
					result = processAdd(number1, number2);
				}
				args.set(pos1, "" + result);
				for (i = pos1+1; i < pos2 + 1; i++)
				{
					args.set(i, null);
					numberOfElements -= 1;
				}
				i = (getNextElement(i - 1) - 1);
				number1 = result;
			}
		}
		if (!message.equals(OK))
		{
			return new ArrayList<>();
		}
		ArrayList<String> v = new ArrayList<>();
		for (int i = 0; i < args.size(); i++)
		{
			if (args.get(i) != null)
			{
				v.add(args.get(i));
			}
		}
//		log("number of args = " + args.size() + ", operators = " + operatorFound + ", operands = " + operandFound);
		if (args.size() > 1 && (operandFound == 0 || operatorFound == 0))
		{
			message = "Missing operator or operand";
			return new ArrayList<>();
		}
		return v;
	}
	protected double processAdd(double a, double b)
	{
		return a + b;
	}
	protected double processSubtract(double a, double b)
	{
		return a - b;
	}
	protected double processMultiply(double a, double b)
	{
		return a * b;
	}
	protected double processDivide(double a, double b)
	{
		return a / b;
	}
	protected StringBuffer dumpNumber(StringBuffer numberInProgress, ArrayList<String> args)
	{
		if (numberInProgress != null)
		{

//			log("dumping " + numberInProgress);
			args.add(numberInProgress.toString());
			numberInProgress = null;
			numberReceived = true;
		}
		return numberInProgress;
	}
	protected void log(String s)
	{
		System.out.println(s);
	}
	/**
	 * @return Returns the message.
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	public static void main(String[] args)
	{
//      String a = "(3 * 18 + 18)/3";
//      String a = "((3 * 18) + 18)/3";
//      String a = "(18 * 3 + 18)/3";
//      String a = "(18 + 3 * 18)/3";
	    String[] s = {"(18 + (3 * 18))/3", "(18 * 3 + 18)/3", "((3 * 18) + 18)/3", "(3 * 18 + 18)/3"};
        for (int a = 0; a < 4; a++)
        {
    		System.out.println("Running " + s[a]);
            tmEvaluator eval = new tmEvaluator();
            System.out.println("Evaluates to " + eval.evaluate(s[a]));
            System.out.println(eval.getMessage());
        }
	}
}
