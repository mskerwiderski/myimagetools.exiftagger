package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoDevelopmentLaboratory extends AbstractGearInfo {

	private static final long serialVersionUID = 7258458485989605792L;

	private String developmentlaboratory;
	
	public GearInfoDevelopmentLaboratory() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.developmentlaboratory = record.get(startIdx);
	}

	public String getDesc() {
		return this.developmentlaboratory;
	}

	@Override
	public String toString() {
		return "GearInfoDevelopmentLaboratory [developmentlaboratory=" + developmentlaboratory + "]";
	}
}
