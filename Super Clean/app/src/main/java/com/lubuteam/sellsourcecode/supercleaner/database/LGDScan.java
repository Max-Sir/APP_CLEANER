package com.lubuteam.sellsourcecode.supercleaner.database;

public class LGDScan {

	int apps;
	int files;
	int infections;
	int deletions;
	String duration;
	String when;

	public LGDScan(int t0, int t1, int t2, int t3, String t4, String t5) {
		this.apps = t0;
		this.files = t1;
		this.infections = t2;
		this.deletions = t3;
		this.duration = t4;
		this.when = t5;
	}
	
	public int getApps() {
		return this.apps;
	}

	public int getFiles() {
		return this.files;
	}

	public int getInfections() {
		return this.infections;
	}

	public int getDeletions() {
		return this.deletions;
	}

	public String getDuration() {
		return this.duration;
	}

	public String getWhen() {
		return this.when;
	}

}
