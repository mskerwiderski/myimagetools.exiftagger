package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoHybridProcess extends AbstractGearInfo {

	public enum HybridProcessType {
		FilmSelfNegative,
		FilmSelfSlide,
		FilmLabNegative,
		FilmLabSlide,
		InstantFilm
	}
	
	private String hybridProcessId;
	private String hybridProcessDesc;
	
	public GearInfoHybridProcess() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.hybridProcessId = record.get(startIdx++);
		this.hybridProcessDesc = record.get(startIdx++);
	}

	@Override
	public int getColCnt() {
		return 2;
	}

	@Override
	public String getId() {
		return this.hybridProcessId;
	}

	public String getDesc() {
		return this.hybridProcessDesc;
	}

	@Override
	public String toString() {
		return "GearInfoHybridProcess [hybridProcessId=" + hybridProcessId
			+ ", hybridProcessDesc=" + hybridProcessDesc + "]";
	}
}
