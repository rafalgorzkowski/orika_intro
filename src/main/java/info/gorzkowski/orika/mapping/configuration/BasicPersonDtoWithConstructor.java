package info.gorzkowski.orika.mapping.configuration;

import java.util.Date;

public class BasicPersonDtoWithConstructor {

    private String fullName;
    private int currentAge;
    private Date birthDate;


    public BasicPersonDtoWithConstructor(String fullName, int currentAge, Date birthDate) {
        this.fullName = fullName;
        this.currentAge = currentAge;
        this.birthDate = birthDate;
    }

    public BasicPersonDtoWithConstructor(Date birthDate) {
        System.out.println("BasicPersonDtoWithConstructor with birthDate has been invoked.");
        this.birthDate = birthDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getCurrentAge() {
        return currentAge;
    }

    public void setCurrentAge(int currentAge) {
        this.currentAge = currentAge;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
