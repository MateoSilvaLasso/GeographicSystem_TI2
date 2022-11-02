package model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;

import exception.*;
import com.google.gson.Gson;

public class GeographicController {
    ArrayList<Country> countries;
    ArrayList<String> serializable;
    Gson gson = new Gson();

    public GeographicController() {
        this.countries = new ArrayList<>();
        this.serializable = new ArrayList<String>();
    }

    public void addCountry(String id, String name, double population, String countryCode) throws IdAlreadyUsedException {
        Country e = new Country(id, name, population, countryCode);
        for (Country c : countries) {
            if (c.getId().equals(e.getId())) {
                throw new IdAlreadyUsedException(id);
            }
        }
        this.countries.add(e);
    }

    public void addSerializableCountry(String s) {
        this.serializable.add(s);
        //autoSave();
    }

    public void comprobateInsertComand(String s) throws InexistentCommandException {
        String[] arr = s.split("VALUES");
        String n = arr[0];
        String a = "";
        for (int i = 0; i < 18; i++) {
            a += n.charAt(i);
        }
        if (!a.equals("INSERT INTO cities") && !a.equals("INSERT INTO countr")) {
            throw new InexistentCommandException(s);
        } else if (a.equals("INSERT INTO countries")) {

        }

        String[] format;

        if (a.equals("INSERT INTO cities")) {
            format = n.split("cities");
            String w = format[1];
            w = w.replace("(", "");
            w = w.replace(") ", "");
            String[] x = w.split(",");
            if (!x[0].equals("id") || !x[1].equals(" name") || !x[2].equals(" countryID") || !x[3].equals(" population")) {
                throw new InexistentCommandException(s);
            }
        } else {
            format = n.split("countries");
            String w = format[1];
            w = w.replace("(", "");
            w = w.replace(") ", "");

            String[] x = w.split(",");

            for (int i = 0; i < x.length; i++) {
                String c = x[i];
                if (c.equals("id") || c.equals(" name") || c.equals(" population") || c.equals(" countryCode")) {

                } else {

                    throw new InexistentCommandException(s);
                }
            }
        }
    }

    public void insertInto(int option, String command) {
        try {
            if (option == 1) {
                String[] a = command.split("VALUES");
                comprobateInsertComand(command);
                String w = a[1];
                w = w.replace(" (", "");
                w = w.replace(")", "");
                w = w.replace(" ", "");
                System.out.println(w);
                String[] comprobate = w.split(",");
                String id = comprobate[0];
                String name = comprobate[1];
                double population = Double.parseDouble(comprobate[2]);
                String code = comprobate[3];
                addCountry(id, name, population, code);
                addSerializableCountry(command);

            } else {

                String[] a = command.split("VALUES");
                comprobateInsertComand(a[0]);
                String w = a[1];
                w = w.replace(" (", "");
                w = w.replace(")", "");
                w = w.replace(" ", "");
                String[] comprobate = w.split(",");

                checkCountryId(comprobate[2]);
                String id = comprobate[0];
                String name = comprobate[1];
                String code = comprobate[2];
                double population = Double.parseDouble(comprobate[3]);
                addCity(id, name, code, population);
                addSerializableCountry(command);
            }
        } catch (InexistentCommandException ex) {
            ex.printStackTrace();
        } catch (IncorrectFormatException ex) {
            ex.printStackTrace();
        } catch (NoCountryIDException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    public void checkAttribute(String s, int option) throws NoAttributeException {
        if (option == 1) {
            if (!s.equalsIgnoreCase("id") && !s.equalsIgnoreCase("name") && !s.equalsIgnoreCase("population") && !s.equalsIgnoreCase("countryCode")) {
                throw new NoAttributeException(s);
            }
        } else {
            if (!s.equalsIgnoreCase("id") && !s.equalsIgnoreCase("name") && !s.equalsIgnoreCase("population") && !s.equalsIgnoreCase("countryId")) {
                throw new NoAttributeException(s);
            }
        }
    }

    public void checkCountryId(String s) throws NoCountryIDException {
        boolean flag = false;

        for (int i = 0; i < countries.size() && !flag; i++) {
            if (countries.get(i).getId().equals(s)) {
                flag = true;
            }
        }

        if (!flag) {
            throw new NoCountryIDException(s);
        }
    }

    public void checkSelectCommand(String s, int option) throws InexistentCommandException {
        String[] arr;
        if (option == 1) {
            arr = s.split("=");
            if (!arr[0].equals("SELECT * FROM cities WHERE name ") && !arr[0].equals("SELECT * FROM countries WHERE name ")
                    && !arr[0].equals("SELECT * FROM cities WHERE countryId ") && !arr[0].equals("SELECT * FROM countries WHERE countryId ")
                    && !arr[0].equals("SELECT * FROM cities WHERE id ") && !arr[0].equals("SELECT * FROM countries WHERE id ")
                    && !arr[0].equals("SELECT * FROM countries WHERE countryCode ") && !arr[0].equals("SELECT * FROM cities WHERE country ")) {
                throw new InexistentCommandException(s);
            }
        } else if (option == 2) {
            arr = s.split(">");
            if (!arr[0].equals("SELECT * FROM countries WHERE population ") && !arr[0].equals("SELECT * FROM cities WHERE population ")) {
                throw new InexistentCommandException(s);
            }
        } else if (option == 3) {
            arr = s.split("<");
            if (!arr[0].equals("SELECT * FROM countries WHERE population ") && !arr[0].equals("SELECT * FROM cities WHERE population ")) {
                throw new InexistentCommandException(s);
            }
        } else {
            arr = s.split("WHERE");
            if (!arr[0].equals("SELECT * FROM countries") && !arr[0].equals("SELECT * FROM cities")) {
                throw new InexistentCommandException(s);
            }
        }
    }

    public void selectFrom(int option, String command) {

        try {
            if (option == 1) {

                checkSelectCommand(command, option);
                String[] criteria = command.split("=");
                if (criteria[0].equals("SELECT * FROM countries WHERE name ")) {
                    String t = criteria[1];
                    t = t.replace(" ", "");
                    if (searchCountryBool(t)) {
                        System.out.println("The country exists");
                    } else {
                        System.out.println("The country doesn't exist");
                    }
                } else if(criteria[0].equals("SELECT * FROM cities WHERE name ")){
                    String t = criteria[1];
                    t = t.replace(" ", "");
                    if (searchCityBool(t)) {
                        System.out.println("The city exists");
                    } else {
                        System.out.println("The city doesn't exist");
                    }
                } else if(criteria[0].equals("SELECT * FROM countries WHERE countryId ")){

                    String t = criteria[1];
                    t = t.replace(" ", "");
                    Country country = searchCountryId(t);
                    if (country != null) {
                        System.out.println("The country with this id is " + country.getName());
                    } else {
                        System.out.println("The country doesn't exist");
                    }
                } else if(criteria[0].equals("SELECT * FROM cities WHERE countryId ")){

                    String t = criteria[1];
                    t = t.replace(" ", "");
                    Country country = searchCountryId(t);
                    if (country != null) {
                        ArrayList<City> cities = country.getCities();
                        for(City x : cities){
                            System.out.println(x.getName());
                        }
                    } else {
                        System.out.println("This country doesn't exist");
                    }
                } else if(criteria[0].equals("SELECT * FROM cities WHERE id ")){
                    String t = criteria[1];
                    t = t.replace(" ", "");
                    City city = searchCityId(t);
                    if(city != null){
                        System.out.println(city.getName());
                    } else {
                        System.out.println("The city doesn't exist");
                    }
                } else if(criteria[0].equals("SELECT * FROM countries WHERE id ")){

                    String t = criteria[1];
                    t = t.replace(" ", "");
                    Country country = searchCountryId(t);
                    if(country != null){
                        System.out.println(country.getName());
                    } else {
                        System.out.println("The country doesn't exist");
                    }
                } else {
                    String t = criteria[1];
                    t = t.replace(" ", "");
                    Country country = searchCountryCode(t);
                    if(country != null){
                        System.out.println(country.getName());
                    } else {
                        System.out.println("The country doesn't exist");
                    }
                }

            } else if (option == 2) {

                checkSelectCommand(command, option);
                String[] Population = command.split(">");
                if (Population[0].equals("SELECT * FROM countries WHERE population ")) {
                    String w = Population[1];
                    w = w.replace(" ", "");
                    double pop = Double.parseDouble(w);
                    ArrayList<String> arr = searchBigPopulation(pop);
                    if (arr.isEmpty()) {
                        System.out.println("There isn't a country with a population greater than " + pop + " millions");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                } else {
                    String w = Population[1];
                    w = w.replace(" ", "");
                    double pop = Double.parseDouble(w);
                    ArrayList<String> arr = searchBigCityPopulation(pop);
                    if (arr.isEmpty()) {
                        System.out.println("There isn't a city with a population greater than " + pop + " millions");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                }
            } else if (option == 3) {

                checkSelectCommand(command, option);
                String[] Population = command.split("<");
                if (Population[0].equals("SELECT * FROM countries WHERE population ")) {
                    String w = Population[1];
                    w = w.replace(" ", "");
                    double minPop = Double.parseDouble(w);
                    ArrayList<String> arr = searchMinPopulation(minPop);
                    if (arr.isEmpty()) {
                        System.out.println("There isn't a country with a population lesser than " + minPop + " millions");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                } else {
                    String w = Population[1];
                    w = w.replace(" ", "");
                    double minPop = Double.parseDouble(w);
                    ArrayList<String> arr = searchMinCityPopulation(minPop);
                    if (arr.isEmpty()) {
                        System.out.println("There isn't a city with a population lesser than " + minPop + " millions");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                }
            } else if (option == 4){

                checkSelectCommand(command, option);
                String[] Population = command.split("WHERE");
                if (Population[0].equals("SELECT * FROM countries")) {
                    ArrayList<String> arr = getCountries();
                    if (arr.isEmpty()) {
                        System.out.println("There are no countries");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                } else {
                    ArrayList<String> arr = getCities();
                    if (arr.isEmpty()) {
                        System.out.println("There are no cities");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                }
            }
        } catch (InexistentCommandException ex) {
            ex.printStackTrace();
        }
    }

    public void checkOrderByCommand(String s, int option) throws InexistentCommandException {
        String[] arr;
        if (option == 1) {
            arr = s.split(" ");
            if ((!arr[0].equals("SELECT") || !arr[1].equals("*") || !arr[2].equals("FROM") || !arr[3].equals("countries") || !arr[4].equals("WHERE") || !arr[5].equals("population") || (!arr[6].equals(">") && !arr[6].equals("<")) || !arr[8].equals("ORDER") || !arr[9].equals("BY")) && (!arr[0].equals("SELECT") || !arr[1].equals("*") || !arr[2].equals("FROM") || !arr[3].equals("cities") || !arr[4].equals("WHERE") || !arr[5].equals("population") || (!arr[6].equals(">") && !arr[6].equals("<")) || !arr[8].equals("ORDER") || !arr[9].equals("BY"))) {
                throw new InexistentCommandException(s);
            }

        } else {
            arr = s.split(" ");
            if ((!arr[0].equals("SELECT") || !arr[1].equals("*") || !arr[2].equals("FROM") || !arr[3].equals("cities") || !arr[4].equals("WHERE") || !arr[5].equals("name") || !arr[6].equals("=") || !arr[8].equals("ORDER") || !arr[9].equals("BY")) && (!arr[0].equals("SELECT") || !arr[1].equals("*") || !arr[2].equals("FROM") || !arr[3].equals("countries") || !arr[4].equals("WHERE") || !arr[5].equals("name") || !arr[6].equals("=") || !arr[8].equals("ORDER") || !arr[9].equals("BY"))) {
                throw new InexistentCommandException(s);
            }
        }
    }

    public void checkDelete(int option, String command) throws InexistentCommandException {
        if (option == 1) {
            String[] comprobate = command.split(" ");
            if (!comprobate[0].equals("DELETE") || !comprobate[1].equals("FROM") || (!comprobate[2].equals("countries") && !comprobate[2].equals("cities")) || !comprobate[3].equals("WHERE") || !comprobate[5].equals("=")) {
                throw new InexistentCommandException(command);
            }
        } else {
            String[] comprobate = command.split(" ");
            if (!comprobate[0].equals("DELETE") || !comprobate[1].equals("FROM") || (!comprobate[2].equals("countries") && !comprobate[2].equals("cities")) || !comprobate[3].equals("WHERE") || !comprobate[4].equalsIgnoreCase("population") || (!comprobate[5].equals(">") && !comprobate[5].equals("<"))) {
                throw new InexistentCommandException(command);
            }
        }
    }

    public void addCity(String id, String name, String countryCode, double population) throws IncorrectFormatException {

        City city = new City(id, name, countryCode, population);
        Country c = searchCountryId(countryCode);
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getId().equals(countryCode)) {
                for (City x : c.getCities()) {
                    if (c.getId().equals(x.getId())) {
                        throw new IdAlreadyUsedException(id);
                    }
                }
                countries.get(i).addCity(city);
            }
        }


    }

    public boolean searchCountryBool(String s) {
        boolean flag = false;

        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getName().equals(s)) {
                flag = true;
            }
        }

        return flag;
    }

    public Country searchCountryId(String s) {

        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getId().equals(s)) {
                return countries.get(i);
            }
        }
        return null;
    }

    public Country searchCountryCode(String s) {

        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCountryCode().equals(s)) {
                return countries.get(i);
            }
        }
        return null;
    }

    public boolean searchCityBool(String s) {

        boolean flag = false;
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                if (countries.get(i).getCities().get(j).getName().equals(s)) {
                    flag = true;
                }
            }
        }

        return flag;
    }

    public City searchCityId(String s) {

        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                if (countries.get(i).getCities().get(j).getId().equals(s)) {
                    return countries.get(i).getCities().get(j);
                }
            }
        }

        return null;
    }

    public ArrayList<String> searchBigPopulation(double p) {
        ArrayList<String> pop = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getPopulation() > p) {
                pop.add(countries.get(i).getName());
            }
        }


        return pop;
    }

    public ArrayList<String> searchBigCityPopulation(double p) {
        ArrayList<String> pop = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                if (countries.get(i).getCities().get(j).getPopulation() > p) {
                    pop.add(countries.get(i).getCities().get(j).getName());
                }
            }
        }

        return pop;
    }

    public ArrayList<String> searchMinPopulation(double p) {
        ArrayList<String> pop = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getPopulation() <= p)
                pop.add(countries.get(i).getName());
        }

        return pop;
    }

    public ArrayList<String> searchMinCityPopulation(double p) {
        ArrayList<String> pop = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                if (countries.get(i).getCities().get(j).getPopulation() < p) {
                    pop.add(countries.get(i).getCities().get(j).getName());
                }
            }
        }

        return pop;
    }

    public void orderBy(int option, String command) {

        try {
            if (option == 1) {
                checkOrderByCommand(command, option);
                String[] attribute = command.split("BY ");
                if(command.contains("countries")){
                    checkAttribute(attribute[1], 1);
                }else{
                    checkAttribute(attribute[1], 2);
                }

                String[] criteria = command.split(" ");
                double cr = Double.parseDouble(criteria[7]);
                ArrayList<String> arr = orderSelection(option, command, cr, "", attribute[1]);
                for (int i = 0; i < arr.size(); i++) {
                    System.out.println(arr.get(i));
                }

            } else {


                checkOrderByCommand(command, option);
                String[] attribute = command.split("BY ");
                if(command.contains("countries")){
                    checkAttribute(attribute[1], 1);
                }else{
                    checkAttribute(attribute[1], 2);
                }

                String[] criteria = command.split(" ");
                String name = criteria[7];
                ArrayList<String> arr = orderSelection(option, command, 0, name, attribute[1]);
                for (int i = 0; i < arr.size(); i++) {
                    System.out.println(arr.get(i));
                }

            }
        } catch (InexistentCommandException ex) {
            ex.printStackTrace();
        } catch (NoAttributeException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            ex.getMessage();
        }
    }

    public ArrayList<String> orderSelection(int option, String command, double number, String name, String attribute) {
        ArrayList<String> all = new ArrayList<>();
        if (option == 1) {
            ArrayList<Country> arr = new ArrayList<>();
            ArrayList<City> ar = new ArrayList<>();
            String[] check = command.split(" ");
            if (check[3].equals("countries")) {
                if (check[6].equals("<")) {
                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getPopulation() < number) {
                            arr.add(countries.get(i));
                        }
                    }
                } else if (check[6].equals(">")) {
                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getPopulation() > number) {
                            arr.add(countries.get(i));
                        }
                    }
                }

                if (attribute.equals("id")) {
                    Collections.sort(arr, (a, b) -> {
                        return a.getId().compareTo(b.getId());
                    });
                } else if (attribute.equals("name")) {
                    Collections.sort(arr, (a, b) -> {
                        return a.getName().compareTo(b.getName());
                    });
                } else if (attribute.equals("population")) {
                    Collections.sort(arr, (a, b) -> {
                        if (a.getPopulation() < b.getPopulation()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    });

                } else {
                    Collections.sort(arr, (a, b) -> {
                        return a.getCountryCode().compareTo(b.getCountryCode());
                    });
                }

                for (int i = 0; i < arr.size(); i++) {
                    String target = arr.get(i).getId() + ", " + arr.get(i).getName() + ", " + arr.get(i).getPopulation() + ", " + arr.get(i).getCountryCode();
                    all.add(target);
                }
            } else {
                if (check[6].equals("<")) {
                    for (int i = 0; i < countries.size(); i++) {
                        for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                            if (countries.get(i).getCities().get(j).getPopulation() < number) {
                                ar.add(countries.get(i).getCities().get(j));
                            }
                        }
                    }
                } else if (check[6].equals(">")) {
                    for (int i = 0; i < countries.size(); i++) {
                        for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                            if (countries.get(i).getCities().get(j).getPopulation() > number) {
                                ar.add(countries.get(i).getCities().get(j));
                            }
                        }


                    }
                }

                if (attribute.equals("id")) {
                    Collections.sort(ar, (a, b) -> {
                        return a.getId().compareTo(b.getId());
                    });
                } else if (attribute.equals("name")) {
                    Collections.sort(ar, (a, b) -> {
                        return a.getName().compareTo(b.getName());
                    });
                } else if (attribute.equals("population")) {
                    Collections.sort(ar, (a, b) -> {
                        if (a.getPopulation() < b.getPopulation()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    });

                } else {
                    Collections.sort(ar, (a, b) -> {
                        return a.getCountryId().compareTo(b.getCountryId());
                    });
                }

                for (int i = 0; i < ar.size(); i++) {
                    String target = ar.get(i).getId() + ", " + ar.get(i).getName() + ", " + ar.get(i).getPopulation() + ", " + ar.get(i).getCountryId();
                    all.add(target);
                }


            }

        } else {
            ArrayList<City> arr = new ArrayList<>();
            ArrayList<Country> ar = new ArrayList<>();
            String[] check = command.split(" ");
            if (check[3].equals("cities")) {
                for (int i = 0; i < countries.size(); i++) {
                    for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                        if (countries.get(i).getCities().get(j).getName().equals(name)) {
                            arr.add(countries.get(i).getCities().get(j));
                        }
                    }
                }

                if (attribute.equals("id")) {
                    Collections.sort(arr, (a, b) -> {
                        return a.getId().compareTo(b.getId());
                    });
                } else if (attribute.equals("name")) {
                    Collections.sort(arr, (a, b) -> {
                        return a.getName().compareTo(b.getName());
                    });
                } else if (attribute.equals("countryId")) {
                    Collections.sort(arr, (a, b) -> {
                        return a.getCountryId().compareTo(b.getCountryId());
                    });
                } else {
                    Collections.sort(arr, (a, b) -> {
                        if (a.getPopulation() < b.getPopulation()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    });
                }

                for (int i = 0; i < arr.size(); i++) {
                    String target = arr.get(i).getId() + ", " + arr.get(i).getName() + ", " + arr.get(i).getCountryId() + ", " + arr.get(i).getPopulation();
                    all.add(target);
                }
            } else {
                for (int i = 0; i < countries.size(); i++) {
                    if (countries.get(i).getName().equals(name)) {
                        ar.add(countries.get(i));
                    }
                }

                if (attribute.equals("id")) {
                    Collections.sort(ar, (a, b) -> {
                        return a.getId().compareTo(b.getId());
                    });
                } else if (attribute.equals("name")) {
                    Collections.sort(ar, (a, b) -> {
                        return a.getName().compareTo(b.getName());
                    });
                } else if (attribute.equals("countryId")) {
                    Collections.sort(ar, (a, b) -> {
                        return a.getCountryCode().compareTo(b.getCountryCode());
                    });
                } else {
                    Collections.sort(ar, (a, b) -> {
                        if (a.getPopulation() < b.getPopulation()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    });
                }

                for (int i = 0; i < ar.size(); i++) {
                    String target = ar.get(i).getId() + ", " + ar.get(i).getName() + ", " + ar.get(i).getCountryCode() + ", " + ar.get(i).getPopulation();
                    all.add(target);
                }
            }
        }
        return all;
    }

    public void delete(int option, String command) {
        try {
            if (option == 1) {
                checkDelete(option, command);
                String[] check = command.split(" ");
                if (check[4].equalsIgnoreCase("population")) {
                    String pop = String.valueOf(check[6]);
                    deletePlace(option, check[2], check[4], pop, 0, "");
                } else {
                    deletePlace(option, check[2], check[4], check[6], 0, "");
                }
            } else {
                checkDelete(option, command);
                String[] check = command.split(" ");
                double pop = Double.parseDouble(check[6]);
                deletePlace(option, check[2], "", "", pop, check[5]);
            }
        } catch (InexistentCommandException ex) {
            ex.printStackTrace();
        } catch (NoAttributeException ex) {
            ex.printStackTrace();
        }
    }

    public void deletePlace(int option, String place, String attribute, String value, double population, String m) throws NoAttributeException {
        if (option == 1) {

            if (place.contains("cities")) {
                if ((!attribute.equalsIgnoreCase("name") && !attribute.equalsIgnoreCase("id") && !attribute.equalsIgnoreCase("CountryId") && !attribute.equalsIgnoreCase("population"))) {
                    throw new NoAttributeException(attribute);
                }
                if (attribute.equalsIgnoreCase("name")) {
                    for (int i = 0; i < countries.size(); i++) {
                        for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                            if (countries.get(i).getCities().get(j).getName().equals(value)) {
                                countries.get(i).getCities().remove(j);
                                j--;
                            }
                        }
                    }
                }
                if (attribute.equalsIgnoreCase("id")) {
                    for (int i = 0; i < countries.size(); i++) {
                        for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                            if (countries.get(i).getCities().get(j).getId().equals(value)) {
                                countries.get(i).getCities().remove(j);
                                j--;
                            }
                        }
                    }
                }
                if (attribute.equalsIgnoreCase("countryId")) {
                    for (int i = 0; i < countries.size(); i++) {
                        for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                            if (countries.get(i).getCities().get(j).getCountryId().equals(attribute)) {
                                countries.get(i).getCities().remove(j);
                                j--;
                            }
                        }
                    }
                }
                if (attribute.equalsIgnoreCase("population")) {
                    double p = Double.parseDouble(value);
                    for (int i = 0; i < countries.size(); i++) {
                        for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                            if (countries.get(i).getCities().get(j).getPopulation() == p) {
                                countries.get(i).getCities().remove(j);
                                j--;
                            }
                        }
                    }
                }
            } else {
                if (!attribute.equalsIgnoreCase("name") && !attribute.equalsIgnoreCase("id") && !attribute.equalsIgnoreCase("population") && !attribute.equalsIgnoreCase("CountryCode")) {
                    throw new NoAttributeException(attribute);
                }
                if (attribute.equalsIgnoreCase("name")) {
                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getName().equals(value)) {
                            countries.remove(i);
                            i--;
                        }
                    }
                }
                if (attribute.equalsIgnoreCase("id")) {
                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getId().equals(value)) {
                            countries.remove(i);
                            i--;
                        }
                    }
                }
                if (attribute.equalsIgnoreCase("population")) {

                    double p = Double.parseDouble(value);
                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getPopulation() == p) {
                            countries.remove(i);
                            i--;
                        }
                    }
                }
                if (attribute.equalsIgnoreCase("CountryCode")) {


                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getCountryCode().equals(value)) {
                            countries.remove(i);
                            i--;
                        }
                    }
                }
            }

        } else {
            if (place.contains("cities")) {
                if (m.equals("<")) {

                    for (int i = 0; i < countries.size(); i++) {
                        for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                            if (countries.get(i).getCities().get(j).getPopulation() < population) {
                                countries.get(i).getCities().remove(j);
                                j--;
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < countries.size(); i++) {
                        for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                            if (countries.get(i).getCities().get(j).getPopulation() > population) {
                                countries.get(i).getCities().remove(j);
                                j--;
                            }
                        }
                    }
                }

            } else {
                if (m.equals("<")) {
                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getPopulation() < population) {
                            countries.remove(i);
                            i--;
                        }
                    }
                } else {
                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getPopulation() > population) {
                            countries.remove(i);
                            i--;
                        }
                    }
                }
            }
        }
    }


    public ArrayList<String> getCountries() {
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            arr.add(countries.get(i).getName()+" "+countries.get(i).getId()+" "+countries.get(i).getCountryCode()+" "+countries.get(i).getPopulation());
        }
        return arr;
    }

    public ArrayList<String> getCities() {
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                arr.add(countries.get(i).getCities().get(j).getName()+" "+countries.get(i).getCities().get(j).getId()+" "+countries.get(i).getCities().get(j).getCountryId()+" "+countries.get(i).getCities().get(j).getPopulation());
            }
        }

        return arr;
    }

    public ArrayList<String> getCitiesCountry() {
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getCities().size(); j++) {
                arr.add(countries.get(i).getCities().get(j).getName() + " " + countries.get(i).getCities().get(j).getCountryId());
            }
        }

        return arr;
    }

    //------------------------------------------------------------------------------------------------------------------

    //METODO PARA IMPORTAR UN ARCHIVO QUE CONTENGA COMANDOS Y LOS EJECUTE LINEA POR LINEA
    public void importSQL(String fileName, int option) {

        //ASIGNAMOS EL ARCHIVO A UNA VARIABLE
        File file = new File(fileName + ".txt");

        //COMPROBAMOS QUE EL ARCHIVO EXISTA
        if (file.exists()) {

            try {

                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(fis)
                );

                String line;
                String json="";
                while ((line = reader.readLine()) != null) {
                    json+=line;
                }

                fis.close();

                Gson gson1=new Gson();

                String[] arr= gson1.fromJson(json,String[].class);

                for(int i=0; i<arr.length; i++){
                    comprobateInsertComand(arr[i]);

                    String[] separable = arr[i].split("\\(");
                    if (separable[0].equals("INSERT INTO countries")) {
                        String[] comand = arr[i].split("VALUES");
                        String values = comand[1];
                        values = values.replace(" (", "");
                        values = values.replace(")", "");
                        values = values.replace(" ", "");
                        String[] atributes = values.split(",");
                        String id = atributes[0];
                        String name = atributes[1];
                        double population = Double.parseDouble(atributes[2]);
                        String code = atributes[3];
                        addCountry(id, name, population, code);

                    } else {
                        String[] comand = arr[i].split("VALUES");
                        String values = comand[1];
                        values = values.replace(" (", "");
                        values = values.replace(")", "");
                        values = values.replace(" ", "");
                        String[] atributes = values.split(",");
                        checkCountryId(atributes[2]);
                        String id = atributes[0];
                        String name = atributes[1];
                        String code = atributes[2];
                        double population = Double.parseDouble(atributes[3]);
                        addCity(id, name, code, population);

                    }

                    serializable.add(arr[i]);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Files were loaded");
        } else {
            //SI NO EXISTE, DECIMOS QUE NO EXISTE JAJA ._.
            System.out.println("This file doesn't exist.");
        }

    }

    public void autoSave() {

        String json = gson.toJson(serializable);

        try {
            FileOutputStream fos = new FileOutputStream(new File("DataBase.txt"));
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
