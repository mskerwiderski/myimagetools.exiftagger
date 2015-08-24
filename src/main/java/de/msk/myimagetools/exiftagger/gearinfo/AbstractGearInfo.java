package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractGearInfo implements Comparable<AbstractGearInfo> {
	public void init(CSVRecord record) {
		List<String> values = new ArrayList<String>();
		if (record == null) {
			throw new IllegalArgumentException("record must not be null.");
		}
		if (record.size() != this.getColCnt()) {
			throw new IllegalArgumentException("invalid record size of '" + record.size() + "'.");
		}
		for (int idx=0; idx < record.size(); idx++) {
			values.add(StringUtils.trimToEmpty(record.get(idx)));
		}
		this.initAux(values, 0);
	}
	public int getColCnt() {
		return 1;
	}
	public abstract void initAux(List<String> record, int startIdx);
	public String getId() {
		return null;
	}
	public abstract String getDesc();
	public int compareTo(AbstractGearInfo other) {
		return this.getDesc().compareTo(other.getDesc());
	}
}
