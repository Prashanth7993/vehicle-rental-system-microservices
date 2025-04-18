package com.project.vendor_service.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.naming.java.javaURLContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.vendor_service.feign.AuthClient;
import com.project.vendor_service.feign.VehicleClient;
import com.project.vendor_service.models.RolePojo;
import com.project.vendor_service.models.UserCredentialPojo;
import com.project.vendor_service.models.VehiclePojo;
import com.project.vendor_service.models.VendorPojo;
import com.project.vendor_service.models.VendorWrapper;
import com.project.vendor_service.service.VendorService;

//@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173")
@RestController
@RequestMapping("/vendor")

public class VendorController {
	@Autowired
	private VendorService vendorService;

	@Autowired
	private VehicleClient vehicleClient;
	
	@Autowired
	private AuthClient authClient;

	@GetMapping("/greet")
	public String greet() {
		return "Hello! From Vendor Service..............";
	}

	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
	public ResponseEntity<Void> handlePreflightRequest() {
		return ResponseEntity.ok().header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5173")
				.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, PATCH")
				.header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, Content-Type")
				.header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true").build();
	}

	@GetMapping
	public ResponseEntity<?> getAllVendors() {
		return new ResponseEntity<>(vendorService.getAllVendors(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getVendorById(@PathVariable long id) {
		VendorPojo vendorFound = vendorService.getVendorById(id);
		if (vendorFound != null) {
			List<VehiclePojo> vehiclesOfVendor = vehicleClient.getAllVehiclesByVendorId(id);
			vendorFound.setVehicles(vehiclesOfVendor);
			return new ResponseEntity<>(vendorFound, HttpStatus.OK);
		}
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<?> getVendorByEmail(@PathVariable String email) {
		VendorPojo vendorFound = vendorService.getVendorByEmail(email);
		if (vendorFound != null) {
			return new ResponseEntity<>(vendorFound, HttpStatus.OK);
		}
		return ResponseEntity.noContent().build();

	}

	@GetMapping("/name/{name}")
	public ResponseEntity<?> getVendorByName(@PathVariable String name) {
		VendorPojo vendorFound = vendorService.getVendorByName(name);
		if (vendorFound != null) {
			return new ResponseEntity<>(vendorFound, HttpStatus.OK);
		}
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/id-name/vendors")
	public ResponseEntity<?> getVendorDetails() {
		List<VendorWrapper> vendors = vendorService.getVendorDetails();
		return new ResponseEntity<>(vendors, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> addNewVendor(@RequestBody VendorPojo vendorPojo) {
		boolean vendorExists = vendorService.checkIfVendorExists(vendorPojo.getEmail());
		if (vendorExists) {
			return new ResponseEntity<>(Map.entry("message", "Vendor Already Exists with the given mail"),
					HttpStatus.OK);
		}
		VendorPojo vendor=vendorService.addVendor(vendorPojo);
		//Registering Vendor in AuthService to Enable them to login to the Dashboard,
		RolePojo rolePojo=new RolePojo();
		if(vendor!=null) {
			UserCredentialPojo vendorCredentialPojo=new UserCredentialPojo();
			rolePojo.setName("VENDOR");
			vendorCredentialPojo.setUsername(vendorPojo.getEmail());
			vendorCredentialPojo.setPassword(vendorPojo.getPassword());
			vendorCredentialPojo.setRoles(Collections.singletonList(rolePojo));
			authClient.registerNewUser(vendorCredentialPojo);
			return new ResponseEntity<>(vendor, HttpStatus.OK);
		}
		return ResponseEntity.badRequest().build();
		
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateVendorById(@PathVariable long id, @RequestBody VendorPojo updatedVendorPojo) {
		VendorPojo updatedVendor = vendorService.updateVendorById(id, updatedVendorPojo);
		return new ResponseEntity<>(updatedVendor, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteVendorById(@PathVariable long id) {
		boolean deleted = vendorService.deleteVendorById(id);
		return new ResponseEntity<>(deleted, HttpStatus.OK);
	}

}
