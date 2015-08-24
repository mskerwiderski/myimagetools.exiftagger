package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoDevelopmentProcess extends AbstractGearInfo {

	private String processName;
	
	public GearInfoDevelopmentProcess() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.processName = record.get(startIdx);
	}

	public String getDesc() {
		return this.processName;
	}

	@Override
	public String toString() {
		return "GearInfoDevelopmentProcess [processName=" + processName + "]";
	}
}
