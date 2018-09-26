package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;

	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "licenseNumber", required = true) String licenseNumber,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "flyHour", required = true) int flyHour) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		return "add";
	}

	@RequestMapping("/pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);

		model.addAttribute("pilot", archive);
		return "view-pilot";
	}

	@RequestMapping("/pilot/viewall")
	public String viewall(Model model) {
		List<PilotModel> archive = pilotService.getPilotList();

		model.addAttribute("listPilot", archive);
		return "viewall-pilot";
	}

	@RequestMapping(value = { "/pilot/view/license-number", "pilot/view/license-number/{licenseNumber}" })
	public String viewPilot(@PathVariable Optional<String> licenseNumber, Model model) {
		if (licenseNumber.isPresent()) {
			List<PilotModel> archive = pilotService.getPilotList();

			for (PilotModel pilot : archive) {
				if (pilot.getLicenseNumber().equals(licenseNumber.get())) {
					model.addAttribute("pilot", pilot);
					return "view-pilot";
				}
			}
		}
		return "view-error";
	}

	@RequestMapping(value = {"/pilot/update/fly-hour/{flyHour}",
			"/pilot/update/license-number/{licenseNumber}/fly-hour",
			"/pilot/update/license-number/{licenseNumber}/fly-hour/{flyHour}" })
	public String update(@PathVariable Optional<String> licenseNumber, @PathVariable Integer flyHour, Model model) {
		
		if (licenseNumber.isPresent()) {
			if (pilotService.getPilotDetailByLicenseNumber(licenseNumber.get()) == null) {
				return "update-error";
			}

			pilotService.update(licenseNumber.get(), flyHour);
			return "update";
		}
		return "update-error";
	}
	
	@RequestMapping(value = {"/pilot/delete","/pilot/delete/id", "/pilot/delete/id/{id}" })
	public String delete(@PathVariable Optional<String> id, Model model) {
		if(id.isPresent()) {
			if (pilotService.getPilotDetailById(id.get()) == null) {
				return "delete-error";
			}
			pilotService.delete(id.get());
			return "delete";
		}
		return "delete-error";
	}
	

}
