package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class GearInfoCamera extends AbstractGearInfo {

	private String cameraId;
	private String cameraMake; 
	private String cameraModel;
	private String cameraSerialNumber;
	private String cameraReleaseDate;
	private String cameraManufacturingDate;
	private String cameraKadlubeksCatalogNo;
	private String cameraAdditionalInfo;
	private String cameraFormatFactor;
	private String cameraLensId;
	
	public GearInfoCamera() {
	}

	@Override
	public int getColCnt() {
		return 10;
	}
	
	public String getMakeAndModel() {
		String makeAndModel = "";
		if (!StringUtils.isEmpty(this.cameraMake)) {
			makeAndModel += this.cameraMake;
		}
		if (!StringUtils.isEmpty(this.cameraModel)) {
			if (!StringUtils.isEmpty(makeAndModel)) {
				makeAndModel += " ";
			}
			makeAndModel += this.cameraModel;
		}
		return makeAndModel;
	}
	
	public String getDesc() {
		return this.getMakeAndModel() + " [" + this.getId() + "]";
	}
	
	@Override
	public void initAux(List<String> record, int startIdx) {
		this.cameraId = record.get(startIdx++);
		this.cameraMake = record.get(startIdx++);
		this.cameraModel = record.get(startIdx++);
		this.cameraSerialNumber = record.get(startIdx++);
		this.cameraReleaseDate = record.get(startIdx++);
		this.cameraManufacturingDate = record.get(startIdx++);
		this.cameraKadlubeksCatalogNo = record.get(startIdx++);
		this.cameraAdditionalInfo = record.get(startIdx++);
		this.cameraFormatFactor = record.get(startIdx++);
		this.cameraLensId = record.get(startIdx++);
	}

	@Override
	public String getId() {
		return this.cameraId;
	}
	
	public String getCameraId() {
		return cameraId;
	}

	public String getCameraMake() {
		return cameraMake;
	}

	public String getCameraModel() {
		return cameraModel;
	}

	public String getCameraSerialNumber() {
		return cameraSerialNumber;
	}

	public String getCameraReleaseDate() {
		return cameraReleaseDate;
	}

	public String getCameraManufacturingDate() {
		return cameraManufacturingDate;
	}

	public String getCameraKadlubeksCatalogNo() {
		return cameraKadlubeksCatalogNo;
	}

	public String getCameraAdditionalInfo() {
		return cameraAdditionalInfo;
	}

	public String getCameraFormatFactor() {
		return cameraFormatFactor;
	}

	public String getCameraLensId() {
		return cameraLensId;
	}

	@Override
	public String toString() {
		return "GearInfoCamera [cameraId=" + cameraId + ", cameraMake="
			+ cameraMake + ", cameraModel=" + cameraModel
			+ ", cameraSerialNumber=" + cameraSerialNumber
			+ ", cameraReleaseDate=" + cameraReleaseDate
			+ ", cameraManufacturingDate=" + cameraManufacturingDate
			+ ", cameraKadlubeksCatalogNo=" + cameraKadlubeksCatalogNo
			+ ", cameraAdditionalInfo=" + cameraAdditionalInfo
			+ ", cameraFormatFactor=" + cameraFormatFactor
			+ ", cameraLensId=" + cameraLensId + "]";
	}
}
