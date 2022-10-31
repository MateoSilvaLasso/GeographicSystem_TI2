package model;

import exception.IncorrectFormatException;

public class City implements Comparable<City> {
    private String id;
    private String name;
    private String countryId;
    private double population;

    public City(String id, String name, String countryId, double population) throws IncorrectFormatException{
        if(id.charAt(0)!= 39 && id.charAt(id.length()-1)!=39)
            throw new IncorrectFormatException(id);
        else
            this.id = id;

        if(name.charAt(0)!=39 && name.charAt(name.length()-1)!=39)
            throw new IncorrectFormatException(name);
        else
            this.name = name;
        if(countryId.charAt(0)!=39 && countryId.charAt(countryId.length()-1)!=39)
            throw new IncorrectFormatException(countryId);
        else
            this.countryId = countryId;

        this.population = population;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public double getPopulation() {
        return population;
    }

    public void setPopulation(double population) {
        this.population = population;
    }

    @Override
    public int compareTo(City o){
        return 0;
    }
}
