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
import de.msk.myimagetools.exiftagger.util.ExifSpecUtils.ExifExposureMode;

public class DataFilePhotoExifParser extends AbstractDataFileParser {

	public DataFilePhotoExifParser(CmdLineParams cmdLineParams,
		GearInfos gearInfos) {
		super(cmdLineParams, gearInfos);
	}

	/*
	00 Number ('1')
	01 Date ('23/03/2015 22:21')
	02 Camera ('1')
	03 Film type ('ADOX CMS 20 II')
	04 ASA value ('200')
	05 Speed ('1/125s')
	06 Aperture ('3.5')
	07 Lens ('Mamiya G50')
	08 Focal ('90')
	09 Latitude ('48.16583876754729')
	10 Longitude ('11.34543091724777')
	11 Comment ('Aussichtsplattform')
	*/
	
	private static final int COLS = 12;
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
		
		// fld01: '23/03/2015 22:21'
		String srcDateTime = csvRecord.get(1);
		String destDateTime = 
			StringUtils.mid(srcDateTime, 6, 4) + ":" +
			StringUtils.mid(srcDateTime, 3, 2) + ":" +
			StringUtils.mid(srcDateTime, 0, 2) + " " +
			StringUtils.mid(srcDateTime, 11, 5); 
		imageDataRecord.setDateTimeOriginal(destDateTime);
		
		// fld05: '1/125s'
		String exposureTime = csvRecord.get(5);
		if (!StringUtils.isEmpty(exposureTime)) {
			if (StringUtils.equals(exposureTime, "Auto")) {
				imageDataRecord.setExposureMode(
					ExifExposureMode.Auto.getStrValue());
			} else {
				imageDataRecord.setExposureTime(
					StringUtils.remove(exposureTime, "s"));		
			}
		}
		
		// fld06: '5.6'
		String aperture = csvRecord.get(6);
		if (!StringUtils.isEmpty(aperture)) {
			if (StringUtils.equals(aperture, "Auto")) {
				imageDataRecord.setExposureMode(
					ExifExposureMode.Auto.getStrValue());
			} else {
				imageDataRecord.setAperture(aperture);		
			}
		}
		
		// fld07: lens id
		GearInfoLens lens = this.getGearInfos().getLens(csvRecord.get(7));
		if (lens != null) {
			imageDataRecord.setLens(lens);
		}
		
		// fld08: '80'
		if (lens != null) {
			imageDataRecord.setFocalLength(csvRecord.get(8),
				lens.getLensFocalLengthCorrFactor());
		}
		
		// fld09: Latitude ('48.16583876754729')
		// fld10: Longitude ('11.34543091724777')
		String latitudeStr = csvRecord.get(9);
		String latitudeRef = "N";
		if (StringUtils.startsWith(latitudeStr, "-")) {
			latitudeRef = "S";
			latitudeStr = StringUtils.remove(latitudeStr, "-");
		} else {
			latitudeStr = StringUtils.remove(latitudeStr, "+");
		}
		String longitudeStr = csvRecord.get(10);
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
		
		// fld11: Comment ('%Ein Turm,#fo22,#fnd8,*,')
		String comment = csvRecord.get(11);
		ParserUtils.parseComment(this.getGearInfos(), imageDataRecord, comment);
		
		return imageDataRecord;
	}

	protected void updateCameraAndFilmDataRecord(
		CameraAndFilmDataRecord cameraAndFilmDataRecord,
		CSVRecord csvRecord) {
		// fld02: camera id
		GearInfoCamera camera = this.getGearInfos().getCamera(csvRecord.get(2));
		if (camera != null) {
			cameraAndFilmDataRecord.setCamera(camera);
		} 
		// fld03: filmtype
		cameraAndFilmDataRecord.setFilmName(
			this.getGearInfos().getFilmOrInstantFilmName(csvRecord.get(3)));
		// fld04: 200
		cameraAndFilmDataRecord.setFilmSpeed(csvRecord.get(4));
	}
	
	@Override
	protected void processCsvRecord(int recordNumber, CSVRecord record)
		throws ExifTaggerException {
		CameraAndFilmDataRecord cameraAndFilmDataRecord = 
			this.getCameraAndFilmDataRecord(); 
		Map<Integer, ImageDataRecord> imageDataRecordsMap = 
			cameraAndFilmDataRecord.getImageDataRecordsMap();
		if (recordNumber > 0) {
			ImageDataRecord imageDataRecord = 
				createImageDataRecord(record);
			imageDataRecordsMap.put(imageDataRecord.getImageNumber(), 
				imageDataRecord);
			if (recordNumber == 1) {
				updateCameraAndFilmDataRecord(
					cameraAndFilmDataRecord, record);
			}
	    }
	}
	
	@Override
	public void postProcess() throws ExifTaggerException {
	}
}
