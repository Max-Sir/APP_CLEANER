package com.lubuteam.sellsourcecode.supercleaner.database;

public class CusStruct {

	byte[] sbyte;
	int id;

	public CusStruct(byte[] t1, int t2) {
		this.sbyte = t1;
		this.id = t2;
	}

	public void setS(byte[] s) {
		this.sbyte = s;
	}

	public void setVid(int t) {
		this.id = t;
	}

	public byte[] getS() {
		return this.sbyte;
	}

	public int getVid() {
		return this.id;
	}

}
