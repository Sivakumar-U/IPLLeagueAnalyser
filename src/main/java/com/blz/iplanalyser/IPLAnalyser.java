package com.blz.iplanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

	public String getTopStrikingRatesOfBowler(String csvFilePath) throws IPLAnalyserException {
		loadWktsCSVData(csvFilePath);
		if (iplWktsCSVList == null || iplWktsCSVList.size() == 0) {
			throw new IPLAnalyserException("NO_CENSUS_DATA", IPLAnalyserException.ExceptionType.NO_SUCH_FILE);
		}
		Comparator<MostWktsCSV> wktsComparator = Comparator.comparing(census -> census.sr);
		this.sort2(wktsComparator);
		String sortedWktsJson = new Gson().toJson(this.iplWktsCSVList);
		return sortedWktsJson;
	}

	public List<MostWktsCSV> getBowlersHadBestEconomy(String csvFilePath) throws IOException, IPLAnalyserException {
		loadWktsCSVData(csvFilePath);
		List<MostWktsCSV> playerWithBestEconomy = iplWktsCSVList.stream()
				.sorted(Comparator.comparingDouble(MostWktsCSV::getEcon)).collect(Collectors.toList());
		Collections.reverse(playerWithBestEconomy);
		return playerWithBestEconomy;
	}

	public List<MostWktsCSV> getBestStrikingRatesWith5WAnd4W(String csvFilePath)
			throws IOException, IPLAnalyserException {
		loadWktsCSVData(csvFilePath);
		int playerWith4wAnd5w = iplWktsCSVList.stream().map(player -> (player.getnumber4w() + player.getnumber5w()))
				.max(Integer::compare).get();
		List<MostWktsCSV> playerWithBest4wAnd5w = iplWktsCSVList.stream()
				.filter(player -> player.getnumber4w() + player.getnumber5w() == playerWith4wAnd5w)
				.collect(Collectors.toList());
		double playerWithBestStrikeRate = playerWithBest4wAnd5w.stream().map(MostWktsCSV::getSr).max(Double::compare)
				.get();
		return playerWithBest4wAnd5w.stream().filter(player -> player.getSr() == playerWithBestStrikeRate)
				.collect(Collectors.toList());
	}

	public List<MostWktsCSV> getWhoHadBowlingAvgsWithBestStrikingRates(String csvFilePath)
			throws IOException, IPLAnalyserException {
		loadWktsCSVData(csvFilePath);
		double playerWithAvgs = iplWktsCSVList.stream().map(MostWktsCSV::getAvg).max(Double::compare).get();
		List<MostWktsCSV> playerWithBestAvgs = iplWktsCSVList.stream()
				.filter(player -> player.getAvg() == playerWithAvgs).collect(Collectors.toList());
		double playerWithBestStrikeRate = playerWithBestAvgs.stream().map(MostWktsCSV::getSr).max(Double::compare)
				.get();
		return playerWithBestAvgs.stream().filter(player -> player.getSr() == playerWithBestStrikeRate)
				.collect(Collectors.toList());
	}

	public List<MostWktsCSV> getBowlersMaximumWicketsWithBestBowlingAvgs(String csvFilePath)
			throws IOException, IPLAnalyserException {
		loadWktsCSVData(csvFilePath);
		int playerWithWkts = iplWktsCSVList.stream().map(MostWktsCSV::getWkts).max(Integer::compare).get();
		List<MostWktsCSV> playerWithMaxWkts = iplWktsCSVList.stream()
				.filter(player -> player.getWkts() == playerWithWkts).collect(Collectors.toList());
		double playerWithBestBowlingAvgs = playerWithMaxWkts.stream().map(MostWktsCSV::getAvg).max(Double::compare)
				.get();
		return playerWithMaxWkts.stream().filter(player -> player.getAvg() == playerWithBestBowlingAvgs)
				.collect(Collectors.toList());
	}

	public List<String> BestBattingAverageWithBestBowlingAverage() throws IOException, IPLAnalyserException {
		List<String> list = new ArrayList<>();
		List<MostRunsCSV> playerWithBestBattingAvg = iplRunsCSVList.stream()
				.sorted(Comparator.comparingDouble(player -> player.avg)).collect(Collectors.toList());
		Collections.reverse(playerWithBestBattingAvg);
		List<MostWktsCSV> playerWithBestBowlingAvg = iplWktsCSVList.stream()
				.sorted(Comparator.comparingDouble(player -> player.avg)).collect(Collectors.toList());
		for (MostRunsCSV mostRunsCSV : playerWithBestBattingAvg) {
			for (MostWktsCSV mostWktsCSV : playerWithBestBowlingAvg) {
				if (mostRunsCSV.player.equals(mostWktsCSV.player)) {
					list.add(mostRunsCSV.player);
				}
			}
		}
		return list;
	}

	public List<String> getBestAllRounders() throws IOException, IPLAnalyserException {
		List<String> allRounder = new ArrayList<>();
		List<MostRunsCSV> playerWithBestBattingRuns = iplRunsCSVList.stream()
				.sorted(Comparator.comparingDouble(player -> player.runs)).collect(Collectors.toList());
		Collections.reverse(playerWithBestBattingRuns);
		List<MostWktsCSV> playerWithBestBowlingWkts = iplWktsCSVList.stream()
				.sorted(Comparator.comparingDouble(player -> player.wkts)).collect(Collectors.toList());
		for (MostRunsCSV mostRunsCSV : playerWithBestBattingRuns) {
			for (MostWktsCSV mostWktsCSV : playerWithBestBowlingWkts) {
				if (mostRunsCSV.player.equals(mostWktsCSV.player)) {
					allRounder.add(mostRunsCSV.player);
				}
			}
		}
		return allRounder;
	}

	public List<MostRunsCSV> maximum100sWithBestBattingAverage(String csvFilePath)
			throws IOException, IPLAnalyserException {
		loadCSVData(csvFilePath);
		int century = iplRunsCSVList.stream().map(MostRunsCSV::getCentury).max(Integer::compare).get();
		List<MostRunsCSV> maxCentury = iplRunsCSVList.stream().filter(player -> player.getCentury() == century)
				.collect(Collectors.toList());
		double battingAvg = maxCentury.stream().map(MostRunsCSV::getAvg).max(Double::compare).get();
		return maxCentury.stream().filter(player -> player.getAvg() == battingAvg).collect(Collectors.toList());
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
