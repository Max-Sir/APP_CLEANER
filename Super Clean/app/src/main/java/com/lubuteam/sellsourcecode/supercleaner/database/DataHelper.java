package com.lubuteam.sellsourcecode.supercleaner.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataHelper extends SQLiteOpenHelper {

	private static String DB_PATH;

	private static String DB_NAME = "vxoid.bin";

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	public DataHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
		DB_PATH = "/data/data/" + myContext.getPackageName() + "/databases/";
	}
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			db.disableWriteAheadLogging();
		}
	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
		} else {

			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}


	}

	private boolean checkDataBase() {

		File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}

	private void copyDataBase() throws IOException {

		try {
			InputStream myInput = myContext.getAssets().open(DB_NAME);

			InputFunction.d(myInput, DB_PATH + DB_NAME);

			myInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
		myDataBase.disableWriteAheadLogging();

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public List<CusStruct> getStrings(int fileID) {

		List<CusStruct> ssList = new ArrayList<CusStruct>();
		String query;

		switch (fileID) {
		case 0x0a786564:
		case 0x464c457f:
		case 0x11223344:
		case 0x214f3558:
			query = "SELECT * FROM stringInfo WHERE fid =" + fileID
					+ " ORDER BY vid ASC";
			break;
		default:
			query = "SELECT * FROM stringInfo WHERE fid =0x00";
			break;

		}

		Cursor c = myDataBase.rawQuery(query, null);

		while (c.moveToNext()) {
			CusStruct s = new CusStruct(toByteArray(c.getString(1)), c.getInt(3));
			ssList.add(s);
		}
		c.close();

		return ssList;

	}

	public int isInDNA(int id1, int id2) {
		String q = "SELECT vid FROM signatureInfo WHERE signature1 = " + id1
				+ " and signature2 = " + id2;
		Cursor c = myDataBase.rawQuery(q, null);
		int vid = -1;

		if (c.moveToNext()) {
			vid = c.getInt(0);
		}
		c.close();

		return vid;
	}

	public String getVname(int id) {
		String q = "SELECT * FROM virusName WHERE _id = " + id;
		Cursor c = myDataBase.rawQuery(q, null);
		String name = "";

		if (c.moveToNext()) {
			name = c.getString(1);
		}
		c.close();

		return name;
	}

	public String[] getDatInfo() {
		String q = "SELECT * FROM datInfo";
		Cursor c = myDataBase.rawQuery(q, null);

		c.moveToFirst();
		String[] t = { c.getInt(3) + "", c.getString(1) };
		c.close();

		return t;
	}

	private byte[] toByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public void crush() {
		File d = new File(DB_PATH + DB_NAME);
		d.delete();
	}

}
