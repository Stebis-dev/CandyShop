package com.CandyShop.fxControllers.tableViewGuidelines;

import com.CandyShop.model.Manager;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerTableGuidelines {
    private SimpleStringProperty employeeId;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty birthdate;
    private SimpleStringProperty medicalCertificate;
    private SimpleStringProperty employmentDate;

    public ManagerTableGuidelines(Manager manager) {
        this.employeeId = new SimpleStringProperty(manager.getEmployeeId() != null ? manager.getEmployeeId() : "");
        this.name = new SimpleStringProperty(manager.getName() != null ? manager.getName() : "");
        this.surname = new SimpleStringProperty(manager.getSurname() != null ? manager.getSurname() : "");
        this.birthdate = new SimpleStringProperty(manager.getBirthDate() != null ? manager.getBirthDate().toString() : "");
        this.medicalCertificate = new SimpleStringProperty(manager.getMedCertificate() != null ? manager.getMedCertificate() : "");
        this.employmentDate = new SimpleStringProperty(manager.getEmploymentDate() != null ? manager.getEmploymentDate().toString() : "");

    }

    public String getEmployeeId() {
        return employeeId == null ? "" : employeeId.get();
    }

    public String getName() {
        return name == null ? "" : name.get();
    }

    public String getSurname() {
        return surname == null ? "" : surname.get();
    }

    public String getBirthdate() {
        return birthdate == null ? "" : birthdate.get();
    }

    public String getMedicalCertificate() {
        return medicalCertificate == null ? "" : medicalCertificate.get();
    }

    public String getEmploymentDate() {
        return employmentDate == null ? "" : employmentDate.get();
    }
}
