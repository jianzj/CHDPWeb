package com.chdp.chdpweb.printer;

public class clsPrinterCommand 
{
	EZioLib.API API = EZioLib.API.INSTANCE;
	
	//---------------------------------------------------------------------------
	// Start Job (^L)
	//---------------------------------------------------------------------------
	void Start()
	{
		API.sendcommand("^L");
	}

	//---------------------------------------------------------------------------
	// End Job (E)
	//---------------------------------------------------------------------------
	void End()
	{
		API.sendcommand("E");
	}

	//---------------------------------------------------------------------------
	// Print Text (Simple)
	//---------------------------------------------------------------------------
	int PrintText(int PosX, int PosY, int FontHeight, String FontName, String Data)
	{
	    return API.ecTextOut(PosX, PosY, FontHeight, FontName, Data);
	}

	//---------------------------------------------------------------------------
	// Print Text (More Parameter)
	//---------------------------------------------------------------------------
	int PrintText(int PosX, int PosY, int FontHeight, String FontName, String Data, int TextWidth, FontWeight Dark, RotateMode Rotate)
	{
	    return API.ecTextOutR(PosX, PosY, FontHeight, FontName, Data, TextWidth, Dark.Val(), Rotate.Val());
	}

	//---------------------------------------------------------------------------
	// Print Text (Detail Parameter)
	//---------------------------------------------------------------------------
	int PrintText(int PosX, int PosY, int FontHeight, String FontName, String Data, int TextWidth, FontWeight Dark, RotateMode Rotate, Italic_State Italic, Underline_State Underline, Strikeout_State Strikeout, Inverse_State Inverse)
	{
	    return API.ecTextOutFine(PosX, PosY, FontHeight, FontName, Data, TextWidth, Dark.Val(), Rotate.Val(), Italic.Val(), Underline.Val(), Strikeout.Val(), Inverse.Val());
	}

	//---------------------------------------------------------------------------
	// UploadImage (Internal)
	//---------------------------------------------------------------------------
	void UploadImage_Int(String Filename, String DisplayName, Image_Type mType)
	{
	    String strType;
	    if (mType == Image_Type.BMP)
	        strType = "bmp";
	    else
	        strType = "pcx";

	    // Delete Image
	    API.sendcommand("~MDELG," + DisplayName);

	    // Upload Image
	    API.intloadimage(Filename, DisplayName, strType);
	}

	//---------------------------------------------------------------------------
	// UploadImage (External)
	//---------------------------------------------------------------------------
	void UploadImage_Ext(String Filename, String DisplayName, Image_Type mType)
	{
	    String strType;
	    if (mType == Image_Type.BMP)
	        strType = "bmp";
	    else
	        strType = "pcx";

	    // Delete Image
	    API.sendcommand("~MDELG," + DisplayName);

	    // Upload Image
	    API.extloadimage(Filename, DisplayName, strType);
	}

	//---------------------------------------------------------------------------
	// UploadImage (Full Color)
	//---------------------------------------------------------------------------
	void UploadImage_FullColor(String Filename, String DisplayName, int nRotate)
	{
	    // Delete Image
		API.sendcommand("~MDELG," + DisplayName);

	    // Upload Image
		API.downloadimage(Filename, nRotate, DisplayName);
	}

	//---------------------------------------------------------------------------
	// Upload Text Image
	//---------------------------------------------------------------------------
	void UploadText(int FontHeight, String FontName, String Data, int TextWidth, FontWeight Dark, RotateMode Rotate, String Name)
	{
	    // Delete Image
		API.sendcommand("~MDELG," + Name);

	    // Upload Text Image
		API.ecTextDownLoad(FontHeight, FontName, Data, TextWidth, Dark.Val(), Rotate.Val(), Name);
	}
	
	//---------------------------------------------------------------------------
	// Upload Text Image
	//---------------------------------------------------------------------------
	void UploadTextW(int FontHeight, String FontName, String Data, int TextWidth, FontWeight Dark, RotateMode Rotate, String Name)
	{
	    // Delete Image
		API.sendcommand("~MDELG," + Name);

	    // Upload Text Image
		API.ecTextDownLoadW(FontHeight, FontName, Data, TextWidth, Dark.Val(), Rotate.Val(), Name, Data.length());
	}

	//---------------------------------------------------------------------------
	// Print Image By Name
	//---------------------------------------------------------------------------
	void PrintImageByName(String DisplayName, int PosX, int PosY)
	{
	    // Print Image
		API.sendcommand("Y" + Integer.toString(PosX) + "," + Integer.toString(PosY) + "," + DisplayName);
	}

	//---------------------------------------------------------------------------
	// Print Image
	//---------------------------------------------------------------------------
	int PrintImage(int PosX, int PosY, String FileName, int mRotation)
	{
	    return API.putimage(PosX, PosY, FileName, mRotation);
	}

	//---------------------------------------------------------------------------
	// Print Image
	//---------------------------------------------------------------------------
	int PrintImage(int PosX, int PosY, String FileName, int mRotation, int Halftone)
	{
	    return API.putimage_Halftone(PosX, PosY, FileName, mRotation, Halftone);
	}
	
	//---------------------------------------------------------------------------
	// Auto Sensing
	//---------------------------------------------------------------------------
	void AutoSensing()
	{
		API.sendcommand("~S,SENSOR,0");
	}

	//---------------------------------------------------------------------------
	// Send Command String
	//---------------------------------------------------------------------------
	int Send(String Cmd)
	{
	    return API.sendcommand(Cmd);
	}

	//---------------------------------------------------------------------------
	// Send Byte Array
	//---------------------------------------------------------------------------
	void SendByte(byte [] ByteArray)
	{
		API.sendbuf(ByteArray, ByteArray.length);
	}

	//---------------------------------------------------------------------------
	// Read Return Data
	//---------------------------------------------------------------------------
	String Read()
	{
		//byte[] ByteData = new byte[2048];
		//int len = API.RcvBuf(ByteData, ByteData.length); 
		//String tmp = new String(ByteData).trim();
	    //return tmp;
		
        byte[] byteArray = new byte[2048];
        String RetData = "";
        int RetryNo = 3;
        int CurNo = 0;

        while (true)
        {
            if (API.RcvBuf(byteArray, byteArray.length) == 0)
            {
                CurNo++;
            }
            else
            {
                CurNo = 0;
                RetData += new String(byteArray).trim();
            }

            if (CurNo >= RetryNo)
                break;
        }

        return RetData;
	}

	//---------------------------------------------------------------------------
	// Print 1D BarCode
	//---------------------------------------------------------------------------
	int PrintBarCode(BarCodeType mBarCodeType, int PosX, int PosY, String Data)
	{
	    return API.Bar_S(mBarCodeType.Val(), PosX, PosY, Data);
	}

	//---------------------------------------------------------------------------
	// Print 1D BarCode (More Parameter)
	//---------------------------------------------------------------------------
	int PrintBarCode(BarCodeType mBarCodeType, int PosX, int PosY, int Narrow, int Wide, int Height, int Rotation, int Raedable, String Data)
	{
	    return API.Bar(mBarCodeType.Val(), PosX, PosY, Narrow, Wide, Height, Rotation, Raedable, Data);
	}

	//---------------------------------------------------------------------------
	// Print QR Code
	//---------------------------------------------------------------------------
	int PrintQRCode(int PosX, int PosY, String Data)
	{
	    return API.Bar_QRcode_S(PosX, PosY, Data.length(), Data);
	}

	//---------------------------------------------------------------------------
	// Print QR Code (More Parameter)
	//---------------------------------------------------------------------------
	int PrintQRCode(int PosX, int PosY, int Mode, int Type, String ErrorLavel, int Mask, int Mul, int Rotation, String Data)
	{
	    return API.Bar_QRcode(PosX, PosY, Mode, Type, ErrorLavel, Mask, Mul, Data.length(), Rotation, Data);
	}

	//---------------------------------------------------------------------------
	// Print PDF417
	//---------------------------------------------------------------------------
	int PrintPDF417(int PosX, int PosY, String Data)
	{
	    return API.Bar_PDF417_S(PosX, PosY, Data.length(), Data);
	}

	//---------------------------------------------------------------------------
	// Print PDF417 (More Parameter)
	//---------------------------------------------------------------------------
	int PrintPDF417(int PosX, int PosY, int Width, int Height, int Row, int Col, int ErrorLevel, int Rotation, String Data)
	{
	    return API.Bar_PDF417(PosX, PosY, Width, Height, Row, Col, ErrorLevel, Data.length(), Rotation, Data);
	}

	//---------------------------------------------------------------------------
	// Print Aztec
	//---------------------------------------------------------------------------
	int PrintAztec(int PosX, int PosY, String Data)
	{
	    return API.Bar_Aztec_S(PosX, PosY, Data.length(), Data);
	}

	//---------------------------------------------------------------------------
	// Print Aztec (More Parameter)
	//---------------------------------------------------------------------------
	int PrintAztec(int PosX, int PosY, int Rotation, int Mul, String ECICs, String MenuSymbol, int Type, String Data)
	{
	    return API.Bar_Aztec(PosX, PosY, Rotation, Mul, ECICs, Type, MenuSymbol, Data.length(), Data);
	}

	//---------------------------------------------------------------------------
	// Print MaxiCode
	//---------------------------------------------------------------------------
	int PrintMaxiCode(int PosX, int PosY, String CountryCode, String PostalCode, String nClass, String Data)
	{
	    return API.Bar_Maxicode_S(PosX, PosY, CountryCode, PostalCode, nClass, 0, Data);
	}

	//---------------------------------------------------------------------------
	// Print MaxiCode (More Parameter)
	//---------------------------------------------------------------------------
	int PrintMaxiCode(int PosX, int PosY, int SymbolNo, int SetNo, int Mode, String CountryCode, String PostalCode, String nClass, int Rotate, String Data)
	{
	    return API.Bar_Maxicode(PosX, PosY, SymbolNo, SetNo, Mode, CountryCode, PostalCode, nClass, Rotate, Data);
	}

	//---------------------------------------------------------------------------
	// Print Data Matrix
	//---------------------------------------------------------------------------
	int PrintDataMatrix(int PosX, int PosY, String Data)
	{
	    return API.Bar_DataMatrix_S(PosX, PosY, Data.length(), Data);
	}

	//---------------------------------------------------------------------------
	// Print Data Matrix (More Parameter)
	//---------------------------------------------------------------------------
	int PrintDataMatrix(int PosX, int PosY, int Enlarge, String RotationR, String Data)
	{
	    return API.Bar_DataMatrix(PosX, PosY, Enlarge, RotationR, Data.length(), Data);
	}

    int DrawHorLine(int PosX, int PosY, int Length, int Thick)
    {
        return API.DrawHorLine(PosX, PosY, Length, Thick);
    }

    int DrawVerLine(int PosX, int PosY, int Length, int Thick)
    {
        return API.DrawVerLine(PosX, PosY, Length, Thick);
    }

    int FillRec(int PosX, int PosY, int Rec_W, int Rec_H)
    {
        return API.FillRec(PosX, PosY, Rec_W, Rec_H);
    }

    int DrawRec(int PosX, int PosY, int Rec_W, int Rec_H, int lrw, int ubw)
    {
        return API.DrawRec(PosX, PosY, Rec_W, Rec_H, lrw, ubw);
    }

    int DrawOblique(int PosX1, int PosY1, int Thick, int PosX2, int PosY2)
    {
        return API.DrawOblique(PosX1, PosY1, Thick, PosX2, PosY2);
    }

    int DrawEllipse(int PosX, int PosY, int Ellipse_W, int Ellipse_H, int PenWidth)
    {
        return API.DrawEllipse(PosX, PosY, Ellipse_W, Ellipse_H, PenWidth);
    }

    int DrawRoundRec(int PosX, int PosY, int Rec_W, int Rec_H, int Arc_W, int Arc_H, int PenWidth)
    {
        return API.DrawRoundRec(PosX, PosY, Rec_W, Rec_H, Arc_W, Arc_H, PenWidth);
    }

    int DrawTriangle(int PosX1, int PosY1, int PosX2, int PosY2, int PosX3, int PosY3, int PenWidth)
    {
        return API.DrawTriangle(PosX1, PosY1, PosX2, PosY2, PosX3, PosY3, PenWidth);
    }

    int DrawDiamond(int PosX, int PosY, int Rec_W, int Rec_H, int PenWidth)
    {
        return API.DrawDiamond(PosX, PosY, Rec_W, Rec_H, PenWidth);
    }
    
    int PrintText_EZPL_Internal(
            String FontType,
            int PosX,
            int PosY,
            int Mul_X,
            int Mul_Y,
            int Gap,
            String RotationInverse,
            String Data)
        {
            return API.InternalFont_TextOut(FontType, PosX, PosY, Mul_X, Mul_Y, Gap, RotationInverse, Data);
        }

        int PrintText_EZPL_Internal(
            String FontType,
            int PosX,
            int PosY,
            String Data)
        {
            return API.InternalFont_TextOut_S(FontType, PosX, PosY, Data);
        }

        int PrintText_EZPL_Bitmapped(
            String FontName,
            int PosX,
            int PosY,
            int Mul_X,
            int Mul_Y,
            int Gap,
            String RotationInverse,
            String Data)
        {
            return API.DownloadFont_TextOut(FontName, PosX, PosY, Mul_X, Mul_Y, Gap, RotationInverse, Data);
        }

        int PrintText_EZPL_Bitmapped(
            String FontName,
            int PosX,
            int PosY,
            String Data)
        {
            return API.DownloadFont_TextOut_S(FontName, PosX, PosY, Data);
        }

        int PrintText_EZPL_TTF(
            String FontName,
            int PosX,
            int PosY,
            int Font_W,
            int Font_H,
            int SpaceChar,
            String RotationInverse,
            String TTFTable,
            int WidthMode,
            String Data)
        {
            return API.TrueTypeFont_TextOut(FontName, PosX, PosY, Font_W, Font_H, SpaceChar, RotationInverse, TTFTable, WidthMode, Data);
        }

        int PrintText_EZPL_TTF(
            String FontName,
            int PosX,
            int PosY,
            String Data)
        {
            return API.TrueTypeFont_TextOut_S(FontName, PosX, PosY, Data);
        }
	
	//---------------------------------------------------------------------------
}
