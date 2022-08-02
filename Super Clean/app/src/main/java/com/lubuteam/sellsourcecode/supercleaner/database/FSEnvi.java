package com.lubuteam.sellsourcecode.supercleaner.database;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.zip.CRC32;

public class FSEnvi {

	private static int firstDword;
	private static FileInputStream toScan;
	private static int code;
	private static CRC32 crc;
	private static int temp;

	public static int scanFile(String path) {
		code = 0;
		try {
			toScan = new FileInputStream(path);
		} catch (Exception e) {
			return 0;
		}

		try {
			BufferedInputStream bis = new BufferedInputStream(toScan);
			bis.mark(4);
			byte buffer[] = new byte[4];
			bis.read(buffer, 0, 4);
			bis.reset();
			firstDword = ((0xFF & buffer[3]) << 24) | ((0xFF & buffer[2]) << 16) | ((0xFF & buffer[1]) << 8)
					| (0xFF & buffer[0]);

			if (firstDword == 0x04034b50) {
				code = Envi.scanApk(path);
			} else if (firstDword == 0x0a786564) {
				crc = new CRC32();
				while ((temp = bis.read()) != -1) {
					crc.update(temp);
				}
				bis.reset();
				code = Envi.scanDex(bis, (int) crc.getValue());
			} else if (firstDword == 0x464c457f) {
				crc = new CRC32();
				while ((temp = bis.read()) != -1) {
					crc.update(temp);
				}
				bis.reset();
				code = Envi.scanELF(bis, (int) crc.getValue());
			} else if (firstDword == 0x214f3558) {
				code = Envi.scanDOS(bis);
			} else {
				code = 0;
			}
			bis.close();

		} catch (Exception e) {
			return 0;

		}

		return code;
	}

}
