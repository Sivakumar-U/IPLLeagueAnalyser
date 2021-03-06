package com.blz.iplanalyser;

import com.opencsv.bean.CsvBindByName;

public class MostWktsCSV {

	@CsvBindByName(column = "POS")
	public int pos;

	@CsvBindByName(column = "PLAYER")
	public String player;

	@CsvBindByName(column = "Mat")
	public int mat;

	@CsvBindByName(column = "Inns")
	public int inns;

	@CsvBindByName(column = "Ov")
	public double ov;

	@CsvBindByName(column = "Runs")
	public int runs;

	@CsvBindByName(column = "Wkts")
	public int wkts;

	@CsvBindByName(column = "BBI")
	public int bbi;

	@CsvBindByName(column = "Avg")
	public double avg;

	@CsvBindByName(column = "Econ")
	public double econ;

	@CsvBindByName(column = "SR")
	public double sr;

	@CsvBindByName(column = "4w")
	public int num4w;

	@CsvBindByName(column = "5w")
	public int num5w;

	public double getEcon() {
		return econ;
	}

	public double getSr() {
		return sr;
	}

	public double getAvg() {
		return avg;
	}

	public int getnumber4w() {
		return num4w;
	}

	public int getnumber5w() {
		return num5w;
	}

	public int getWkts() {
		return wkts;
	}

}
