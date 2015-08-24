package de.msk.myimagetools.exiftagger.util.csv;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;

public class CsvUtils {

	private CsvUtils() {
	}

	public static List<String> readValuesFromOneRecord(String record) 
		throws ExifTaggerException {
		if (StringUtils.isEmpty(record)) {
			throw new IllegalArgumentException("record must not be null.");
		}
		List<String> values = new ArrayList<String>();
		StringReader reader = new StringReader(record);
		try {
			CSVRecord csvRecord = CSVFormat.EXCEL.parse(reader).iterator().next();
			for (Iterator<String> it=csvRecord.iterator(); it.hasNext();) {
				String value = it.next();
				if (!StringUtils.isEmpty(value)) {
					values.add(value);
				}
			}
		} catch (IOException e) {
			throw new ExifTaggerException(e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				// noop.
			}
		}
		return values;
	}
	
	public static List<CSVRecord> readCsvComplete(String pathFileName) 
		throws ExifTaggerException {
		List<CSVRecord> csvRecords = new ArrayList<CSVRecord>();
		Reader reader = null;
		try {
			reader = new FileReader(pathFileName);
			Iterable<CSVRecord> csvIterator = CSVFormat.EXCEL.parse(reader);
			for (CSVRecord csvRecord : csvIterator) {
				if ((csvRecord.size() == 0) || 
					StringUtils.startsWith(csvRecord.get(0), "#") ||
					((csvRecord.size() == 1) && StringUtils.isEmpty(csvRecord.get(0)))) {
					// empty row or comment row --> skip.
				} else {
					csvRecords.add(csvRecord);
				}
			}
		} catch (Exception e) {
			throw new ExifTaggerException(e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				// noop.
			}
		}
		return csvRecords;
	}
}
