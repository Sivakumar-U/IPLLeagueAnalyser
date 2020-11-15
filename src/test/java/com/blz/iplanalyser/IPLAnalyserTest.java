package com.blz.iplanalyser;

import java.io.IOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

public class IPLAnalyserTest {

	private static final String MOST_RUNS_FILE_PATH = "C:\\Users\\Siva Reddy\\MostRuns.csv";
	private static final String MOST_WKTS_FILE_PATH = "C:\\Users\\Siva Reddy\\MostWkts.csv";
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

	@Test
	public void givenMostRunsCSV_ShouldReturnMax4sCricketer() throws IOException, IPLAnalyserException {
		Assert.assertEquals("Shikhar Dhawan", new IPLAnalyser().getTop4sCricketer(MOST_RUNS_FILE_PATH).get(0).player);
	}

	@Test
	public void givenMostRunsCSV_ShouldReturnMax6sCricketer() throws IOException, IPLAnalyserException {
		Assert.assertEquals("Andre Russell", new IPLAnalyser().getTop6sCricketer(MOST_RUNS_FILE_PATH).get(0).player);
	}

	@Test
	public void givenMostRunsCSV_ShouldReturnBestStrikingRates_With6sAnd4s() throws IOException, IPLAnalyserException {
		Assert.assertEquals("Andre Russell",
				new IPLAnalyser().getBestStrikingRatesWith6sAnd4s(MOST_RUNS_FILE_PATH).get(0).player);
	}

	@Test
	public void givenMostRunsCSV_ShouldReturnbestStrikingRatesWithBestAverages()
			throws IOException, IPLAnalyserException {
		Assert.assertEquals("MS Dhoni",
				new IPLAnalyser().getGreatAveragesWithBestStrikingRates(MOST_RUNS_FILE_PATH).get(0).player);
	}

	@Test
	public void givenMostRunsCSV_ShouldReturnWhoHitMaxRuns_WithBestAvgs() throws IOException, IPLAnalyserException {
		Assert.assertEquals("David Warner ",
				new IPLAnalyser().getMaximumRunsWithBestAverages(MOST_RUNS_FILE_PATH).get(0).player);
	}

	// Wickets
	@Test
	public void givenMostWktsCSV_ShouldReturnNumberOfWktsRecords() throws IPLAnalyserException {
		Assert.assertEquals(101, iplAnalyser.loadWktsCSVData(MOST_WKTS_FILE_PATH));
	}

	public void givenMostWktsCSV_ShouldReturnTopBowlingAverages() {
		try {
			iplAnalyser.loadCSVData(MOST_WKTS_FILE_PATH);
			String sortedData = iplAnalyser.getTopBattingAverages();
			MostWktsCSV[] wktsCSV = new Gson().fromJson(sortedData, MostWktsCSV[].class);
			Assert.assertEquals(166, wktsCSV[wktsCSV.length - 1].avg, DELTA);
		} catch (IPLAnalyserException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void givenMostWktsCSV_ShouldReturnTopStrikeRatesOfBowlers() {
		String sortedData = null;
		try {
			sortedData = iplAnalyser.getTopStrikingRatesOfBowler(MOST_WKTS_FILE_PATH);
			MostWktsCSV[] wktsCSV = new Gson().fromJson(sortedData, MostWktsCSV[].class);
			Assert.assertEquals(120, wktsCSV[wktsCSV.length - 1].avg, DELTA);
		} catch (IPLAnalyserException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void givenMostWktsCSV_ShouldReturnBestBowlingEconomy() throws IOException, IPLAnalyserException {
		Assert.assertEquals("Ben Cutting",
				new IPLAnalyser().getBowlersHadBestEconomy(MOST_WKTS_FILE_PATH).get(0).player);
	}

	@Test
	public void givenMostWktsCSV_ShouldReturnBestBowling_StrikingRatesWith5WAnd4W()
			throws IOException, IPLAnalyserException {
		Assert.assertEquals("Lasith Malinga",
				new IPLAnalyser().getBestStrikingRatesWith5WAnd4W(MOST_WKTS_FILE_PATH).get(0).player);
	}

	@Test
	public void givenMostWktsCSV_ShouldReturnGreatBowlingAveragesWithBestStrikingRates()
			throws IOException, IPLAnalyserException {
		Assert.assertEquals("Krishnappa Gowtham",
				iplAnalyser.getWhoHadBowlingAvgsWithBestStrikingRates(MOST_WKTS_FILE_PATH).get(0).player);
	}

	@Test
	public void givenMostWktsCSV_ShouldReturnMaximumWickets_WithBestBowlingAvgs()
			throws IOException, IPLAnalyserException {
		Assert.assertEquals("Imran Tahir",
				iplAnalyser.getBowlersMaximumWicketsWithBestBowlingAvgs(MOST_WKTS_FILE_PATH).get(0).player);
	}

	@Test
	public void givenMostWktsCSV_ShouldReturnBestBatting_AndBowlingAverages() throws IOException, IPLAnalyserException {
		Assert.assertEquals("Andre Russell", iplAnalyser.BestBattingAverageWithBestBowlingAverage().get(0));
	}

}
