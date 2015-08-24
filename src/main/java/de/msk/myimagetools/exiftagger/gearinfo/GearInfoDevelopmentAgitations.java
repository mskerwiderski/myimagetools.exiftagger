package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoDevelopmentAgitations extends AbstractGearInfo {

	private String developerAgitations;
	
	public GearInfoDevelopmentAgitations() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.developerAgitations = record.get(startIdx);
	}

	public String getDesc() {
		return this.developerAgitations;
	}

	@Override
	public String toString() {
		return "GearInfoDeveloperAgitations [developerAgitations=" + developerAgitations + "]";
	}
}
