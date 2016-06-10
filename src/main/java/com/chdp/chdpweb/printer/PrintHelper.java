package com.chdp.chdpweb.printer;

public class PrintHelper {
	private static GodexPrinter Printer = new GodexPrinter();

	public static void startAndSetup() {
		Printer.Open(PortType.USB.Val());

		Printer.Config.LabelMode(PaperMode.GetEnum(0), 40, 2);
		Printer.Config.LabelWidth(60);

		Printer.Config.Dark(6);
		Printer.Config.Speed(3);
		Printer.Config.PageNo(1);
		Printer.Config.CopyNo(1);
	}

	public static void printPackage(String name, String hospitalNo, int num, int sex, String hospital, String uuid,
			String createTime) {
		System.setProperty("jna.encoding", "GBK");
		Printer.Command.Start();
		Printer.Command.UploadText(40, "宋体", hospital, 20, FontWeight.FW_400_NORMAL, RotateMode.Angle_0, "hospital");
		Printer.Command.PrintImageByName("hospital", 15, 15);
		//Printer.Command.PrintText_EZPL_Internal("Z1", 5, 15, 2, 2, 0, "0", hospital + " No." + hospitalNo);
		Printer.Command.UploadText(50, "宋体", name, 30, FontWeight.FW_900_HEAVY, RotateMode.Angle_0, "name");
		Printer.Command.PrintImageByName("name", 210, 65);
		//Printer.Command.PrintText_EZPL_Internal("Z1", 175, 75, 3, 3, 0, "0", name);
		Printer.Command.UploadText(50, "宋体", num+"帖", 30, FontWeight.FW_900_HEAVY, RotateMode.Angle_0, "num");
		Printer.Command.PrintImageByName("num", 240, 125);
		//Printer.Command.PrintText_EZPL_Internal("Z1", 175, 140, 3, 3, 0, "0", num + "帖" + num * 2 + "包");
		Printer.Command.UploadText(30, "宋体", "No.", 15, FontWeight.FW_400_NORMAL, RotateMode.Angle_0, "no");
		Printer.Command.PrintImageByName("no", 210, 200);
		Printer.Command.UploadText(50, "宋体", hospitalNo, 30, FontWeight.FW_900_HEAVY, RotateMode.Angle_0, "hospitalNo");
		Printer.Command.PrintImageByName("hospitalNo", 250, 185);
		Printer.Command.UploadText(30, "宋体", "生产日期 " + createTime.substring(0, 10), 15, FontWeight.FW_400_NORMAL, RotateMode.Angle_0, "date");
		Printer.Command.PrintImageByName("date", 90, 250);
		//Printer.Command.PrintText_EZPL_Internal("Z1", 175, 200, 2, 2, 0, "0", "生产日期" + createTime.substring(0, 10));
		Printer.Command.PrintQRCode(20, 65, 2, 2, "H", 8, 7, 0, uuid);
		Printer.Command.UploadText(30, "宋体", "上海青浦中药饮片有限公司", 15, FontWeight.FW_400_NORMAL, RotateMode.Angle_0, "company");
		Printer.Command.PrintImageByName("company", 50, 280);
		//Printer.Command.PrintText_EZPL_Internal("Z1", 35, 260, 2, 2, 0, "0", "上海青浦中药饮片有限公司");
		Printer.Command.End();
	}

	public static void printPrs(String name, String hospitalNo, int num, int sex, String hospital, String uuid,
			String createTime) {
		System.setProperty("jna.encoding", "GBK");
		Printer.Command.Start();
		Printer.Command.UploadText(40, "宋体", hospital, 20, FontWeight.FW_400_NORMAL, RotateMode.Angle_0, "hospital");
		Printer.Command.PrintImageByName("hospital", 15, 15);
		//Printer.Command.PrintText_EZPL_Internal("Z1", 5, 15, 2, 2, 0, "0", hospital + " No." + hospitalNo);
		Printer.Command.UploadText(40, "宋体", "No." + hospitalNo, 20, FontWeight.FW_400_NORMAL, RotateMode.Angle_0, "no");
		Printer.Command.PrintImageByName("no", 255, 90);
		Printer.Command.UploadText(50, "宋体", name, 25, FontWeight.FW_400_NORMAL, RotateMode.Angle_0, "name");
		Printer.Command.PrintImageByName("name", 255, 145);
		//Printer.Command.PrintText_EZPL_Internal("Z1", 255, 125, 3, 3, 0, "0", name);
		Printer.Command.UploadText(30, "宋体", sex == 1 ? "男" : "女", 20, FontWeight.FW_400_NORMAL, RotateMode.Angle_0, "sex");
		Printer.Command.PrintImageByName("sex", 415, 155);
		//Printer.Command.PrintText_EZPL_Internal("Z1", 405, 135, 2, 2, 0, "0", sex == 1 ? "男" : "女");
		Printer.Command.UploadText(50, "宋体", num + "帖" + num * 2 + "包", 25, FontWeight.FW_400_NORMAL, RotateMode.Angle_0, "num");
		Printer.Command.PrintImageByName("num", 255, 210);
		//Printer.Command.PrintText_EZPL_Internal("Z1", 255, 190, 3, 3, 0, "0", num + "帖" + num * 2 + "包");
		Printer.Command.PrintQRCode(15, 70, 2, 2, "H", 8, 9, 0, uuid);
		Printer.Command.End();
	}

	public static void printMachine(String name, String uuid) {
		System.setProperty("jna.encoding", "GBK");
		Printer.Command.Start();
		Printer.Command.PrintText_EZPL_Internal("Z1", 10, 8, 2, 2, 0, "0", name);
		Printer.Command.PrintQRCode(100, 55, 2, 2, "H", 8, 10, 0, uuid);
		Printer.Command.End();
	}

	public static void close() {
		Printer.Close();
	}

	public static void main(String[] args) {
		startAndSetup();
		//printPackage("朱雪英", "17262", 7, 1, "青浦中医院", "20160201230202021", "2016-02-12 23:23:23");
		printPrs("王玥", "17262", 7, 1, "青浦中医院", "20160201230202021", "2016-02-12 23:23:23");
		close();
	}
}
