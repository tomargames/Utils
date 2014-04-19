package org.ToMar.Utils;

/**
 *
 * @author marie
 */
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Component;

/*
 * Created on Mar 20, 2005
 * Imported into NetBeans on 6/6/2013
 */
public class tmButton extends Component
{
    protected int x = 100;
    protected int y = 100;
    protected int xLabel = 10;
    protected int yLabel = 25;
    protected int width = 50;
    protected int height = 40;
    protected int fontSize = 14;
    protected String fontFamily = "Droid Sans";
    protected String label = "button";
    protected Font font;
    protected Color bgColor = tmColors.LIGHTGRAY;
    protected Color fgColor = tmColors.BLACK;

    public tmButton()
    {
         font = new Font(fontFamily, Font.BOLD, fontSize);
    }

    public tmButton (int x, int y, int width, String label)
    {
		this.x = x;
		this.y = y;
		this.width = width;
		this.label = label;
        font = new Font(fontFamily, Font.BOLD, fontSize);
	}
    public tmButton (int x, int y, int width, int height, String label, Color bgColor)
    {
   		this.x = x;
		this.y = y;
		this.width = width;
        this.height = height;
		this.label = label;
        font = new Font(fontFamily, Font.BOLD, fontSize);
        this.bgColor = bgColor;
    }
	public tmButton (int x, int y, int width, int height, String label, Color bgColor, Font font)
    {
   		this.x = x;
		this.y = y;
		this.width = width;
        this.height = height;
		this.label = label;
        this.font = font;
        this.bgColor = bgColor;
    }
	public boolean clicked(int x, int y)
	{
		if (x > this.x &&
			x < this.x + width &&
			y > this.y &&
			y < this.y + height)
		{
			return true;
		}
		return false;
	}
	public void draw(Graphics og)
	{
        og.setColor(bgColor);
		og.fillRoundRect(x, y, width, height, 5, 5);
		og.setColor(fgColor);
		og.drawRoundRect(x, y, width, height, 5, 5);
		og.setFont(font);
		og.drawString(label, x + xLabel, y + yLabel);
	}

	/**
	 * @return Returns the height.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @param height The height to set.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return Returns the x.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @param x The x to set.
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * @return Returns the y.
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * @param y The y to set.
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	public Font getFont()
	{
		return font;
	}
	public void setFont(Font font)
	{
		this.font = font;
	}

	public int getXLabel()
	{
		return xLabel;
	}

	public void setXLabel(int label)
	{
		xLabel = label;
	}

	public int getYLabel()
	{
		return yLabel;
	}

	public void setYLabel(int label)
	{
		yLabel = label;
	}
	public Color getBgColor()
	{
		return bgColor;
	}

	public void setBgColor(Color bgColor)
	{
		this.bgColor = bgColor;
	}

	public Color getFgColor()
	{
		return fgColor;
	}

	public void setFgColor(Color fgColor)
	{
		this.fgColor = fgColor;
	}
    public int getFontSize()
    {
        return fontSize;
    }

    public void setFontSize(int fontSize)
    {
        this.fontSize = fontSize;
        font = new Font(fontFamily, Font.BOLD, fontSize);
    }

    public String getFontFamily()
    {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily)
    {
        this.fontFamily = fontFamily;
        font = new Font(fontFamily, Font.BOLD, fontSize);
    }
}
