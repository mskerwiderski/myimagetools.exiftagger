package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoTag extends AbstractGearInfo {

	private static final long serialVersionUID = -5004586110595660056L;

	private String tagId;
	private String tagName;
	
	public GearInfoTag() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.tagId = record.get(startIdx++);
		this.tagName = record.get(startIdx++);
	}

	@Override
	public int getColCnt() {
		return 2;
	}

	@Override
	public String getId() {
		return this.tagId;
	}

	public String getDesc() {
		return this.tagName;
	}

	@Override
	public String toString() {
		return "GearInfoTag [tagId=" + tagId + ", tagName=" + tagName
			+ "]";
	}
}
