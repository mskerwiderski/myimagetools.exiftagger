package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoDevelopmentAgitations extends AbstractGearInfo {

	private static final long serialVersionUID = -2826377380399536712L;

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
