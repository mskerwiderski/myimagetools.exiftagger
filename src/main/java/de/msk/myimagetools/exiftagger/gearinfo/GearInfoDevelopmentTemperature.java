package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoDevelopmentTemperature extends AbstractGearInfo {

	private String developmentTemperature;
	
	public GearInfoDevelopmentTemperature() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.developmentTemperature = record.get(startIdx);
	}

	public String getDesc() {
		return this.developmentTemperature;
	}

	@Override
	public String toString() {
		return "GearInfoDevelopmentTemperature [developmentTemperature=" + developmentTemperature + "]";
	}
}
