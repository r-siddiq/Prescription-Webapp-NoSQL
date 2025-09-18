package application;

import application.model.Doctor;
import application.model.DoctorRepository;
import application.model.Patient;
import application.model.PatientRepository;
import application.service.SequenceService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import view.PatientView;

/*
 * Controller class for patient interactions.
 *   register as a new patient.
 *   update patient profile.
 */
@Controller
public class ControllerPatientUpdate {
	
	@Autowired
	PatientRepository patientRepository;

	@Autowired
	DoctorRepository doctorRepository;

	@Autowired
	SequenceService sequence;
	
	/*
	 *  Display patient profile for patient id.
	 */
	@GetMapping("/patient/edit/{id}")
	public String getUpdateForm(@PathVariable int id, Model model) {
		Patient patientModel = patientRepository.findById(id);

		if (patientModel == null) {
			model.addAttribute("message", "Patient not found");
			return "patient_get";
		}

		PatientView patientView = new PatientView();
		patientView.setId(id);
		patientView.setFirstName(patientModel.getFirstName());
		patientView.setLastName(patientModel.getLastName());
		patientView.setBirthdate(patientModel.getBirthdate());
		patientView.setSsn(patientModel.getSsn());
		patientView.setStreet(patientModel.getStreet());
		patientView.setCity(patientModel.getCity());
		patientView.setState(patientModel.getState());
		patientView.setZipcode(patientModel.getZipcode());
		patientView.setPrimaryName(patientModel.getPrimaryName());

		model.addAttribute("patient", patientView);
		return "patient_edit";
}
	
	
	/*
	 * Process changes from patient_edit form
	 *  Primary doctor, street, city, state, zip can be changed
	 *  ssn, patient id, name, birthdate, ssn are read only in template.
	 */
	@PostMapping("/patient/edit")
	public String updatePatient(PatientView patient, Model model) {
		// validate doctor last name
		String doctorName = patient.getPrimaryName();
		String doctorLastName;
		if (doctorName.contains(" ")){
			String[] doctorNames = doctorName.split(" ");
			doctorLastName = doctorNames[1];
		} else {
			doctorLastName = doctorName;
		}

		Doctor primaryDoctor = doctorRepository.findByLastName(doctorLastName);

		if (primaryDoctor == null){
			model.addAttribute("message", "Doctor not found");
			model.addAttribute("patient", patient);
			return "patient_edit";
		}

		//	doctor validated

		Patient p = patientRepository.findById(patient.getId());

		p.setStreet(patient.getStreet());
		p.setCity(patient.getCity());
		p.setState(patient.getState());
		p.setZipcode(patient.getZipcode());
		p.setPrimaryName(patient.getPrimaryName());

		patientRepository.save(p);

		model.addAttribute("message", "Update successful");
		model.addAttribute("patient", patient);
		return "patient_show";
	}
}
