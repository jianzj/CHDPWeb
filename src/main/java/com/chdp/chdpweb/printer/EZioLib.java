package com.chdp.chdpweb.printer;

//---------------------------------------------------------------------------
// Create EZioLib.java by Jeffrey 2014/07/21
//---------------------------------------------------------------------------
import com.sun.jna.Library;
import com.sun.jna.Native;

public class EZioLib {
	public interface API extends Library {
		String path = API.class.getResource("/").getPath().replaceAll("%20", " ").substring(1) + "EZio64.dll";
		API INSTANCE = (API) Native.loadLibrary(path, API.class);

		public int openport(String strPort);

		public int OpenUSB(String strUsbID);

		public int OpenDriver(String strDriverName);

		public int OpenNet(String strIP, String strPort);

		public int setbaudrate(int nBaud);

		public void closeport();

		public int setup(int LabelLength, int Darkness, int Speed, int LabelMode, int LabelGap, int BlackTop);

		public int sendcommand(String Command);

		public int RcvBuf(byte[] ByteData, int nLength);

		public int sendbuf(byte[] ByteData, int nLength);

		public int ecTextOut(int PosX, int PosY, int FontHeight, String FontName, String data);

		public int ecTextOutR(int PosX, int PosY, int FontHeight, String FontName, String data, int width, int weight,
				int rotation);

		public int ecTextOutFine(int PosX, int PosY, int FontHeight, String FontName, String data, int width,
				int weight, int rotation, int italic, int under, int strike, int inverse);

		public int intloadimage(String filename, String destfilenme, String type);

		public int extloadimage(String filename, String destfilenme, String type);

		public int putimage(int x, int y, String filename, int degree);

		public int putimage_Halftone(int PosX, int PosY, String Filename, int Degree, int Halftone);

		public int downloadimage(String filepath, int rotation, String name);

		public int ecTextDownLoad(int h, String font, String data, int width, int weight, int rotation, String name);

		public int ecTextDownLoadW(int h, String font, String data, int width, int weight, int rotation, String name,
				int len);

		public int FindFirstUSB(byte[] x);

		public int FindNextUSB(byte[] x);

		public int Bar(String BarcodeType, int PosX, int PosY, int Narrow, int Wide, int Height, int Rotation,
				int Readable, String data);

		public int Bar_S(String BarcodeType, int PosX, int PosY, String data);

		public int Bar_GS1DataBar(String BarcodeType, int PosX, int PosY, int Narrow, int Segment, int Height,
				int Rotation, int Readable, String data);

		public int Bar_GS1DataBar_S(String BarcodeType, int PosX, int PosY, String data);

		public int Bar_PDF417(int PosX, int PosY, int Width, int Height, int Row, int Column, int ErrorLevel, int Len,
				int Rotation, String data);

		public int Bar_PDF417_S(int PosX, int PosY, int Len, String data);

		public int Bar_MicroPDF417(int PosX, int PosY, int Width, int Height, int Mode, int Len, int Rotation,
				String data);

		public int Bar_MicroPDF417_S(int PosX, int PosY, int Len, String data);

		public int Bar_Maxicode(int PosX, int PosY, int SymbolNo, int SetNo, int Mode, String CountryCode,
				String PostalCode, String Class, int Rotation, String data);

		public int Bar_Maxicode_S(int PosX, int PosY, String CountryCode, String PostalCode, String Class, int Rotation,
				String data);

		public int Bar_DataMatrix(int PosX, int PosY, int Enlarge, String RotationR, int Len, String data);

		public int Bar_DataMatrix_S(int PosX, int PosY, int Len, String data);

		public int Bar_QRcode(int PosX, int PosY, int Mode, int Type, String ErrorLevel, int Mask, int Mul, int Len,
				int Rotation, String data);

		public int Bar_QRcode_S(int PosX, int PosY, int Len, String data);

		public int Bar_Aztec(int PosX, int PosY, int Rotation, int Mul, String ECICs, int Type, String MenuSymbol,
				int Len, String data);

		public int Bar_Aztec_S(int PosX, int PosY, int Len, String data);

		public int DrawHorLine(int PosX, int PosY, int Length, int Thick);

		public int DrawVerLine(int PosX, int PosY, int Length, int Thick);

		public int FillRec(int PosX, int PosY, int Rec_W, int Rec_H);

		public int DrawRec(int PosX, int PosY, int Rec_W, int Rec_H, int lrw, int ubw);

		public int DrawOblique(int PosX1, int PosY1, int Thick, int PosX2, int PosY2);

		public int DrawEllipse(int PosX, int PosY, int Ellipse_W, int Ellipse_H, int PenWidth);

		public int DrawRoundRec(int PosX, int PosY, int Rec_W, int Rec_H, int Arc_W, int Arc_H, int PenWidth);

		public int DrawTriangle(int PosX1, int PosY1, int PosX2, int PosY2, int PosX3, int PosY3, int Thick);

		public int DrawDiamond(int PosX, int PosY, int Diamand_W, int Diamand_H, int Thick);

		public int InternalFont_TextOut(String FontType, int PosX, int PosY, int Mul_X, int Mul_Y, int Gap,
				String RotationInverse, String Data);

		public int InternalFont_TextOut_S(String FontType, int PosX, int PosY, String Data);

		public int DownloadFont_TextOut(String FontName, int PosX, int PosY, int Mul_X, int Mul_Y, int Gap,
				String RotationInverse, String Data);

		public int DownloadFont_TextOut_S(String FontName, int PosX, int PosY, String Data);

		public int TrueTypeFont_TextOut(String FontName, int PosX, int PosY, int Font_W, int Font_H, int SpaceChar,
				String RotationInverse, String TTFTable, int WidthMode, String Data);

		public int TrueTypeFont_TextOut_S(String FontName, int PosX, int PosY, String Data);

		public int GetDllVersion(byte[] version);
	}
}

enum BarCodeType {
	Code39_Extended("A"), // BA
	Code39_Extended_CheckDidit("A2"), // BA2
	Code39("A3"), // BA3
	Code39_CheckDidit("A4"), // BA4
	EAN8("B"), // BB
	EAN8_Add2("C"), // BC
	EAN8_Add5("D"), // BD
	EAN13("E"), // BE
	EAN13_Add2("F"), // BF
	EAN13_Add5("G"), // BG
	UPCA("H"), // BH
	UPCA_Add2("I"), // BI
	UPCA_Add5("J"), // BJ
	UPCE("K"), // BK
	UPCE_Add2("L"), // BL
	UPCE_Add5("M"), // BM
	I2of5("N"), // BN
	I2of5_CheckDigit("N2"), // BN2
	Codabar("O"), // BO
	Code93("P"), // BP
	Code128_Auto("Q"), // BQ
	Code128_Subset("Q2"), // BQ2
	UCC_128("R"), // BR
	PostNET("S"), // BS
	ITF14("T"), // BT
	EAN128("U"), // BU
	RPS128("V"), // BV
	HIBC("X"), // BX
	MSI_1MOD10("Y"), // BY
	MSI_2MOD10("Y2"), // BY2
	MSI_1MOD1110("Y3"), // BY3
	MSI_NoDigitCheck("Y4"), // BY4
	I2of5_ShippingBearerBars("Z"), // BZ
	UCC_EAN128_KMART("1"), // B1
	UCC_EAN128_RANDOM("2"), // B2
	Telepen("3"), // B3
	FIM("4"), // B4
	Plessey("7"); // B7

	private String m_Val;

	BarCodeType(String Val) {
		m_Val = Val;
	}

	public String Val() {
		return m_Val;
	}
};

enum PortType {
	LPT1(0), COM1(1), COM2(2), COM3(3), COM4(4), LPT2(5), USB(6);

	private int m_nVal;

	PortType(int nVal) {
		m_nVal = nVal;
	}

	public int Val() {
		return m_nVal;
	}

	static PortType GetEnum(int nIdx) {
		switch (nIdx) {
		case 0:
			return LPT1;
		case 1:
			return COM1;
		case 2:
			return COM2;
		case 3:
			return COM3;
		case 4:
			return COM4;
		case 5:
			return LPT2;
		case 6:
			return USB;
		default:
			return USB;
		}
	}
};

enum PaperMode {
	GapLabel(0), PlainPaperLabel(1);

	private int m_nVal;

	PaperMode(int nVal) {
		m_nVal = nVal;
	}

	public int Val() {
		return m_nVal;
	}

	static PaperMode GetEnum(int nIdx) {
		switch (nIdx) {
		case 0:
			return GapLabel;
		case 1:
			return PlainPaperLabel;
		default:
			return PlainPaperLabel;
		}
	}
};

enum RotateMode {
	Angle_0(0), Angle_90(90), Angle_180(180), Angle_270(270);

	private int m_nVal;

	RotateMode(int nVal) {
		m_nVal = nVal;
	}

	public int Val() {
		return m_nVal;
	}

	static RotateMode GetEnum(int nIdx) {
		switch (nIdx) {
		case 0:
			return Angle_0;
		case 90:
			return Angle_90;
		case 180:
			return Angle_180;
		case 270:
			return Angle_270;
		default:
			return Angle_0;
		}
	}
};

enum FontWeight {
	FW_100_THIN(100), FW_200_EXTRALIGHT(200), FW_300_LIGHT(300), FW_400_NORMAL(400), FW_500_MEDIUM(
			500), FW_600_FW_SEMIBOLD(600), FW_700_BOLD(700), FW_800_EXTRABOLD(800), FW_900_HEAVY(900);

	private int m_nVal;

	FontWeight(int nVal) {
		m_nVal = nVal;
	}

	public int Val() {
		return m_nVal;
	}

	static FontWeight GetEnum(int nIdx) {
		switch (nIdx) {
		case 100:
			return FW_100_THIN;
		case 200:
			return FW_200_EXTRALIGHT;
		case 300:
			return FW_300_LIGHT;
		case 400:
			return FW_400_NORMAL;
		case 500:
			return FW_500_MEDIUM;
		case 600:
			return FW_600_FW_SEMIBOLD;
		case 700:
			return FW_700_BOLD;
		case 800:
			return FW_800_EXTRABOLD;
		case 900:
			return FW_900_HEAVY;
		default:
			return FW_500_MEDIUM;
		}
	}
};

enum Italic_State {
	Italic_OFF(0), Italic_ON(1);

	private int m_nVal;

	Italic_State(int nVal) {
		m_nVal = nVal;
	}

	public int Val() {
		return m_nVal;
	}

	static Italic_State GetEnum(int nIdx) {
		switch (nIdx) {
		case 0:
			return Italic_OFF;
		case 1:
			return Italic_ON;
		default:
			return Italic_OFF;
		}
	}
};

enum Underline_State {
	Underline_OFF(0), Underline_ON(1);

	private int m_nVal;

	Underline_State(int nVal) {
		m_nVal = nVal;
	}

	public int Val() {
		return m_nVal;
	}

	static Underline_State GetEnum(int nIdx) {
		switch (nIdx) {
		case 0:
			return Underline_OFF;
		case 1:
			return Underline_ON;
		default:
			return Underline_OFF;
		}
	}
};

enum Strikeout_State {
	Strikeout_OFF(0), Strikeout_ON(1);

	private int m_nVal;

	Strikeout_State(int nVal) {
		m_nVal = nVal;
	}

	public int Val() {
		return m_nVal;
	}

	static Strikeout_State GetEnum(int nIdx) {
		switch (nIdx) {
		case 0:
			return Strikeout_OFF;
		case 1:
			return Strikeout_ON;
		default:
			return Strikeout_OFF;
		}
	}
};

enum Inverse_State {
	Inverse_OFF(0), Inverse_ON(1);

	private int m_nVal;

	Inverse_State(int nVal) {
		m_nVal = nVal;
	}

	public int Val() {
		return m_nVal;
	}

	static Inverse_State GetEnum(int nIdx) {
		switch (nIdx) {
		case 0:
			return Inverse_OFF;
		case 1:
			return Inverse_ON;
		default:
			return Inverse_OFF;
		}
	}
};

enum Image_Type {
	BMP(0), PCX(1);

	private int m_nVal;

	Image_Type(int nVal) {
		m_nVal = nVal;
	}

	public int Val() {
		return m_nVal;
	}

	static Image_Type GetEnum(int nIdx) {
		switch (nIdx) {
		case 0:
			return BMP;
		case 1:
			return PCX;
		default:
			return BMP;
		}
	}
};