package de.msk.myimagetools.exiftagger.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import de.msk.myimagetools.exiftagger.gearinfo.GearInfoCamera;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoLens;

public class ImageDataRecord {
	public static class GpsPosition {
		private String latitude;
		private String latitudeRef;
		private String longitude;
		private String longitudeRef;
		public GpsPosition(String latitude, String latitudeRef,
				String longitude, String longitudeRef) {
			this.latitude = latitude;
			this.latitudeRef = latitudeRef;
			this.longitude = longitude;
			this.longitudeRef = longitudeRef;
		}
		public String getLatitude() {
			return latitude;
		}
		public String getLatitudeRef() {
			return latitudeRef;
		}
		public String getLongitude() {
			return longitude;
		}
		public String getLongitudeRef() {
			return longitudeRef;
		}
		@Override
		public String toString() {
			return "GpsPosition [latitude=" + latitude + ", latitudeRef="
				+ latitudeRef + ", longitude=" + longitude
				+ ", longitudeRef=" + longitudeRef + "]";
		}
	}
	
	private Integer imageNumber;
	private String title;
	private String captionAbstract;
	private String dateTimeOriginal;
	private String exposureTime;
	private String aperture;
	private String focalLength;
	private String flashMode;
	private String meteringMode;
	private String exposureMode;
	private String exposureProgram;
	private String exposureCompensation;
	private GpsPosition gpsPosition = null;
	private GearInfoCamera camera = null;
	private GearInfoLens lens = null;
	private List<String> tags = new ArrayList<String>();

	public Integer getImageNumber() {
		return imageNumber;
	}
	
	public void setImageNumber(Integer imageNumber) {
		this.imageNumber = imageNumber;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCaptionAbstract() {
		return captionAbstract;
	}

	public void setCaptionAbstract(String captionAbstract) {
		this.captionAbstract = captionAbstract;
	}

	public String getDateTimeOriginal() {
		return dateTimeOriginal;
	}
	
	public void setDateTimeOriginal(String dateTimeOriginal) {
		this.dateTimeOriginal = dateTimeOriginal;
	}
	
	public String getExposureTime() {
		return exposureTime;
	}
	
	public void setExposureTime(String exposureTime) {
		this.exposureTime = exposureTime;
	}
	
	public String getAperture() {
		return aperture;
	}
	
	public void setAperture(String aperture) {
		this.aperture = aperture;
	}
	
	public String getFocalLength() {
		return focalLength;
	}

	public void setFocalLength(String focalLength, String correctionFactor) {
		String focalLengthCorrected = focalLength;
		if (!StringUtils.isEmpty(focalLength) && 
			!StringUtils.isEmpty(correctionFactor) && 
			NumberUtils.isNumber(focalLength) &&
			NumberUtils.isNumber(correctionFactor)) {
			focalLengthCorrected = 
				String.valueOf(
					NumberUtils.createInteger(focalLength) + 
					NumberUtils.createInteger(correctionFactor));
		}
		this.focalLength = focalLengthCorrected;
	}

	public String getFlashMode() {
		return flashMode;
	}
	
	public void setFlashMode(String flashMode) {
		this.flashMode = flashMode;
	}
	
	public String getMeteringMode() {
		return meteringMode;
	}
	
	public void setMeteringMode(String meteringMode) {
		this.meteringMode = meteringMode;
	}
	
	public String getExposureMode() {
		return exposureMode;
	}
	
	public void setExposureMode(String exposureMode) {
		this.exposureMode = exposureMode;
	}
	
	public String getExposureProgram() {
		return exposureProgram;
	}
	
	public void setExposureProgram(String exposureProgram) {
		this.exposureProgram = exposureProgram;
	}
	
	public String getExposureCompensation() {
		return exposureCompensation;
	}
	
	public void setExposureCompensation(String exposureCompensation) {
		this.exposureCompensation = exposureCompensation;
	}
	
	public GpsPosition getGpsPosition() {
		return gpsPosition;
	}

	public void setGpsPosition(GpsPosition gpsPosition) {
		this.gpsPosition = gpsPosition;
	}

	public GearInfoCamera getCamera() {
		return camera;
	}
	
	public void setCamera(GearInfoCamera camera) {
		this.camera = camera;
	}
	
	public GearInfoLens getLens() {
		return lens;
	}
	
	public void setLens(GearInfoLens lens) {
		this.lens = lens;
	}

	public List<String> getTags() {
		return tags;
	}

	public void addTag(String tag) {
		if (StringUtils.isEmpty(tag)) {
			throw new IllegalArgumentException("tag must not be empty.");
		}
		this.tags.add(tag);
	}

	public void addTags(String... tags) {
		for (String tag : tags) {
			this.addTag(tag);
		}
	}
	
	@Override
	public String toString() {
		return "ImageDataRecord [imageNumber=" + imageNumber + ", title="
			+ title + ", captionAbstract=" + captionAbstract
			+ ", dateTimeOriginal=" + dateTimeOriginal + ", exposureTime="
			+ exposureTime + ", aperture=" + aperture + ", focalLength="
			+ focalLength + ", flashMode=" + flashMode + ", meteringMode="
			+ meteringMode + ", exposureMode=" + exposureMode
			+ ", exposureProgram=" + exposureProgram
			+ ", exposureCompensation=" + exposureCompensation
			+ ", gpsPosition=" + gpsPosition + ", camera=" + camera
			+ ", lens=" + lens + ", tags=" + tags + "]";
	}
}
