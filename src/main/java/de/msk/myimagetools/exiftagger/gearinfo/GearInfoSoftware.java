package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoSoftware extends AbstractGearInfo {

	private static final long serialVersionUID = 8889123821874863303L;

	private String software;
	
	public GearInfoSoftware() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.software = record.get(startIdx);
	}

	public String getDesc() {
		return this.software;
	}

	@Override
	public String toString() {
		return "GearInfoSoftware [software=" + software + "]";
	}
}
