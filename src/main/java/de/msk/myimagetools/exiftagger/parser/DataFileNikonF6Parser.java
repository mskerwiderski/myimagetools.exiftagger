package de.msk.myimagetools.exiftagger.parser;

import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.domain.CameraAndFilmDataRecord;
import de.msk.myimagetools.exiftagger.domain.ImageDataRecord;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoLens;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;
import de.msk.myimagetools.exiftagger.util.ExifSpecUtils;

public class DataFileNikonF6Parser extends AbstractDataFileParser {

	public DataFileNikonF6Parser(CmdLineParams cmdLineParams,
		GearInfos gearInfos) {
		super(cmdLineParams, gearInfos);
	}

	/*
	00 Frame number=02
	01 Shutter speed=250
	02 Aperture=F5.6
	03 Focal length=38(24-120)
	04 Lens maximum aperture=F4
	05 Metering system=Color matrix
	06 Exposure mode=M
	07 NOT IMPORTED - Flash sync mode=Front curtain sync
	08 Exposure compensation value=0.0
	09 NOT IMPORTED - EV difference in Manual=-8.2
	10 NOT IMPORTED - Flash exposure compensation value=0.0
	11 Speedlight setting=TTL auto flash/Optional speedlight
	12 NOT IMPORTED - Multiple exposure=None
	13 NOT IMPORTED - Lock=AE Unlock
	14 NOT IMPORTED - Vibration Reduction=VR off
	15 Date(yy/mm/dd)=2015/03/13
	16 Time=17:59
	 */

	private static final int COLS = 17;
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
		
		// fld0: '01'
		imageDataRecord.setImageNumber(Integer.valueOf(csvRecord.get(0)));
		
		// fld15 + fld16: '2007/08/07' and '15:36' --> '2007:08:07 15:36'
		String dateTimeOriginal = csvRecord.get(15) + " " + csvRecord.get(16) + ":00";
		dateTimeOriginal = StringUtils.replace(dateTimeOriginal, "/", ":");
		imageDataRecord.setDateTimeOriginal(dateTimeOriginal);
		
		// fld01: '125' --> 0,008 or '2"' --> 2,000
		String exifExposureTimeStr = csvRecord.get(1);
		String exifExposureTimeVal = "";
		if (!StringUtils.isEmpty(exifExposureTimeStr)) {
			if (StringUtils.endsWith(exifExposureTimeStr, "\"")) {
				exifExposureTimeStr = StringUtils.remove(exifExposureTimeStr, "\"");
				exifExposureTimeVal = exifExposureTimeStr;
			} else {
				exifExposureTimeVal = 
					String.valueOf(1d / Double.valueOf(exifExposureTimeStr));		
			}
		}
		imageDataRecord.setExposureTime(exifExposureTimeVal);
		
		// fld02: 'F5.6' --> '5.6'
		String exifFNumber = StringUtils.remove(csvRecord.get(2), "F");
		imageDataRecord.setAperture(exifFNumber);

		// fld04: Lens maximum aperture
		String maxApertureValue = StringUtils.remove(csvRecord.get(4), "F");
		if (!StringUtils.contains(maxApertureValue, ".")) {
			maxApertureValue += ".0";
		}
		
		// fld03: 32(17-35) or 35
		String focalLengthOrg = csvRecord.get(3);
		String focalLengthRange = focalLengthOrg;
		String focalLengthActual = focalLengthOrg;
		if (StringUtils.contains(focalLengthOrg, "(") && StringUtils.contains(focalLengthOrg, ")")) {
			focalLengthRange = StringUtils.substringBetween(focalLengthOrg, "(", ")");
			focalLengthActual = StringUtils.substringBefore(focalLengthOrg, "(");
		}
		// lensId = e.g. 'Nikon 1:4.0 24-120'
		String lensId =
			"Nikon 1:" + maxApertureValue + " " + focalLengthRange;
		GearInfoLens gearInfoLens = this.getGearInfos().getLens(lensId);
		if (gearInfoLens != null) {
			imageDataRecord.setLens(gearInfoLens);
			imageDataRecord.setFocalLength(focalLengthActual,
				gearInfoLens.getLensFocalLengthCorrFactor());
		}
		
		// fld05: Metering system
		String meteringSystem = csvRecord.get(5);
		if (StringUtils.equals(meteringSystem, "Color matrix") ||
			StringUtils.equals(meteringSystem, "Matrix")) {
			imageDataRecord.setMeteringMode(
				ExifSpecUtils.ExifMeteringMode.MultiSegment.getStrValue());
		} else if (StringUtils.equals(meteringSystem, "Center weighted")) {
			imageDataRecord.setMeteringMode(
				ExifSpecUtils.ExifMeteringMode.CenterWeightedAverage.getStrValue());
		} else if (StringUtils.equals(meteringSystem, "Spot")) {
			imageDataRecord.setMeteringMode(
				ExifSpecUtils.ExifMeteringMode.Spot.getStrValue());
		} else {
			imageDataRecord.setMeteringMode(
				ExifSpecUtils.ExifMeteringMode.Unknown.getStrValue());
		}
		
		// fld06: Exposure mode
		String exposureMode = csvRecord.get(6);
		if (StringUtils.equals(exposureMode, "M")) {
			imageDataRecord.setExposureMode(
				ExifSpecUtils.ExifExposureMode.Manual.getStrValue());
			imageDataRecord.setExposureProgram(
				ExifSpecUtils.ExifExposureProgram.Manual.getStrValue());
		} else if (StringUtils.equals(exposureMode, "P")) {
			imageDataRecord.setExposureMode(
				ExifSpecUtils.ExifExposureMode.Auto.getStrValue());
			imageDataRecord.setExposureProgram(
				ExifSpecUtils.ExifExposureProgram.ProgramAE.getStrValue());
		} else if (StringUtils.equals(exposureMode, "A")) {
			imageDataRecord.setExposureMode(
				ExifSpecUtils.ExifExposureMode.Auto.getStrValue());
			imageDataRecord.setExposureProgram(
				ExifSpecUtils.ExifExposureProgram.AperturePriorityAE.getStrValue());
		} else if (StringUtils.equals(exposureMode, "S")) {
			imageDataRecord.setExposureMode(
				ExifSpecUtils.ExifExposureMode.Auto.getStrValue());
			imageDataRecord.setExposureProgram(
				ExifSpecUtils.ExifExposureProgram.ShutterSpeedPriorityAE.getStrValue());
		} else {
			imageDataRecord.setExposureProgram(
				ExifSpecUtils.ExifExposureProgram.NotDefined.getStrValue());
		}
		
		// fld08: Exposure compensation value
		imageDataRecord.setExposureCompensation(csvRecord.get(8));
		
		// fld11: 'TTL auto flash/Optional speedlight' or 'non-TTL auto flash'
		String flash = BooleanUtils.toString(
			!StringUtils.contains(csvRecord.get(11), "non"), "1", "0");
		imageDataRecord.setFlashMode(flash);
		
		return imageDataRecord;
	}

	@Override
	protected void processCsvRecord(int recordNumber, CSVRecord record)
		throws ExifTaggerException {
		CameraAndFilmDataRecord cameraAndFilmDataRecord = 
			this.getCameraAndFilmDataRecord(); 
		Map<Integer, ImageDataRecord> imageDataRecordsMap = 
			cameraAndFilmDataRecord.getImageDataRecordsMap();
		if (recordNumber == 1) {
			cameraAndFilmDataRecord.setFilmSpeed(record.get(0));
		} else if (recordNumber > 2) {
			if (recordNumber == 3) {
				// noop.
			}
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
