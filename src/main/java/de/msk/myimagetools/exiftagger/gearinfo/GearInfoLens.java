package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class GearInfoLens extends AbstractGearInfo {

	private String lensId;
	private String lensMake;
	private String lensModel;
	private String lensFocalLengthMin;
	private String lensFocalLengthMax;
	private String lensFocalLengthCorrFactor;
	private String lensMaxAperture;
	private String lensSerialNumber;
	private String lensReleaseDate;
	private String lensManufacturingDate;
	private String lensAdditionalInfo;
	
	public GearInfoLens() {
	}

	@Override
	public int getColCnt() {
		return 11;
	}

	public String getMakeAndModel() {
		String makeAndModel = "";
		if (!StringUtils.isEmpty(this.lensMake)) {
			makeAndModel += this.lensMake;
		}
		if (!StringUtils.isEmpty(this.lensModel)) {
			if (!StringUtils.isEmpty(makeAndModel)) {
				makeAndModel += " ";
			}
			makeAndModel += this.lensModel;
		}
		return makeAndModel;	
	}
	
	public boolean isZoom() {
		return !StringUtils.equals(
			this.getLensFocalLengthMin(), 
			this.getLensFocalLengthMax());
	}
	
	@Override
	public String getId() {
		return this.lensId;
	}

	@Override
	public String getDesc() {
		return this.getMakeAndModel() + " [" + this.getId() + "]";
	}
	
	@Override
	public void initAux(List<String> record, int startIdx) {
		this.lensId = record.get(startIdx++);
		this.lensMake = record.get(startIdx++);
		this.lensModel = record.get(startIdx++);
		this.lensFocalLengthMin = record.get(startIdx++);
		this.lensFocalLengthMax = record.get(startIdx++);
		this.lensFocalLengthCorrFactor = record.get(startIdx++);
		this.lensMaxAperture = record.get(startIdx++);
		this.lensSerialNumber = record.get(startIdx++);
		this.lensReleaseDate = record.get(startIdx++);
		this.lensManufacturingDate = record.get(startIdx++);
		this.lensAdditionalInfo = record.get(startIdx++);
	}

	public String getLensId() {
		return lensId;
	}

	public String getLensMake() {
		return lensMake;
	}

	public String getLensModel() {
		return lensModel;
	}

	public String getLensFocalLengthMin() {
		return lensFocalLengthMin;
	}

	public String getLensFocalLengthMax() {
		return lensFocalLengthMax;
	}

	public String getLensFocalLengthCorrFactor() {
		return lensFocalLengthCorrFactor;
	}

	public String getLensMaxAperture() {
		return lensMaxAperture;
	}

	public String getLensSerialNumber() {
		return lensSerialNumber;
	}

	public String getLensReleaseDate() {
		return lensReleaseDate;
	}

	public String getLensManufacturingDate() {
		return lensManufacturingDate;
	}

	public String getLensAdditionalInfo() {
		return lensAdditionalInfo;
	}

	@Override
	public String toString() {
		return "GearInfoLens [lensId=" + lensId + ", lensMake=" + lensMake
			+ ", lensModel=" + lensModel + ", lensFocalLengthMin="
			+ lensFocalLengthMin + ", lensFocalLengthMax="
			+ lensFocalLengthMax + ", lensMaxAperture=" + lensMaxAperture
			+ ", lensSerialNumber=" + lensSerialNumber
			+ ", lensReleaseDate=" + lensReleaseDate
			+ ", lensManufacturingDate=" + lensManufacturingDate
			+ ", lensAdditionalInfo=" + lensAdditionalInfo + "]";
	}
}
