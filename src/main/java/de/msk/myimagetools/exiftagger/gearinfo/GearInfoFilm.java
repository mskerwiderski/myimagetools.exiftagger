package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoFilm extends AbstractGearInfo {

	private static final long serialVersionUID = 1803548820103161505L;

	private String filmId;
	private String filmName;
	
	public GearInfoFilm() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.filmId = record.get(startIdx++);
		this.filmName = record.get(startIdx++);
	}

	@Override
	public int getColCnt() {
		return 2;
	}

	@Override
	public String getId() {
		return this.filmId;
	}

	public String getDesc() {
		return this.filmName;
	}

	@Override
	public String toString() {
		return "GearInfoFilm [filmId=" + filmId + ", filmName=" + filmName
			+ "]";
	}
}
