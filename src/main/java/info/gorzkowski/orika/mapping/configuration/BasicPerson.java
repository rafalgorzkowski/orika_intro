package info.gorzkowski.orika.mapping.configuration;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BasicPerson {
    private String name;
    private int age;
    private Date birthDate;
    private List<String> nameParts;
    private Map<String, String> namePartsMap;
    private NestedElement nestedElement;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getNameParts() {
        return nameParts;
    }

    public void setNameParts(List<String> nameParts) {
        this.nameParts = nameParts;
    }

    public Map<String, String> getNamePartsMap() {
        return namePartsMap;
    }

    public void setNamePartsMap(Map<String, String> namePartsMap) {
        this.namePartsMap = namePartsMap;
    }

    public NestedElement getNestedElement() {
        return nestedElement;
    }

    public void setNestedElement(NestedElement nestedElement) {
        this.nestedElement = nestedElement;
    }
}
