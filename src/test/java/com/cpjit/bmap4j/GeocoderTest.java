package com.cpjit.bmap4j;

import org.junit.Test;

public class GeocoderTest {
	
	@Test
	public void test() throws Bmap4jException {
		Geocoder geocoder = new Geocoder("qIvsQOWaRi2Oov6dlVYpeDCAk3mQQqK5", "DP9EUX4uKW5h8U4KTSDyUR3Hr4oZZZWg");
		LngLat lngLat = geocoder.addressToLocation("双流国际机场");
		System.out.println(lngLat);
		
		Address address = geocoder.locationToAddress(lngLat);
		System.out.println(address);
	}
}
