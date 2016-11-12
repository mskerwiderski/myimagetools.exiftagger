package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoHardware extends AbstractGearInfo {

	private static final long serialVersionUID = -1715780859892390686L;

	private String hardware;
	
	public GearInfoHardware() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.hardware = record.get(startIdx);
	}

	public String getDesc() {
		return this.hardware;
	}

	@Override
	public String toString() {
		return "GearInfoHardware [hardware=" + hardware + "]";
	}
}
