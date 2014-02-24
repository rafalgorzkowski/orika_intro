package info.gorzkowski.orika.basic;

import java.util.Date;


public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private Sex sex;
    private Date birthDate;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public static class PersonBuilder
    {
        private Person person;

        private PersonBuilder()
        {
            person = new Person();
        }

        public PersonBuilder withFirstName(String firstName)
        {
            person.firstName = firstName;
            return this;
        }

        public PersonBuilder withLastName(String lastName)
        {
            person.lastName = lastName;
            return this;
        }

        public PersonBuilder withAge(int age)
        {
            person.age = age;
            return this;
        }

        public PersonBuilder withSex(Sex sex)
        {
            person.sex = sex;
            return this;
        }

        public PersonBuilder withBirthDate(Date birthDate)
        {
            person.birthDate = birthDate;
            return this;
        }

        public static PersonBuilder person()
        {
            return new PersonBuilder();
        }

        public Person build()
        {
            return person;
        }
    }
}
