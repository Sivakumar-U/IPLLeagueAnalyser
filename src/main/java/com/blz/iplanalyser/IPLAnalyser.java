package com.blz.iplanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class IPLAnalyser {

	public static List<MostRunsCSV> iplRunsCSVList;
	public static List<MostWktsCSV> iplWktsCSVList;

	public int loadCSVData(String iplCSVFilePath) throws IPLAnalyserException {
		try (Reader reader = Files.newBufferedReader(Paths.get(iplCSVFilePath))) {
			ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
			iplRunsCSVList = csvBuilder.getCSVFileList(reader, MostRunsCSV.class);
			return iplRunsCSVList.size();
		} catch (IOException e) {
			throw new IPLAnalyserException(e.getMessage(), IPLAnalyserException.ExceptionType.NO_SUCH_FILE);
		}
	}

	public String getTopBattingAverages() throws IPLAnalyserException {
		if (iplRunsCSVList == null || iplRunsCSVList.size() == 0) {
			throw new IPLAnalyserException("NO_CENSUS_DATA", IPLAnalyserException.ExceptionType.NO_SUCH_FILE);
		}
		Comparator<MostRunsCSV> runsComparator = Comparator.comparing(census -> census.avg);
		this.sort(runsComparator);
		String sortedMostRunsJson = new Gson().toJson(this.iplRunsCSVList);
		return sortedMostRunsJson;
	}

	public String getTopStrikingRates() throws IPLAnalyserException {
		if (iplRunsCSVList == null || iplRunsCSVList.size() == 0) {
			throw new IPLAnalyserException("NO_CENSUS_DATA", IPLAnalyserException.ExceptionType.NO_SUCH_FILE);
		}
		Comparator<MostRunsCSV> runsComparator = Comparator.comparing(census -> census.sr);
		this.sort(runsComparator);
		String sortedMostRunsJson = new Gson().toJson(this.iplRunsCSVList);
		return sortedMostRunsJson;
	}

	public List<MostRunsCSV> getTop4sCricketer(String csvFilePath) throws IOException, IPLAnalyserException {
		loadCSVData(csvFilePath);
		List<MostRunsCSV> playerWithMax4s = iplRunsCSVList.stream()
				.sorted((player1, player2) -> Double.compare(player1.getNum4s(), player2.getNum4s()))
				.collect(Collectors.toList());
		Collections.reverse(playerWithMax4s);
		return playerWithMax4s;
	}

	public List<MostRunsCSV> getTop6sCricketer(String csvFilePath) throws IOException, IPLAnalyserException {
		loadCSVData(csvFilePath);
		List<MostRunsCSV> playerWithMax6s = iplRunsCSVList.stream()
				.sorted((player1, player2) -> Double.compare(player1.getNum6s(), player2.getNum6s()))
				.collect(Collectors.toList());
		Collections.reverse(playerWithMax6s);
		return playerWithMax6s;
	}

	public List<MostRunsCSV> getBestStrikingRatesWith6sAnd4s(String csvFilePath)
			throws IOException, IPLAnalyserException {
		loadCSVData(csvFilePath);
		Integer playerWithMax6s4s = iplRunsCSVList.stream().map(player -> (player.getNum4s() + player.getNum6s()))
				.max(Double::compare).get();
		List<MostRunsCSV> playerWithBest6s4s = iplRunsCSVList.stream()
				.filter(player -> player.getNum4s() + player.getNum6s() == playerWithMax6s4s)
				.collect(Collectors.toList());
		double playerWithBestStrikerate = playerWithBest6s4s.stream().map(MostRunsCSV::getSr).max(Double::compare)
				.get();
		return playerWithBest6s4s.stream().filter(player -> player.getSr() == playerWithBestStrikerate)
				.collect(Collectors.toList());
	}

	public List<MostRunsCSV> getGreatAveragesWithBestStrikingRates(String csvFilePath)
			throws IOException, IPLAnalyserException {
		loadCSVData(csvFilePath);
		double bestAvgs = iplRunsCSVList.stream().map(MostRunsCSV::getAvg).max(Double::compare).get();
		List<MostRunsCSV> playerWithBestAvgs = iplRunsCSVList.stream().filter(player -> player.getAvg() == bestAvgs)
				.collect(Collectors.toList());
		double playerWithBestStrikerate = playerWithBestAvgs.stream().map(MostRunsCSV::getSr).max(Double::compare)
				.get();
		return playerWithBestAvgs.stream().filter(player -> player.getSr() == playerWithBestStrikerate)
				.collect(Collectors.toList());
	}

	public List<MostRunsCSV> getMaximumRunsWithBestAverages(String csvFilePath)
			throws IOException, IPLAnalyserException {
		loadCSVData(csvFilePath);
		int maxRuns = iplRunsCSVList.stream().map(MostRunsCSV::getRuns).max(Integer::compare).get();
		List<MostRunsCSV> playerWithMaxRuns = iplRunsCSVList.stream().filter(player -> player.getRuns() == maxRuns)
				.collect(Collectors.toList());
		double playerWithBestAvgs = playerWithMaxRuns.stream().map(MostRunsCSV::getAvg).max(Double::compare).get();
		return playerWithMaxRuns.stream().filter(player -> player.getAvg() == playerWithBestAvgs)
				.collect(Collectors.toList());
	}

	public void sort(Comparator<MostRunsCSV> runsComparator) {
		for (int i = 0; i < iplRunsCSVList.size(); i++) {
			for (int j = 0; j < iplRunsCSVList.size() - i - 1; j++) {
				MostRunsCSV census1 = iplRunsCSVList.get(j);
				MostRunsCSV census2 = iplRunsCSVList.get(j + 1);
				if (runsComparator.compare(census1, census2) > 0) {
					iplRunsCSVList.set(j, census2);
					iplRunsCSVList.set(j + 1, census1);
				}
			}
		}
	}

	public int loadWktsCSVData(String CSVFilePath) throws IPLAnalyserException {
		try (Reader reader = Files.newBufferedReader(Paths.get(CSVFilePath))) {
			ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
			iplWktsCSVList = csvBuilder.getCSVFileList(reader, MostWktsCSV.class);
			return iplRunsCSVList.size();
		} catch (IOException e) {
			throw new IPLAnalyserException(e.getMessage(), IPLAnalyserException.ExceptionType.NO_SUCH_FILE);
		}
	}

	public String getTopBowlingAverages(String csvFilePath) throws IPLAnalyserException {
		loadWktsCSVData(csvFilePath);
		if (iplWktsCSVList == null || iplWktsCSVList.size() == 0) {
			throw new IPLAnalyserException("NO_CENSUS_DATA", IPLAnalyserException.ExceptionType.NO_SUCH_FILE);
		}
		Comparator<MostWktsCSV> wktsComparator = Comparator.comparing(census -> census.avg);
		this.sort2(wktsComparator);
		String sortedWktsJson = new Gson().toJson(this.iplWktsCSVList);
		return sortedWktsJson;
	}

	public void sort2(Comparator<MostWktsCSV> wktsComparator) {
		for (int i = 0; i < iplWktsCSVList.size(); i++) {
			for (int j = 0; j < iplWktsCSVList.size() - i - 1; j++) {
				MostWktsCSV wkt1 = iplWktsCSVList.get(j);
				MostWktsCSV wkt2 = iplWktsCSVList.get(j + 1);
				if (wktsComparator.compare(wkt1, wkt2) > 0) {
					iplWktsCSVList.set(j, wkt2);
					iplWktsCSVList.set(j + 1, wkt1);
				}
			}
		}
	}

}
