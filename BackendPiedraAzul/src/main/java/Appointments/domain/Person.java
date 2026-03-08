package Appointments.domain;

import java.util.Date;

public class Person {
    protected Long id;
    protected DoctorTypeEnum documentType;
    protected String identificationNumber;
    protected String firstName;
    protected String lastName;
    protected Date birthDate;
    protected String phone;
    protected boolean active;
    protected User user;

    public Person() {
    }

    public Person(Long id, DoctorTypeEnum documentType, String identificationNumber, String firstName, String lastName, Date birthDate, String phone, boolean active, User user) {
        this.id = id;
        this.documentType = documentType;
        this.identificationNumber = identificationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.active = active;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DoctorTypeEnum getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DoctorTypeEnum documentType) {
        this.documentType = documentType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
