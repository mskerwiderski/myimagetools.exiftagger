package de.msk.myimagetools.exiftagger.util;

import org.apache.commons.lang3.StringUtils;

public class ExifSpecUtils {
	public static final String NAMESPACE_EXIF = "exif";
	public static final String NAMESPACE_XMP = "xmp";
	public static final String NAMESPACE_IPTC = "iptc";
	public static final String NAMESPACE_IPTCCORE = "xmp-iptccore";
	public static final String NAMESPACE_MSK = "xmp-msk";
	public static final String NAMESPACE_MSK_HP = "xmp-mskhp";
	public static final String NAMESPACE_MSK_GI = "xmp-mskgi";

	public static final String EXIF_NO_VALUE = "./.";

	private ExifSpecUtils() {
	}
	
	public enum ExifCopyrightStatus {
		Unknown("CS-UNK"), 
		Protected("CS-PRO"), 
		PublicDomain("CS-PUB");
		private String value;
		private ExifCopyrightStatus(String value) {
			this.value = value;
		}
		public String getStrValue() {
			return this.value;
		}
		public static String getValueByName(String name) {
			String status = Unknown.value;
			if (StringUtils.isEmpty(name)) {
				throw new IllegalArgumentException("name must not be null.");
			}
			if (StringUtils.equals(name, Protected.name())) {
				return Protected.value;
			} else if (StringUtils.equals(name, PublicDomain.name())) {
				return PublicDomain.value;
			} 
			return status;
		}
	};
	
	public enum ExifExposureMode {
		Auto(0), 
		Manual(1), 
		AutoBracket(2);
		private int value;
		private ExifExposureMode(int value) {
			this.value = value;
		}
		public String getStrValue() {
			return String.valueOf(this.value);
		}
	};
	
	public enum ExifMeteringMode {
		Unknown(0), 
		Average(1), 
		CenterWeightedAverage(2),
		Spot(3), 
		MultiSpot(4),
		MultiSegment(5), 
		Partial(6),
		Other(255);
		private int value;
		private ExifMeteringMode(int value) {
			this.value = value;
		}
		public String getStrValue() {
			return String.valueOf(this.value);
		}
	};
	
	public enum ExifExposureProgram {
		NotDefined(0), 
		Manual(1), 
		ProgramAE(2), 
		AperturePriorityAE(3), 
		ShutterSpeedPriorityAE(4), 
		CreativeSlowSpeed(5),
		ActionHighSpeed(6),
		Portrait(7),
		Landscape(8),
		Bulb(9);
		private int value;
		private ExifExposureProgram(int value) {
			this.value = value;
		}
		public String getStrValue() {
			return String.valueOf(this.value);
		}		
	}
}
