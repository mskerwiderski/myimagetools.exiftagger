package de.msk.myimagetools.exiftagger.parser;

import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.domain.CameraAndFilmDataRecord;
import de.msk.myimagetools.exiftagger.domain.ImageDataRecord;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoCamera;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoLens;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;

public class DataFileGenericParser extends AbstractDataFileParser {

	public DataFileGenericParser(CmdLineParams cmdLineParams,
		GearInfos gearInfos) {
		super(cmdLineParams, gearInfos);
	}

	/*
	00 ImageNumber
	01 DateTimeOriginal
	02 ExposureTime
	03 Aperture
	04 CameraId
	05 LensId
	06 focal length
	07 Latitude
	08 Longitude
	09 Comment
	*/

	private static final int COLS = 10;
	protected ImageDataRecord createImageDataRecord(CSVRecord csvRecord) 
		throws ExifTaggerException {
		ImageDataRecord imageDataRecord = new ImageDataRecord();
		
		if (csvRecord == null) {
			throw new ExifTaggerException("csvRecord must not be empty.");
		}
		if (csvRecord.size() != COLS) {
			throw new ExifTaggerException(
				"csvRecord size is " + csvRecord.size() + 
				", but must be " + COLS + ".");
		}
		
		// fld00: '01' or '1' or '001' or ...
		imageDataRecord.setImageNumber(Integer.valueOf(csvRecord.get(0)));
		
		// fld01: '2007:08:07 15:36'
		imageDataRecord.setDateTimeOriginal(csvRecord.get(1));
		
		// fld02: '1/125'
		imageDataRecord.setExposureTime(csvRecord.get(2));
		
		// fld03: '5.6'
		imageDataRecord.setAperture(csvRecord.get(3));
		
		// fld04: camera id
		GearInfoCamera camera = this.getGearInfos().getCamera(csvRecord.get(4));
		if (camera != null) {
			imageDataRecord.setCamera(camera);
		} 
		
		// fld05: lens id
		GearInfoLens lens = this.getGearInfos().getLens(csvRecord.get(5));
		if (lens != null) {
			imageDataRecord.setLens(lens);
		}
		
		// fld06: focal length
		String focalLength = csvRecord.get(6);
		if (StringUtils.isEmpty(focalLength) &&
			(lens != null) && !lens.isZoom()) {
			focalLength = lens.getLensFocalLengthMin();
		}
		if (!StringUtils.isEmpty(focalLength) && (lens != null)) {
			imageDataRecord.setFocalLength(focalLength,
				lens.getLensFocalLengthCorrFactor());		
		}
		
		// fld07: Latitude ('48.16583876754729')
		// fld08: Longitude ('11.34543091724777')
		String latitudeStr = csvRecord.get(7);
		String latitudeRef = "N";
		if (StringUtils.startsWith(latitudeStr, "-")) {
			latitudeRef = "S";
			latitudeStr = StringUtils.remove(latitudeStr, "-");
		} else {
			latitudeStr = StringUtils.remove(latitudeStr, "+");
		}
		String longitudeStr = csvRecord.get(8);
		String longitudeRef = "E";
		if (StringUtils.startsWith(longitudeStr, "-")) {
			longitudeRef = "W";
			longitudeStr = StringUtils.remove(longitudeStr, "-");
		} else {
			longitudeStr = StringUtils.remove(longitudeStr, "+");
		}
		ImageDataRecord.GpsPosition gpsPosition = 
			new ImageDataRecord.GpsPosition(
				latitudeStr, latitudeRef, longitudeStr, longitudeRef);
		imageDataRecord.setGpsPosition(gpsPosition);

		// fld09: Comment ('%Ein Turm,#fo22,#fnd8,')
		String comment = csvRecord.get(9);
		ParserUtils.parseComment(this.getGearInfos(), imageDataRecord, comment);
		
		return imageDataRecord;
	}

	@Override
	protected void processCsvRecord(int recordNumber, CSVRecord record)
		throws ExifTaggerException {
		CameraAndFilmDataRecord filmDataRecord = 
			this.getCameraAndFilmDataRecord(); 
		Map<Integer, ImageDataRecord> imageDataRecordsMap = 
			filmDataRecord.getImageDataRecordsMap();
		if (recordNumber > 0) {
			ImageDataRecord imageDataRecord = 
				createImageDataRecord(record);
			imageDataRecordsMap.put(imageDataRecord.getImageNumber(), 
				imageDataRecord);
	    }
	}
	
	@Override
	public void postProcess() throws ExifTaggerException {
	}
}
