package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoInstantFilm extends AbstractGearInfo {

	private String instantFilmId;
	private String instantFilmName;
	
	public GearInfoInstantFilm() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.instantFilmId = record.get(startIdx++);
		this.instantFilmName = record.get(startIdx++);
	}

	@Override
	public int getColCnt() {
		return 2;
	}

	@Override
	public String getId() {
		return this.instantFilmId;
	}

	public String getDesc() {
		return this.instantFilmName;
	}

	@Override
	public String toString() {
		return "GearInfoInstantFilm [instantFilmId=" + instantFilmId
			+ ", instantFilmName=" + instantFilmName + "]";
	}
}
