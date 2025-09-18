package application;

import application.model.*;
import application.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import view.*;

@Controller
public class ControllerPrescriptionCreate {

	@Autowired
	PrescriptionRepository prescriptionRepository;

	@Autowired
	DrugRepository drugRepository;

	@Autowired
	DoctorRepository doctorRepository;

	@Autowired
	PatientRepository patientRepository;

	@Autowired
	SequenceService sequence;
	
	/*
	 * Doctor requests blank form for new prescription.
	 */
	@GetMapping("/prescription/new")
	public String getPrescriptionForm(Model model) {
		model.addAttribute("prescription", new PrescriptionView());
		return "prescription_create";
	}

	// process data entered on prescription_create form
	@PostMapping("/prescription")
	public String createPrescription(PrescriptionView prescription, Model model) {

		System.out.println("createPrescription " + prescription);

		// Copying data from PrescriptionView
		Prescription prescriptionI = new Prescription();

		// Validating doctor
		if(doctorRepository.findByIdAndFirstNameAndLastName(prescription.getDoctorId(), prescription.getDoctorFirstName(), prescription.getDoctorLastName()) == null){
			model.addAttribute("message", "Doctor not found");
			model.addAttribute("prescription", prescription);
			return "prescription_create";
		}
		prescriptionI.setDoctorId(prescription.getDoctorId());

		// Validating patient
		if(patientRepository.findByIdAndFirstNameAndLastName(prescription.getPatientId(), prescription.getPatientFirstName(), prescription.getPatientLastName()) == null){
			model.addAttribute("message", "Patient not found");
			model.addAttribute("prescription", prescription);
			return "prescription_create";
		}
		prescriptionI.setPatientId(prescription.getPatientId());

		// Validating drug
		if(drugRepository.findByName(prescription.getDrugName()) == null){
			model.addAttribute("message", "Drug not found");
			model.addAttribute("prescription", prescription);
			return "prescription_create";
		}
		prescriptionI.setDrugName(prescription.getDrugName());

		// Populating other fields in prescription
		prescriptionI.setQuantity(prescription.getQuantity());
		prescriptionI.setRefills(prescription.getRefills());

		// Getting the next unique ID for prescriptions and setting
		int id = sequence.getNextSequence("PRESCRIPTION_SEQUENCE");
		prescriptionI.setRxid(id);
		prescription.setRxid(id);

		prescriptionRepository.insert(prescriptionI);
		// Displaying prescription message
		model.addAttribute("message", "Prescription created successfully.");
		model.addAttribute("prescription", prescription);
		return "prescription_show";
	}
}
