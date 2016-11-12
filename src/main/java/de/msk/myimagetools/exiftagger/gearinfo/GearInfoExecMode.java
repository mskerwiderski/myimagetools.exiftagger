package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoExecMode extends AbstractGearInfo {

	private static final long serialVersionUID = 2519474677310139582L;

	private String execModeId;
	private String execModeDesc;
	
	public enum execModeType {
		Save,
		Write,
		SaveAndWrite
	}
	
	public GearInfoExecMode() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.execModeId = record.get(startIdx++);
		this.execModeDesc = record.get(startIdx++);
	}

	@Override
	public int getColCnt() {
		return 2;
	}

	@Override
	public String getId() {
		return this.execModeId;
	}

	public String getDesc() {
		return this.execModeDesc;
	}

	@Override
	public String toString() {
		return "GearInfoExecMode [execModeId=" + execModeId + ", execModeDesc="
			+ execModeDesc + "]";
	}
}
