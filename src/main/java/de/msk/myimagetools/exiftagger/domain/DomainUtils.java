package de.msk.myimagetools.exiftagger.domain;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoCamera;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoLens;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;

public class DomainUtils {

	private DomainUtils() {
	}

	public static GearInfoCamera getCamera(
		CameraAndFilmDataRecord cameraAndFilmDataRecord, 
		ImageDataRecord imageDataRecord) {
		if (cameraAndFilmDataRecord == null) {
			throw new IllegalArgumentException(
				"cameraAndFilmDataRecord must not be null.");
		}
		GearInfoCamera camera = 
			cameraAndFilmDataRecord.getCamera();
		if ((imageDataRecord != null) && (imageDataRecord.getCamera() != null)) {
			camera = imageDataRecord.getCamera();
		}
		return camera;
	}
	
	public static GearInfoLens getLens(GearInfos gearInfos,
		CameraAndFilmDataRecord cameraAndFilmDataRecord, 
		ImageDataRecord imageDataRecord) {
		if (cameraAndFilmDataRecord == null) {
			throw new IllegalArgumentException(
				"cameraAndFilmDataRecord must not be null.");
		}
		GearInfoLens lens = null;
		GearInfoCamera camera = 
			getCamera(cameraAndFilmDataRecord,imageDataRecord);
		if ((camera != null) && !StringUtils.isEmpty(camera.getCameraLensId())) {
			lens = gearInfos.getLens(camera.getCameraLensId());
		}
		if ((imageDataRecord != null) && (imageDataRecord.getLens() != null)) {
			lens = imageDataRecord.getLens();
		}
		return lens;
	}
	
	public static String[] getFocalLengths(
		GearInfos gearInfos,
		CameraAndFilmDataRecord cameraAndFilmDataRecord, 
		ImageDataRecord imageDataRecord) 
		throws ExifTaggerException {
		String[] focalLengths = new String[2];
		GearInfoCamera camera = 
			getCamera(cameraAndFilmDataRecord,imageDataRecord);
		String focalLengthOrg = null;
		if (imageDataRecord != null) {
			focalLengthOrg = imageDataRecord.getFocalLength();
		}
		String focalLength35mm = null;
		if (camera != null) {
			if (StringUtils.isEmpty(focalLengthOrg) &&
				!StringUtils.isEmpty(camera.getCameraLensId())) {
				GearInfoLens lens = gearInfos.getLens(
					camera.getCameraLensId());
				if (!lens.isZoom()) {
					focalLengthOrg = lens.getLensFocalLengthMin();
				}
			}
			if (!StringUtils.isEmpty(focalLengthOrg) && 
				(camera.getCameraFormatFactor() != null)) {
				double focalLengthOrgDbl = 0.0d;
				double formatFactorDbl = 0.0d;
				NumberFormat format = NumberFormat.getInstance(Locale.US);
				try {
					formatFactorDbl = format.parse(camera.getCameraFormatFactor()).doubleValue();
					if (formatFactorDbl == 1.0d) {
						focalLength35mm = focalLengthOrg;
					} else {
						focalLengthOrgDbl = format.parse(focalLengthOrg).doubleValue();
						focalLength35mm = String.valueOf(
							Math.round(new Double((formatFactorDbl * focalLengthOrgDbl) / 5d)) * 5);
					}
				} catch (ParseException e) {
					throw new ExifTaggerException(e);
				}
			}
		}
		focalLengths[0] = focalLengthOrg;
		focalLengths[1] = focalLength35mm;
		return focalLengths;
	}
}
