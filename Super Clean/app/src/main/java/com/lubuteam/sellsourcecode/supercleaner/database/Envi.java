package com.lubuteam.sellsourcecode.supercleaner.database;

import android.content.Context;
import android.database.SQLException;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Envi {

	public static List<CusStruct> cusSt1;
	public static List<CusStruct> cusSt2;

	private static int code;

	private static DataHelper dataHelper;

	public static String eP = Environment.getExternalStorageDirectory() + "/rinix-f/temp";
	private static String temp = UpMani.R();

	public static int scanApk(String arg) {
		code = 0;

		if (arg == null || arg.equals("")) {
			System.exit(0);
		}

		if (isApk(arg)) {
			recursiveScan(arg);
				File toDelete = new File(eP);
				deleteDir(toDelete);
		} else {
			code = 0;
		}
		return code;
	}

	private static boolean isApk(String path) {

		try {

			File fSourceZip = new File(path);
			ZipFile zipFile = new ZipFile(fSourceZip);
			ZipEntry ze = zipFile.getEntry(temp);

			if (ze != null) {
				zipFile.close();
				return true;
			} else {
				zipFile.close();
				return false;
			}

		} catch (Exception e) {
			return false;
		} 

	}

	private static void recursiveScan(String strZipFile) {

		try {

			File fSourceZip = new File(strZipFile);
			String zipPath = eP;
			File temp = new File(zipPath);
			temp.mkdir();
			ZipFile zipFile = new ZipFile(fSourceZip);
			Enumeration<? extends ZipEntry> e = zipFile.entries();
			while (e.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				File destinationFilePath = new File(zipPath, entry.getName());
				destinationFilePath.getParentFile().mkdirs();
				if (entry.isDirectory()) {
					continue;
				} else {

					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

					int b;
					byte buffer[] = new byte[1024];

					bis.read(buffer, 0, 4);
					int firstDword = ((0xFF & buffer[3]) << 24) | ((0xFF & buffer[2]) << 16) | ((0xFF & buffer[1]) << 8)
							| (0xFF & buffer[0]);
					bis.close();
					if (firstDword == 0x04034b50) {

						bis = new BufferedInputStream(zipFile.getInputStream(entry));
						FileOutputStream fos = new FileOutputStream(destinationFilePath);
						BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
						while ((b = bis.read(buffer, 0, 1024)) != -1) {

							bos.write(buffer, 0, b);
						}
						bos.flush();
						bos.close();
						bis.close();

						String childFile = zipPath + "/" + entry.getName();
						recursiveScan(childFile);
					}

					else if (firstDword == 0x0a786564) {
						bis = new BufferedInputStream(zipFile.getInputStream(entry));
						code = scanDex(bis, (int) entry.getCrc());
						bis.close();
						if (code > 0) {
							zipFile.close();
							return;
						}
						continue;
					} else if (firstDword == 0x464c457f) {
						bis = new BufferedInputStream(zipFile.getInputStream(entry));
						code = scanELF(bis, (int) entry.getCrc());
						bis.close();
						if (code > 0) {
							zipFile.close();
							return;
						}
						continue;
					} else if (firstDword == 0x214f3558) {
						bis = new BufferedInputStream(zipFile.getInputStream(entry));
						code = scanDOS(bis);
						bis.close();
						if (code > 0) {
							zipFile.close();
							return;
						}
						continue;
					} else {
						continue;
					}

				}

			}
			zipFile.close();

		} catch (Exception e) {
			return;
		}

	}

	public static int scanDex(BufferedInputStream is, int cr7) {

		int tRCode = 0;
		byte buffer[] = new byte[4096];

		try {
			is.mark(12);
			is.read(buffer, 0, 12);
			is.reset();
			int adler32 = ((0xFF & buffer[11]) << 24) | ((0xFF & buffer[10]) << 16) | ((0xFF & buffer[9]) << 8)
					| (0xFF & buffer[8]);
			int l = dataHelper.isInDNA(cr7, adler32);

			if (l != -1) {
				tRCode = l;
			} else {
				boolean cont = false;
				int loop = 0;
				while ((is.read(buffer, 0, 4096) != -1) && cont == false && loop < 27) {
					for (int i = 0; i < cusSt1.size() && cont == false; i++) {
						cont = stringSearch(buffer, cusSt1.get(i).getS());

						if (cont) {
							tRCode = cusSt1.get(i).getVid();
						}
					}
					loop++;
				}
			}

		} catch (IOException e) {
			return 0;
		}
		try {
			is.close();
		} catch (Exception e) {
			return 0;
		}

		return tRCode;
	}

	public static int scanDOS(BufferedInputStream is) {

		int tRCode = 0;

		try {

			byte buffer[] = new byte[68];
			is.read(buffer, 0, 68);
			CRC32 crc = new CRC32();
			crc.update(buffer);

			if (crc.getValue() == 0x6851CF3C) {
				tRCode = 1;
			}

			is.close();

		} catch (IOException e) {

			return 0;
		}
		return tRCode;
	}

	public static int scanELF(BufferedInputStream is, int cr7) {

		int tRCode = 0;
		byte buffer[] = new byte[0x34];

		try {

			is.mark(0x34);
			is.read(buffer, 0, 0x34);
			is.reset();
			CRC32 crc1 = new CRC32();
			crc1.update(buffer);
			int lemp = dataHelper.isInDNA(cr7, (int) crc1.getValue());
			if (lemp != -1) {
				tRCode = lemp;
			} else {
				boolean cont = false;
				int loop = 0;
				buffer = new byte[4096];
				while ((is.read(buffer, 0, 4096) != -1) && cont == false && loop < 27) {
					for (int i = 0; i < cusSt2.size() && cont == false; i++) {
						cont = stringSearch(buffer, cusSt2.get(i).getS());

						if (cont) {
							tRCode = cusSt2.get(i).getVid();
						}
					}
					loop++;
				}
			}

		} catch (IOException e) {

			return 0;
		}
		try {
			is.close();
		} catch (Exception e) {
			return 0;
		}
		return tRCode;
	}

	private static void deleteDir(File path)  {
		if (path.isDirectory()) {
			for (File child : path.listFiles()) {
				deleteDir(child);
			}
		}
		path.delete();
	}

	public static void releaseAll() {

		cusSt1.clear();
		cusSt2.clear();

		dataHelper.close();

		dataHelper.crush();

	}

	public static void i(Context cx) {

		try {

			dataHelper = new DataHelper(cx);

			dataHelper.createDataBase();

		} catch (IOException ioe) {

			return;

		}

		try {

			dataHelper.openDataBase();

		} catch (SQLException sqle) {

			return;

		}

	}

	private static boolean stringSearch(byte[] bytes, byte[] pattern) {

		ByteSearch bm = new ByteSearch(pattern);

		int k = bm.search(bytes);

		if (k == -1)
			return false;
		else
			return true;

	}

}
