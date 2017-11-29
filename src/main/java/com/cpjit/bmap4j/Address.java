package com.cpjit.bmap4j;

import com.alibaba.fastjson.annotation.JSONField;

public class Address {

	private String country;
	private String province;
	private String city;
	private String district;
	private String town;
	private String street;
	private String streetNumber;
	private String adcode;
	private String countryCode;
	
	Address() {
	}
	
	String getCountry() {
		return country;
	}
	void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	void setDistrict(String district) {
		this.district = district;
	}
	public String getTown() {
		return town;
	}
	void setTown(String town) {
		this.town = town;
	}
	public String getStreet() {
		return street;
	}
	void setStreet(String street) {
		this.street = street;
	}
	@JSONField(name="street_number")
	public String getStreetNumber() {
		return streetNumber;
	}
	void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	public String getAdcode() {
		return adcode;
	}
	void setAdcode(String adcode) {
		this.adcode = adcode;
	}
	@JSONField(name="country_code")
	public String getCountryCode() {
		return countryCode;
	}
	void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	@Override
	public String toString() {
		return "Address [country=" + country + ", province=" + province + ", city=" + city + ", district=" + district
				+ ", town=" + town + ", street=" + street + ", streetNumber=" + streetNumber + ", adcode=" + adcode
				+ ", countryCode=" + countryCode + "]";
	}
}
