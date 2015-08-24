package de.msk.myimagetools.exiftagger.domain;

import java.util.Map;

import de.msk.myimagetools.exiftagger.gearinfo.GearInfoArtist;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoCamera;

public class CameraAndFilmDataRecord {

	private GearInfoArtist artist;
	private GearInfoCamera camera;
	private String filmSpeed;
	private String filmName;
	private Map<Integer, ImageDataRecord>
		imageDataRecordsMap = null;
	private HybridProcessRecord
		hybridProcessRecord = null;

	public GearInfoArtist getArtist() {
		return artist;
	}

	public void setArtist(GearInfoArtist artist) {
		this.artist = artist;
	}

	public GearInfoCamera getCamera() {
		return camera;
	}
	
	public void setCamera(GearInfoCamera camera) {
		this.camera = camera;
	}
	
	public String getFilmSpeed() {
		return filmSpeed;
	}
	
	public void setFilmSpeed(String filmSpeed) {
		this.filmSpeed = filmSpeed;
	}
	
	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	public Map<Integer, ImageDataRecord> getImageDataRecordsMap() {
		return imageDataRecordsMap;
	}
	
	public void setImageDataRecordsMap(
		Map<Integer, ImageDataRecord> imageDataRecordsMap) {
		this.imageDataRecordsMap = imageDataRecordsMap;
	}
	
	public HybridProcessRecord getHybridProcessRecord() {
		return hybridProcessRecord;
	}

	public void setHybridProcessRecord(HybridProcessRecord hybridProcessRecord) {
		this.hybridProcessRecord = hybridProcessRecord;
	}

	@Override
	public String toString() {
		return "CameraAndFilmDataRecord [artist=" + artist + ", camera="
			+ camera + ", filmSpeed=" + filmSpeed + ", filmName="
			+ filmName + ", imageDataRecordsMap=" + imageDataRecordsMap
			+ ", hybridProcessRecord=" + hybridProcessRecord + "]";
	}
}
