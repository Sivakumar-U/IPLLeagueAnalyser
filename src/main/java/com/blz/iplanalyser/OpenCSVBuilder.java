package com.blz.iplanalyser;

import java.io.Reader;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class OpenCSVBuilder<E> implements ICSVBuilder {
	@Override
	public List<E> getCSVFileList(Reader reader, Class csvClass) throws IPLAnalyserException {
		return this.getCSVBean(reader, csvClass).parse();
	}

	private CsvToBean<E> getCSVBean(Reader reader, Class csvClass) throws IPLAnalyserException {
		try {
			CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
			csvToBeanBuilder.withType(csvClass).withIgnoreLeadingWhiteSpace(true);
			return csvToBeanBuilder.build();
		} catch (RuntimeException runtimeException) {
			throw new IPLAnalyserException("Field Exception", IPLAnalyserException.ExceptionType.NO_SUCH_FIELD);
		}
	}

}
