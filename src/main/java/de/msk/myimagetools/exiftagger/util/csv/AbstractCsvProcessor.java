package de.msk.myimagetools.exiftagger.util.csv;

import java.util.List;

import org.apache.commons.csv.CSVRecord;

import de.msk.myimagetools.exiftagger.ExifTaggerException;

public abstract class AbstractCsvProcessor {

	public AbstractCsvProcessor() {
	}

	protected abstract void processCsvRecord(int recordNumber, CSVRecord record)
		throws ExifTaggerException;
	
	protected void processCsvDataFile(String csvFileName) throws ExifTaggerException {
		List<CSVRecord> listRecords = CsvUtils.readCsvComplete(csvFileName);
		for (int idx=0; idx < listRecords.size(); idx++) {
			this.processCsvRecord(idx, listRecords.get(idx));
		}
	}
}
