package de.msk.myimagetools.exiftagger.parser;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.domain.ImageDataRecord;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoTag;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;
import de.msk.myimagetools.exiftagger.util.csv.CsvUtils;

public class ParserUtils {

	private ParserUtils() {
	}

	public static void parseComment(GearInfos gearInfos, ImageDataRecord imageDataRecord, String comment) 
		throws ExifTaggerException {
		if (gearInfos == null) {
			throw new IllegalArgumentException("gearInfos must not be null!");
		}
		if (imageDataRecord == null) {
			throw new IllegalArgumentException("imageDataRecord must not be null!");
		}
		if (!StringUtils.isEmpty(comment)) {
			List<String> values = CsvUtils.readValuesFromOneRecord(comment);
			for (String value : values) {
				if (StringUtils.startsWith(value, "%")) {
					imageDataRecord.setTitle(StringUtils.substring(value, 1));
				} else if (StringUtils.startsWith(value, "+")) {
					imageDataRecord.setCaptionAbstract(StringUtils.substring(value, 1));
				} else if (StringUtils.startsWith(value, "*")) {
					imageDataRecord.setFlashMode("1");
				} else if (StringUtils.startsWith(value, "#")) {
					String tagStr = StringUtils.substring(value, 1);
					GearInfoTag tag = gearInfos.getTag(tagStr);
					if (tag != null) {
						imageDataRecord.addTag(tag.getDesc());
					} else {
						imageDataRecord.addTag(tagStr);
					}
				}
			}
		}
	}
}
