package com.blz.iplanalyser;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

public class IPLAnalyserTest {

	public static final String MOST_RUNS_FILE_PATH = "C:\\Users\\Siva Reddy\\MostRuns.csv";
	private static final double DELTA = 1e-15;

	private static IPLAnalyser iplAnalyser;

	@BeforeClass
	public static void createIPLAnalyserObj() {
		iplAnalyser = new IPLAnalyser();

	}

	@Test
	public void givenMostRunsCSV_ShouldReturnNumberOfRecords() throws IPLAnalyserException {
		Assert.assertEquals(101, iplAnalyser.loadCSVData(MOST_RUNS_FILE_PATH));
	}

	@Test
	public void givenMostRunsCSV_ShouldReturnTopBattingAverages() {
		try {
			iplAnalyser.loadCSVData(MOST_RUNS_FILE_PATH);
			String sortedData = iplAnalyser.getTopBattingAverages();
			MostRunsCSV[] censusCSV = new Gson().fromJson(sortedData, MostRunsCSV[].class);
			Assert.assertEquals(83.2, censusCSV[censusCSV.length - 1].avg, DELTA);
		} catch (IPLAnalyserException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void givenMostRunsCSV_ShouldReturnTopStrikingRatesOfBatsman() {
		try {
			iplAnalyser.loadCSVData(MOST_RUNS_FILE_PATH);
			String sortedData = iplAnalyser.getTopStrikingRates();
			MostRunsCSV[] mostRunsCSV = new Gson().fromJson(sortedData, MostRunsCSV[].class);
			Assert.assertEquals(333.33, mostRunsCSV[mostRunsCSV.length - 1].avg, DELTA);
		} catch (IPLAnalyserException ex) {
			ex.printStackTrace();
		}
	}

}
