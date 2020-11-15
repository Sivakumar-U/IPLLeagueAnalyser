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

	public int loadCSVData(String indiaCensusCSVFilePath) throws IPLAnalyserException {
		try (Reader reader = Files.newBufferedReader(Paths.get(indiaCensusCSVFilePath))) {
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
		Comparator<MostRunsCSV> censusComparator = Comparator.comparing(census -> census.avg);
		this.sort(censusComparator);
		String sortedStateCensusJson = new Gson().toJson(this.iplCSVList);
		return sortedStateCensusJson;
	}

	public void sort(Comparator<MostRunsCSV> censusComparator) {
		for (int i = 0; i < iplCSVList.size(); i++) {
			for (int j = 0; j < iplCSVList.size() - i - 1; j++) {
				MostRunsCSV census1 = iplCSVList.get(j);
				MostRunsCSV census2 = iplCSVList.get(j + 1);
				if (censusComparator.compare(census1, census2) > 0) {
					iplCSVList.set(j, census2);
					iplCSVList.set(j + 1, census1);
				}
			}
		}
	}

}
