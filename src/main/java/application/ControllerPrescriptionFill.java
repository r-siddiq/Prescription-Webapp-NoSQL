package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import application.model.*;
import application.service.*;
import view.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class ControllerPrescriptionFill {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SequenceService sequenceService;

    /*
     * Patient requests form to fill prescription.
     */
    @GetMapping("/prescription/fill")
    public String getFillForm(Model model) {
        model.addAttribute("prescription", new PrescriptionView());
        return "prescription_fill";
    }

    // Process data from prescription_fill form
    @PostMapping("/prescription/fill")
    public String processFillForm(PrescriptionView prescriptionView, Model model) {

        Prescription prescriptionM = prescriptionRepository.findById(prescriptionView.getRxid());
        if (prescriptionM == null) {
            model.addAttribute("message", "Prescription not found");
            model.addAttribute("prescription", prescriptionView);
            return "prescription_fill";
        }

        // Checking if the prescription has no refills
        if (prescriptionM.getFills().size() == prescriptionM.getRefills() + 1){
            model.addAttribute("message", "No more refills available");
            model.addAttribute("prescription", prescriptionView);
            return "prescription_fill";
        }

        // Need to check if the prescription is on the first fill.
        if(!prescriptionM.getFills().isEmpty()){
            // Grab Prescription object from DB and update (decrement by 1)
            int refillsRemaining = prescriptionM.getRefills() - prescriptionM.getFills().size() + 1;
            prescriptionView.setRefillsRemaining(refillsRemaining - 1);
            prescriptionView.setRefills(prescriptionView.getRefillsRemaining());
        } else{
            prescriptionView.setRefills(prescriptionM.getRefills()); //
        }

        // Add refill logs to the FillRequest object
        Prescription.FillRequest fillRequest = new Prescription.FillRequest();
        fillRequest.setDateFilled(LocalDate.now().toString());
        Pharmacy pharmacy = pharmacyRepository.findByNameAndAddress(prescriptionView.getPharmacyName(), prescriptionView.getPharmacyAddress());
        // Validating the Pharmacy
        if(pharmacy == null){
            model.addAttribute("message", "Pharmacy not found");
            model.addAttribute("prescription", prescriptionView);
            return "prescription_fill";
        }
        fillRequest.setPharmacyID(pharmacy.getId());
        double cost = -1.0;
        for(Pharmacy.DrugCost drugCost : pharmacy.getDrugCosts()){
            if(drugCost.getDrugName().equals(prescriptionM.getDrugName())){
                cost = drugCost.getCost();
            }
        }
        // Validating the Drug Cost
        if(cost < 0){
            model.addAttribute("message", "Drug cost not found");
            model.addAttribute("prescription", prescriptionView);
            return "prescription_fill";
        }
        DecimalFormat twoDecimalPlaces = new DecimalFormat("0.00");
        double totalCost = cost * prescriptionM.getQuantity();
        fillRequest.setCost(twoDecimalPlaces.format(totalCost));
        prescriptionM.getFills().add(fillRequest);

        StringBuilder phoneNumberWithHyphenAndParentheses = new StringBuilder();
        for (int i = 0; i < pharmacy.getPhone().length(); i++){
            if (i == 0) {
                phoneNumberWithHyphenAndParentheses.append("(");
            }
            if (i == 3){
                phoneNumberWithHyphenAndParentheses.append(") ");
            }
            if (i == 7) {
                phoneNumberWithHyphenAndParentheses.append("-");
            }
            if(Character.isDigit(pharmacy.getPhone().charAt(i))){
                phoneNumberWithHyphenAndParentheses.append(pharmacy.getPhone().charAt(i));
            }
        }
        // Setting the Pharmacy PHone Number for the PrescriptionView
        prescriptionView.setPharmacyPhone(phoneNumberWithHyphenAndParentheses.toString());

        // Search for patient by last name and validate
        Patient patient = patientRepository.findByLastName(prescriptionView.getPatientLastName());
        if (patient == null) {
            model.addAttribute("message", "Patient not found");
            model.addAttribute("prescription", prescriptionView);
            return "prescription_fill";
        }

        // Setting fields for the next page
        prescriptionView.setPatientId(patient.getId()); //
        prescriptionView.setPatientFirstName(patient.getFirstName()); //
        prescriptionView.setQuantity(prescriptionM.getQuantity()); //
        prescriptionView.setDrugName(prescriptionM.getDrugName()); //
        prescriptionView.setDoctorId(prescriptionM.getDoctorId()); //
        Doctor doctor = doctorRepository.findById(prescriptionM.getDoctorId());
        prescriptionView.setDoctorFirstName(doctor.getFirstName()); //
        prescriptionView.setDoctorLastName(doctor.getLastName()); //
        prescriptionView.setPharmacyID(pharmacy.getId()); //
        prescriptionView.setDateFilled(fillRequest.getDateFilled()); //
        prescriptionView.setCost(twoDecimalPlaces.format(totalCost)); //

        prescriptionRepository.save(prescriptionM);

        model.addAttribute("message", "Prescription filled.");
        model.addAttribute("prescription", prescriptionView);
        return "prescription_show";
    }
}