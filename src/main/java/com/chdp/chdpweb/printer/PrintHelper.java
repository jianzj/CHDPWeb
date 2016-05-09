package com.chdp.chdpweb.printer;

public class PrintHelper {
	private static GodexPrinter Printer = new GodexPrinter();

	public static void startAndSetup() {
		Printer.Open(PortType.USB.Val());

		Printer.Config.LabelMode(PaperMode.GetEnum(0), 40, 2);
		Printer.Config.LabelWidth(60);

		Printer.Config.Dark(8);
		Printer.Config.Speed(3);
		Printer.Config.PageNo(1);
		Printer.Config.CopyNo(1);
	}

	public static void printSingle(String name, String hospitalNo, int num, int sex, String hospital, String uuid,
			String createTime) {
		System.setProperty("jna.encoding", "GBK");
		Printer.Command.Start();
		Printer.Command.PrintText_EZPL_Internal("Z1", 10, 15, 2, 2, 0, "0", hospital + " 编号：" + hospitalNo);
		Printer.Command.PrintText_EZPL_Internal("Z1", 180, 75, 3, 3, 0, "0", name);
		Printer.Command.PrintText_EZPL_Internal("Z1", 380, 85, 2, 2, 0, "0", sex == 1 ? "男" : "女");
		Printer.Command.PrintText_EZPL_Internal("Z1", 180, 140, 3, 3, 0, "0", num + "帖" + num * 2 + "包");
		Printer.Command.PrintText_EZPL_Internal("Z1", 180, 200, 2, 2, 0, "0", "生产日期：" + createTime.substring(10));
		Printer.Command.PrintQRCode(10, 78, 2, 2, "H", 8, 6, 0, uuid);
		Printer.Command.PrintText_EZPL_Internal("Z1", 10, 246, 2, 2, 0, "0", "上海青浦中药饮片有限公司");
		Printer.Command.End();
	}

	public static void close() {
		Printer.Close();
	}
}
