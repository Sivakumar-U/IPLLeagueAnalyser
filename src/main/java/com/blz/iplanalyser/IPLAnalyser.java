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

	public static List<MostRunsCSV> iplCSVList;

	public int loadCSVData(String iplCSVFilePath) throws IPLAnalyserException {
		try (Reader reader = Files.newBufferedReader(Paths.get(iplCSVFilePath))) {
			ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
			iplCSVList = csvBuilder.getCSVFileList(reader, MostRunsCSV.class);
			return iplCSVList.size();
		} catch (IOException e) {
			throw new IPLAnalyserException(e.getMessage(), IPLAnalyserException.ExceptionType.NO_SUCH_FILE);
		}
	}

	public String getTopBattingAverages() throws IPLAnalyserException {
		if (iplCSVList == null || iplCSVList.size() == 0) {
			throw new IPLAnalyserException("NO_CENSUS_DATA", IPLAnalyserException.ExceptionType.NO_SUCH_FILE);
		}
		Comparator<MostRunsCSV> runsComparator = Comparator.comparing(census -> census.avg);
		this.sort(runsComparator);
		String sortedMostRunsJson = new Gson().toJson(this.iplCSVList);
		return sortedMostRunsJson;
	}

	public String getTopStrikingRates() throws IPLAnalyserException {
		if (iplCSVList == null || iplCSVList.size() == 0) {
			throw new IPLAnalyserException("NO_CENSUS_DATA", IPLAnalyserException.ExceptionType.NO_SUCH_FILE);
		}
		Comparator<MostRunsCSV> runsComparator = Comparator.comparing(census -> census.sr);
		this.sort(runsComparator);
		String sortedMostRunsJson = new Gson().toJson(this.iplCSVList);
		return sortedMostRunsJson;
	}

	public List<MostRunsCSV> getTop4sCricketer(String csvFilePath) throws IOException, IPLAnalyserException {
		loadCSVData(csvFilePath);
		List<MostRunsCSV> playerWithMax4s = iplCSVList.stream()
				.sorted((player1, player2) -> Double.compare(player1.getNum4s(), player2.getNum4s()))
				.collect(Collectors.toList());
		Collections.reverse(playerWithMax4s);
		return playerWithMax4s;
	}

	public List<MostRunsCSV> getTop6sCricketer(String csvFilePath) throws IOException, IPLAnalyserException {
		loadCSVData(csvFilePath);
		List<MostRunsCSV> playerWithMax6s = iplCSVList.stream()
				.sorted((player1, player2) -> Double.compare(player1.getNum6s(), player2.getNum6s()))
				.collect(Collectors.toList());
		Collections.reverse(playerWithMax6s);
		return playerWithMax6s;
	}
	
	public List<MostRunsCSV> getBestStrikingRatesWith6sAnd4s(String csvFilePath)
			throws IOException, IPLAnalyserException {
		loadCSVData(csvFilePath);
		Integer playerWithMax6s4s = iplCSVList.stream().map(player -> (player.getNum4s() + player.getNum6s()))
				.max(Double::compare).get();
		List<MostRunsCSV> playerWithBest6s4s = iplCSVList.stream()
				.filter(player -> player.getNum4s() + player.getNum6s() == playerWithMax6s4s)
				.collect(Collectors.toList());
		double playerWithBestStrikerate = playerWithBest6s4s.stream().map(MostRunsCSV::getSr).max(Double::compare)
				.get();
		return playerWithBest6s4s.stream().filter(player -> player.getSr() == playerWithBestStrikerate)
				.collect(Collectors.toList());
	}

	public void sort(Comparator<MostRunsCSV> runsComparator) {
		for (int i = 0; i < iplCSVList.size(); i++) {
			for (int j = 0; j < iplCSVList.size() - i - 1; j++) {
				MostRunsCSV census1 = iplCSVList.get(j);
				MostRunsCSV census2 = iplCSVList.get(j + 1);
				if (runsComparator.compare(census1, census2) > 0) {
					iplCSVList.set(j, census2);
					iplCSVList.set(j + 1, census1);
				}
			}
		}
	}

}
