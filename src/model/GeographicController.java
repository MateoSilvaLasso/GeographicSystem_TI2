package model;

import java.util.ArrayList;

import Exception.*;

public class GeographicController {
    ArrayList<Country> countries;
    ArrayList<String> serializable;

    public GeographicController() {
        this.countries= new ArrayList<>();
        this.serializable= new ArrayList<String>();
    }

    public void addCountry(String id, String name, double population, String countryCode) throws IncorrectFormatExcepcion{
        Country e= new Country(id,name,population,countryCode);
        this.countries.add(e);
    }

    public void addserializableCountry(String s){
        this.serializable.add(s);
    }

    public void comprobateInsertComand(String s) throws InexistentComandException{
        String a="";
        for(int i=0; i<18; i++){
            a+=s.charAt(i);
        }
        if(!a.equals("INSERT INTO cities") && !a.equals("INSERT INTO countr")){
            throw new InexistentComandException(s);
        }else if(a.equals("INSERT INTO countr")){
            /*
            String b="";
            for(int i=19;i<21;i++){
                b+=s.charAt(i);
            }
            if(!b.equals("ies")){
                throw new InexistentComandException(s);
            }
            */

        }

        String[] format;

        if(a.equals("INSERT INTO cities")){
            format=s.split("cities");
            String w= format[1];
            w= w.replace("(","");
            w=w.replace(") ","");
            String[] x=w.split(",");
            if(!x[0].equals("id") || !x[1].equals("name") || !x[2].equals("countryID") || !x[3].equals("population")){
                throw new InexistentComandException(s);
            }
        }else{
            format=s.split("countries");
            String w= format[1];
            w= w.replace("(","");
            w=w.replace(")","");

            String[] x=w.split(",");

            for(int i=0; i< x.length; i++) {
                String c= x[i];
                if (c.equals("id") || c.equals("name")  || c.equals("population")  || c.equals("countryCode ")){

                }else{

                    throw new InexistentComandException(s);
                }

            }
            /*
            if(!x[0].equals(" id") || !x[1].equals(" name") || !x[2].equals(" population") || !x[3].equals(" countryCode")){
                throw new InexistentComandException(s);
            }
            */
        }

    }

    public void comprobateIdCountry(String s) throws NoCountryIDException{

        boolean flag=false;

        for(int i=0; i<countries.size() && !flag; i++){
            if(countries.get(i).getId().equals(s)){
                flag=true;
            }
        }

        if(!flag){
            throw new NoCountryIDException(s);
        }

    }

    public void comprobateSelectComand(String s)throws InexistentComandException{
        String []arr= s.split("=");
        if(!arr[0].equals("SELECT * FROM cities WHERE name")){
            throw new InexistentComandException(s);
        }
    }

    public void addCity(String id, String name, String CountryCode, double population) throws IncorrectFormatExcepcion{

        City city= new City(id,name,CountryCode,population);

        for(int i=0; i<countries.size(); i++){
            if(countries.get(i).getId().equals(CountryCode)){
                countries.get(i).addCity(city);
            }
        }



    }

    public ArrayList<String> getCountries(){
        return this.serializable;
    }


}
