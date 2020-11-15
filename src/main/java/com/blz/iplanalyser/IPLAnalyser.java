package com.blz.iplanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

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
