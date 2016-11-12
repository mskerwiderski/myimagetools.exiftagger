package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoDeveloper extends AbstractGearInfo {

	private static final long serialVersionUID = 599632118287020672L;

	private String developerName;
	
	public GearInfoDeveloper() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.developerName = record.get(startIdx);
	}

	public String getDesc() {
		return this.developerName;
	}

	@Override
	public String toString() {
		return "GearInfoDeveloper [developerName=" + developerName + "]";
	}
}
