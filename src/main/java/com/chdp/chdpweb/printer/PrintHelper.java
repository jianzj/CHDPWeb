package com.chdp.chdpweb.printer;

public class PrintHelper {
	private static GodexPrinter Printer = new GodexPrinter();

	public static void startAndSetup() {
		Printer.Open(PortType.USB.Val());

		Printer.Config.LabelMode(PaperMode.GetEnum(0), 40, 2);
		Printer.Config.LabelWidth(60);

		Printer.Config.Dark(15);
		Printer.Config.Speed(3);
		Printer.Config.PageNo(1);
		Printer.Config.CopyNo(1);
	}

	public static void printPackage(String name, String hospitalNo, int num, int sex, String hospital, String uuid,
			String createTime) {
		System.setProperty("jna.encoding", "GBK");
		Printer.Command.Start();
		Printer.Command.PrintText_EZPL_Internal("Z1", 5, 15, 2, 2, 0, "0", hospital + " No." + hospitalNo);
		Printer.Command.PrintText_EZPL_Internal("Z1", 175, 75, 3, 3, 0, "0", name);
		Printer.Command.PrintText_EZPL_Internal("Z1", 175, 140, 3, 3, 0, "0", num + "帖" + num * 2 + "包");
		Printer.Command.PrintText_EZPL_Internal("Z1", 175, 200, 2, 2, 0, "0", "生产日期" + createTime.substring(0, 10));
		Printer.Command.PrintQRCode(8, 76, 2, 2, "H", 8, 6, 0, uuid);
		Printer.Command.PrintText_EZPL_Internal("Z1", 35, 260, 2, 2, 0, "0", "上海青浦中药饮片有限公司");
		Printer.Command.End();
	}

	public static void printPrs(String name, String hospitalNo, int num, int sex, String hospital, String uuid,
			String createTime) {
		System.setProperty("jna.encoding", "GBK");
		Printer.Command.Start();
		Printer.Command.PrintText_EZPL_Internal("Z1", 5, 15, 2, 2, 0, "0", hospital + " No." + hospitalNo);
		Printer.Command.PrintText_EZPL_Internal("Z1", 255, 125, 3, 3, 0, "0", name);
		Printer.Command.PrintText_EZPL_Internal("Z1", 405, 135, 2, 2, 0, "0", sex == 1 ? "男" : "女");
		Printer.Command.PrintText_EZPL_Internal("Z1", 255, 190, 3, 3, 0, "0", num + "帖" + num * 2 + "包");
		Printer.Command.PrintQRCode(8, 70, 2, 2, "H", 8, 9, 0, uuid);
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
		printPackage("测试人", "200", 15, 1, "安亭镇社区医院嘉松北路", "20160201230202021", "2016-02-12 23:23:23");
		close();
	}
}
