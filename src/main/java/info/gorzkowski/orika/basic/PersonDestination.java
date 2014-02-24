package info.gorzkowski.orika.basic;

import java.util.Date;


public class PersonDestination {
    private String fullName;
    private String givenName;
    private String sirName;

    private int currentAge;
    private Date birthDate;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSirName() {
        return sirName;
    }

    public void setSirName(String sirName) {
        this.sirName = sirName;
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

    public static class PersonDestinationBuilder
    {
        private PersonDestination personDestination;

        private PersonDestinationBuilder()
        {
            personDestination = new PersonDestination();
        }

        public PersonDestinationBuilder withFullName(String fullName)
        {
            personDestination.fullName = fullName;
            return this;
        }

        public PersonDestinationBuilder withGivenName(String givenName)
        {
            personDestination.givenName = givenName;
            return this;
        }

        public PersonDestinationBuilder withSirName(String sirName)
        {
            personDestination.sirName = sirName;
            return this;
        }

        public PersonDestinationBuilder withCurrentAge(int currentAge)
        {
            personDestination.currentAge = currentAge;
            return this;
        }

        public PersonDestinationBuilder withBirthDate(Date birthDate)
        {
            personDestination.birthDate = birthDate;
            return this;
        }

        public static PersonDestinationBuilder personDestination()
        {
            return new PersonDestinationBuilder();
        }

        public PersonDestination build()
        {
            return personDestination;
        }
    }
}
