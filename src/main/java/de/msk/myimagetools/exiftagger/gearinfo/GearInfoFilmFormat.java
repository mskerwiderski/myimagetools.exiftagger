package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoFilmFormat extends AbstractGearInfo {

	private String filmFormat;
	
	public GearInfoFilmFormat() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.filmFormat = record.get(startIdx);
	}

	public String getDesc() {
		return this.filmFormat;
	}

	@Override
	public String toString() {
		return "GearInfoFilmFormat [filmFormat=" + filmFormat + "]";
	}
}
