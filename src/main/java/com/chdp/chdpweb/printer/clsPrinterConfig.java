package com.chdp.chdpweb.printer;
//---------------------------------------------------------------------------
// Create clsPrinterConfig.java by Jeffrey 2014/07/21
//---------------------------------------------------------------------------
public class clsPrinterConfig 
{
	EZioLib.API API = EZioLib.API.INSTANCE;
	
	//---------------------------------------------------------------------------
	// Setup Function
	//---------------------------------------------------------------------------
	void Setup(
	    int LabelLength,
	    int Darkness,
	    int Speed,
	    int LabelMode,
	    int LabelGap,
	    int BlackTop)
	{
		API.setup(LabelLength, Darkness, Speed, LabelMode, LabelGap, BlackTop);
	}

	//---------------------------------------------------------------------------
	// Set Label Height & Paper Type (^Q)
	//---------------------------------------------------------------------------
	void LabelMode(PaperMode nMode, int nLabelHeight, int nGapFeed)
	{
	    if (nMode == PaperMode.GapLabel)
	    	API.sendcommand("^Q" + Integer.toString(nLabelHeight) + "," + Integer.toString(nGapFeed));
	    else
	    	API.sendcommand("^Q" + Integer.toString(nLabelHeight) + ",0," + Integer.toString(nGapFeed));
	}

	//---------------------------------------------------------------------------
	// Set Label Width (^W)
	//---------------------------------------------------------------------------
	void LabelWidth(int nWidth)
	{
		API.sendcommand("^W" + Integer.toString(nWidth));
	}

	//---------------------------------------------------------------------------
	// Set Dark (^H)
	//---------------------------------------------------------------------------
	void Dark(int nDark)
	{
		API.sendcommand("^H" + Integer.toString(nDark));
	}

	//---------------------------------------------------------------------------
	// Set Speed (^S)
	//---------------------------------------------------------------------------
	void Speed(int nSpeed)
	{
		API.sendcommand("^S" + Integer.toString(nSpeed));
	}

	//---------------------------------------------------------------------------
	// Set Page No (^P)
	//---------------------------------------------------------------------------
	void PageNo(int nPageNo)
	{
		API.sendcommand("^P" + Integer.toString(nPageNo));
	}

	//---------------------------------------------------------------------------
	// Set Copy No (^C)
	//---------------------------------------------------------------------------
	void CopyNo(int nCopyNo)
	{
		API.sendcommand("^C" + Integer.toString(nCopyNo));
	}

	//---------------------------------------------------------------------------
}
