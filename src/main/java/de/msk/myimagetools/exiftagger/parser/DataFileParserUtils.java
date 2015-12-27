package de.msk.myimagetools.exiftagger.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.domain.CameraAndFilmDataRecord;
import de.msk.myimagetools.exiftagger.domain.ImageDataRecord;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoTag;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;
import de.msk.myimagetools.exiftagger.util.Utils;
import de.msk.myimagetools.exiftagger.util.csv.CsvUtils;

public class DataFileParserUtils {

	private DataFileParserUtils() {
	}

	public static CameraAndFilmDataRecord process(
		CmdLineParams cmdLineParams, GearInfos gearInfos) 
		throws ExifTaggerException {
		CameraAndFilmDataRecord filmDataRecord = new CameraAndFilmDataRecord();
		if (!StringUtils.isEmpty(cmdLineParams.dataFile)) {
			CSVRecord record = getFirstCsvRecordOfDataFile(cmdLineParams.dataFile, "#");
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
	
	private static CSVRecord getFirstCsvRecordOfDataFile(String csvFileName, String skipLinesWithStartingString) 
		throws ExifTaggerException {
		CSVRecord record = null;
		Reader reader = null;
		try {
			reader = new FileReader(csvFileName);
			Iterable<CSVRecord> csvRecords = CSVFormat.EXCEL.parse(reader);
			Iterator<CSVRecord> it = csvRecords.iterator();
			boolean found = false;
			while (!found && it.hasNext()) {
				record = it.next();
				if ((record.size() > 0) && 
					(StringUtils.isEmpty(skipLinesWithStartingString) || 
					 !StringUtils.startsWith(record.get(0), skipLinesWithStartingString))) {
						found = true;
				}
			}
			if (!found) {
				record = null;
			}
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
	
	public static void parseComment(GearInfos gearInfos, ImageDataRecord imageDataRecord, String comment) 
		throws ExifTaggerException {
		if (gearInfos == null) {
			throw new IllegalArgumentException("gearInfos must not be null!");
		}
		if (imageDataRecord == null) {
			throw new IllegalArgumentException("imageDataRecord must not be null!");
		}
		if (!StringUtils.isEmpty(comment)) {
			List<String> values = CsvUtils.readValuesFromOneRecord(comment);
			for (String value : values) {
				if (StringUtils.startsWith(value, "%")) {
					imageDataRecord.setTitle(StringUtils.substring(value, 1));
				} else if (StringUtils.startsWith(value, "&")) {
					imageDataRecord.setCaptionAbstract(StringUtils.substring(value, 1));
				} else if (StringUtils.startsWith(value, "*")) {
					imageDataRecord.setFlashMode("1");
				} else if (StringUtils.startsWith(value, "#")) {
					String tagStr = StringUtils.substring(value, 1);
					GearInfoTag tag = gearInfos.getTag(tagStr);
					if (tag != null) {
						imageDataRecord.addTag(tag.getDesc());
					} else {
						imageDataRecord.addTag(tagStr);
					}
				} else if (StringUtils.startsWith(value, "$")) {
					String exposureCompensation = StringUtils.substring(value, 1);
					if (!StringUtils.isEmpty(exposureCompensation) && 
						Pattern.matches("([+,-])?[0-9](\\.[0-9])?", exposureCompensation)) {
						imageDataRecord.setExposureCompensation(exposureCompensation);
					}
				}
			}
		}
	}
}
