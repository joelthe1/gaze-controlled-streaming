package com.deadpixel.digitize;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.conversions.Conversions;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class FrameReader {

	public void readCSV() {
		final long startTime1 = System.currentTimeMillis();
		/*CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(getReader("splits/segment_a"));
		System.out.println(allRows.size() + " and time=" + (System.currentTimeMillis() - startTime1));
		allRows.forEach(row -> {
			double[] doubleRow = Arrays.stream(row).mapToDouble(Double::parseDouble).toArray();
			
			if(doubleRow[1] == 0.0) FramesUtil.frameMap.put(row[0], new Frame(doubleRow));
			else if(doubleRow[1] == 8159.0) {
				Frame frame = FramesUtil.frameMap.remove(row[0]);
				frame.setBlock(doubleRow);
				FramesUtil.imageQueue.offer(frame);
				}
			else FramesUtil.frameMap.get(row[0]).setBlock(doubleRow);
		});*/
		// ObjectRowProcessor converts the parsed values and gives you the resulting row.
		ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
			@Override
			public void rowProcessed(Object[] row, ParsingContext context) {
				//here is the row. Let's just print it.
				//System.out.println(Arrays.toString(row));
				//Frame frame = new Frame(row);
				if((double)row[1] == 0.0) FramesUtil.frameMap.put((double)row[0], new Frame(row));
				else if((double)row[1] == 8159.0) {
					Frame frame = FramesUtil.frameMap.remove((double)row[0]);
					frame.setBlock(row);
					FramesUtil.imageQueue.offer(frame);
					}
				else FramesUtil.frameMap.get((double)row[0]).setBlock(row);
			}
		};
		
		List<Integer> doubleFieldIndices = new ArrayList<>();
		for(int i=0; i<195; i++)
			doubleFieldIndices.add(i);
		
		// converts values in the "Price" column (index 4) to BigDecimal
		rowProcessor.convertIndexes(Conversions.toDouble()).set(doubleFieldIndices);
		
		//rowProcessor.convertIndexes(Conversions.toInteger()).set(0, 1, 194);
		
		// converts the values in columns "Make, Model and Description" to lower case, and sets the value "chevy" to null.
		//rowProcessor.convertFields(Conversions.toLowerCase(), Conversions.toNull("chevy")).set("Make", "Model", "Description");
		
		// converts the values at index 0 (year) to BigInteger. Nulls are converted to BigInteger.ZERO.
		//rowProcessor.convertFields(new BigIntegerConversion(BigInteger.ZERO, "0")).set("year");
		
		//rowProcessor.con
		
		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.getFormat().setLineSeparator("\n");
		parserSettings.setRowProcessor(rowProcessor);
		//parserSettings.setHeaderExtractionEnabled(true);
		
		CsvParser parser = new CsvParser(parserSettings);
		
		//the rowProcessor will be executed here.
		parser.parse(getReader("splits/segment_a"));
	}

	public Reader getReader(String path) {
		try {
			return new InputStreamReader(ClassLoader.getSystemResourceAsStream(path), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}

}
