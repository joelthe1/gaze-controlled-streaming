package com.deadpixel.digitize;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class FrameReader {

	public void readCSV() {
		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(getReader("splits/segment_aa"));
		System.out.println(allRows.size());
	}

	public Reader getReader(String path) {
		try {
			return new InputStreamReader(ClassLoader.getSystemResourceAsStream(path), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}

}
