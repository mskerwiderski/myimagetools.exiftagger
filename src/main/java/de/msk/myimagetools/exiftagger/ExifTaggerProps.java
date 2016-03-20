package de.msk.myimagetools.exiftagger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class ExifTaggerProps {

	private static final String EXIFTAGGER_CFG = "exiftagger.cfg";
	
	public enum Keyword {
		camera,
		lens,
		film,
		developer,
		digitizingHardware,
		developmentPullPushFstops,
		tagsFromDataFile
	}
	
	private String rollIdRegEx;
	private String rollIdFormat;
	private String rollIdExample;
	private String dateRegEx;
	private String dateFormat;
	private String dateExample;
	private String monthYearRegEx;
	private String monthYearFormat;
	private String monthYearExample;
	private String keywords;
	
	private static final String YEAR_CURRENT = "$YEAR";
	private static final String MONTH_CURRENT = "$MONTH";
	private static final String DAY_OF_MONTH_CURRENT = "$DAY_OF_MONTH";
	private static Calendar CALENDAR = Calendar.getInstance();
	
	private static String parsePlaceholders(String propStr) {
		propStr = StringUtils.replace(propStr, YEAR_CURRENT, 
			String.valueOf(CALENDAR.get(Calendar.YEAR)));
		propStr = StringUtils.replace(propStr, MONTH_CURRENT, 
			StringUtils.leftPad(String.valueOf(CALENDAR.get(Calendar.MONTH)), 2, '0'));
		propStr = StringUtils.replace(propStr, DAY_OF_MONTH_CURRENT, 
			StringUtils.leftPad(String.valueOf(CALENDAR.get(Calendar.DAY_OF_MONTH)), 2, '0'));
		return propStr;
	}
	
	public static ExifTaggerProps load(String configDir) throws ExifTaggerException {
		if (configDir == null) {
			configDir = "";
		}
		ExifTaggerProps props = new ExifTaggerProps();
		FileInputStream fileInputStream = null;
		try {
			Properties propsFile = new Properties();
			fileInputStream = new FileInputStream(configDir + EXIFTAGGER_CFG);
			propsFile.load(fileInputStream);
			props.rollIdRegEx = propsFile.getProperty("rollIdRegEx");
			props.rollIdFormat = propsFile.getProperty("rollIdFormat");
			props.rollIdExample = parsePlaceholders(propsFile.getProperty("rollIdExample"));
			props.dateRegEx = propsFile.getProperty("dateRegEx");
			props.dateFormat = propsFile.getProperty("dateFormat");
			props.dateExample = parsePlaceholders(propsFile.getProperty("dateExample"));
			props.monthYearRegEx = propsFile.getProperty("monthYearRegEx");
			props.monthYearFormat = propsFile.getProperty("monthYearFormat");
			props.monthYearExample = parsePlaceholders(propsFile.getProperty("monthYearExample"));
			props.keywords = propsFile.getProperty("keywords");
		} catch (Exception e) {
			throw new ExifTaggerException(e);
		} finally {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				// noop.
			}
		}
		return props;
	} 
	
	private ExifTaggerProps() {
	}

	public boolean isKeyword(Keyword keyword) {
		return StringUtils.contains(this.keywords, keyword.name());
	}
	
	public String getRollIdRegEx() {
		return rollIdRegEx;
	}

	public String getRollIdFormat() {
		return rollIdFormat;
	}

	public String getRollIdExample() {
		return rollIdExample;
	}

	public String getDateRegEx() {
		return dateRegEx;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public String getDateExample() {
		return dateExample;
	}

	public String getMonthYearRegEx() {
		return monthYearRegEx;
	}

	public String getMonthYearFormat() {
		return monthYearFormat;
	}

	public String getMonthYearExample() {
		return monthYearExample;
	}

	@Override
	public String toString() {
		return "ExifTaggerProps [rollIdRegEx=" + rollIdRegEx
			+ ", rollIdFormat=" + rollIdFormat + ", rollIdExample="
			+ rollIdExample + ", dateRegEx=" + dateRegEx + ", dateFormat="
			+ dateFormat + ", dateExample=" + dateExample
			+ ", monthYearRegEx=" + monthYearRegEx + ", monthYearFormat="
			+ monthYearFormat + ", monthYearExample=" + monthYearExample
			+ ", keywords=" + keywords + "]";
	}
}
