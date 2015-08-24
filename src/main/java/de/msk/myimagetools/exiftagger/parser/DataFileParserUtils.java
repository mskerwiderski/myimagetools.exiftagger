package de.msk.myimagetools.exiftagger.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.domain.CameraAndFilmDataRecord;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;
import de.msk.myimagetools.exiftagger.util.Utils;

public class DataFileParserUtils {

	private DataFileParserUtils() {
	}

	public static CameraAndFilmDataRecord process(
		CmdLineParams cmdLineParams, GearInfos gearInfos) 
		throws ExifTaggerException {
		CameraAndFilmDataRecord filmDataRecord = new CameraAndFilmDataRecord();
		if (!StringUtils.isEmpty(cmdLineParams.dataFile)) {
			CSVRecord record = getFirstCsvRecordOfDataFile(cmdLineParams.dataFile);
			if (StringUtils.equals(record.get(0), "Film speed")) {
				Utils.logcSep("Nikon F6 datafile detected.");
				filmDataRecord = 
					new DataFileNikonF6Parser(cmdLineParams, gearInfos).process();
			} else if (StringUtils.equals(record.get(0), "ImageNumber")) {
				Utils.logcSep("Generic datafile detected.");
				filmDataRecord = 
					new DataFileGenericParser(cmdLineParams, gearInfos).process();
			} else if (StringUtils.equals(record.get(0), "Number")) {
				Utils.logcSep("PhotoExif datafile detected.");
				filmDataRecord = 
					new DataFilePhotoExifParser(cmdLineParams, gearInfos).process();
			} else {
				throw new ExifTaggerException("Datafile '" + cmdLineParams.dataFile + "' could not be parsed due to unknown format!");
			}
		}
		return filmDataRecord;
	}
	
	private static CSVRecord getFirstCsvRecordOfDataFile(String csvFileName) 
		throws ExifTaggerException {
		CSVRecord record = null;
		Reader reader = null;
		try {
			reader = new FileReader(csvFileName);
			Iterable<CSVRecord> csvRecords = CSVFormat.EXCEL.parse(reader);
			record = csvRecords.iterator().next();
		} catch (FileNotFoundException e) {
			throw new ExifTaggerException("Datafile '" + csvFileName + "' could not be found!");
		} catch (Exception e) {
			throw new ExifTaggerException(e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				// noop.
			}
		}
		return record;
	}
}
