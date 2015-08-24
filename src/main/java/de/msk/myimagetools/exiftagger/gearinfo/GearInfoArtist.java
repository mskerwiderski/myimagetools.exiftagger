package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class GearInfoArtist extends AbstractGearInfo {

	private String id;
	private String lastName;
	private String firstName;
	private String emailAddress;
	private String country;
	private String region;
	private String city;
	private String postalCode;
	private String street;
	private String copyrightNotice;
	private String copyrightStatus;
	private String usageTerms;
	
	public GearInfoArtist() {
		
	}

	public String getFullName() {
		String fullname = "";
		if (!StringUtils.isEmpty(this.firstName)) {
			fullname += this.firstName;
		}
		if (!StringUtils.isEmpty(this.lastName)) {
			if (!StringUtils.isEmpty(fullname)) {
				fullname += " ";
			}
			fullname += this.lastName;
		}
		return fullname;
	}
	
	@Override
	public void initAux(List<String> record, int startIdx) {
		this.id = record.get(startIdx++);
		this.lastName = record.get(startIdx++);
		this.firstName = record.get(startIdx++);
		this.emailAddress = record.get(startIdx++);
		this.country = record.get(startIdx++);
		this.region = record.get(startIdx++);
		this.city = record.get(startIdx++);
		this.postalCode = record.get(startIdx++);
		this.street = record.get(startIdx++);
		this.copyrightNotice = record.get(startIdx++);
		this.copyrightStatus = record.get(startIdx++);
		this.usageTerms = record.get(startIdx++);
	}

	@Override
	public int getColCnt() {
		return 12;
	}

	public String getDesc() {
		return this.id;
	}

	public String getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getCountry() {
		return country;
	}

	public String getRegion() {
		return region;
	}

	public String getCity() {
		return city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getStreet() {
		return street;
	}

	public String getCopyrightNotice() {
		return copyrightNotice;
	}

	public String getCopyrightStatus() {
		return copyrightStatus;
	}

	public String getUsageTerms() {
		return usageTerms;
	}

	@Override
	public String toString() {
		return "GearInfoArtist [id=" + id + ", lastName=" + lastName
			+ ", firstName=" + firstName + ", emailAddress=" + emailAddress
			+ ", country=" + country + ", region=" + region + ", city="
			+ city + ", postalCode=" + postalCode + ", street=" + street
			+ ", copyrightNotice=" + copyrightNotice + ", copyrightStatus="
			+ copyrightStatus + ", usageTerms=" + usageTerms + "]";
	}
}
