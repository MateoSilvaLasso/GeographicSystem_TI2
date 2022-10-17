package model;

import java.util.ArrayList;

import Exception.IncorrectFormatExcepcion;

public class Country implements Comparable<Country> {


    private String id;
    private String name;
    private double population;
    private String countryCode;

    private ArrayList<City> cities;

    public Country(String id, String name, double population, String countryCode) throws IncorrectFormatExcepcion {
        if(id.charAt(0)!=39 && id.charAt(id.length()-1)!=39)
            throw new IncorrectFormatExcepcion(id);
        else
            this.id = id;
        if(name.charAt(0)!=39 && name.charAt(name.length()-1)!=39)
            throw new IncorrectFormatExcepcion(name);
        else
            this.name = name;
        this.population = population;
        if(countryCode.charAt(0)!=39 && countryCode.charAt(countryCode.length()-1)!=39 && countryCode.charAt(1)!=43)
            throw new IncorrectFormatExcepcion(countryCode);
        else
            this.countryCode = countryCode;
        this.cities= new ArrayList<>();
    }

    public boolean addCity(City city) throws IncorrectFormatExcepcion{
        return cities.add(city);
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

    public double getPopulation() {
        return population;
    }

    public void setPopulation(double population) {
        this.population = population;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    @Override
    public int compareTo(Country o) {
        return 0;
    }
}
