package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoDevelopmentHardware extends AbstractGearInfo {

	private static final long serialVersionUID = -3710274395456616023L;

	private String developmentHardware;
	
	public GearInfoDevelopmentHardware() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.developmentHardware = record.get(startIdx);
	}

	public String getDesc() {
		return this.developmentHardware;
	}

	@Override
	public String toString() {
		return "GearInfoDevelopmentHardware [developmentHardware=" + developmentHardware + "]";
	}
}
