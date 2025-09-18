package application;

import application.model.Doctor;
import application.model.DoctorRepository;
import application.model.Patient;
import application.model.PatientRepository;
import application.service.SequenceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import view.*;

/*
 * Controller class for patient interactions.
 *   register as a new patient.
 *   update patient profile.
 */
@Controller
public class ControllerPatientCreate {

  @Autowired
  PatientRepository patientRepository;

  @Autowired
  DoctorRepository doctorRepository;

  @Autowired
  SequenceService sequence;

  /*
   * Request blank patient registration form.
   */
  @GetMapping("/patient/new")
  public String getNewPatientForm(Model model) {
    // return blank form for new patient registration
    model.addAttribute("patient", new PatientView());
    return "patient_register";
  }

  /*
   * Process data from the patient_register form
   */
  @PostMapping("/patient/new")
  public String createPatient(PatientView patient, Model model) {
    //  check for valid doctor name
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
      return "patient_register";
    }

    //  get the next unique id for patient
    int id = sequence.getNextSequence("PATIENT_SEQUENCE");
    //  create a model.patient instance
    //  copy data from PatientView to model
    Patient patientModel = Patient.fromView(patient);

    patientModel.setId(id);

    patientRepository.insert(patientModel);

//      // display patient data and the generated patient ID,  and success message
//      model.addAttribute("message", "Registration successful.");
//      model.addAttribute("patient", patient);
    model.addAttribute("message", "Registration successful.");
    patient.setId(id);
    model.addAttribute("patient", patient);
    return "patient_show";
  }

  /*
   * Request blank form to search for patient by id and name
   */
  @GetMapping("/patient/edit")
  public String getSearchForm(Model model) {
    model.addAttribute("patient", new PatientView());
    return "patient_get";
  }

  /*
   * Perform search for patient by patient id and name.
   */
  @PostMapping("/patient/show")
  public String showPatient(PatientView patient, Model model) {
    Patient patientModel = patientRepository.findByIdAndLastName
                                                   (patient.getId(),
                                                    patient.getLastName());
    if (patientModel == null){
      model.addAttribute("message", "Patient not found");
      model.addAttribute("patient", patient);
      return "patient_get";
    }

    patient.setFirstName(patientModel.getFirstName());
    patient.setBirthdate(patientModel.getBirthdate());
    patient.setSsn(patientModel.getSsn());
    patient.setStreet(patientModel.getStreet());
    patient.setCity(patientModel.getCity());
    patient.setState(patientModel.getState());
    patient.setZipcode(patientModel.getZipcode());
    patient.setPrimaryName(patientModel.getPrimaryName());

    model.addAttribute("patient", patient);
    return "patient_show";

  }

}
