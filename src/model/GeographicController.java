package model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;

import Exception.*;
import com.google.gson.Gson;

public class GeographicController {
    ArrayList<Country> countries;
    ArrayList<String> serializable;
    Gson gson = new Gson();

    public GeographicController() {
        this.countries = new ArrayList<>();
        this.serializable = new ArrayList<String>();
    }

    public void addCountry(String id, String name, double population, String countryCode) throws IncorrectFormatExcepcion {
        Country e = new Country(id, name, population, countryCode);
        this.countries.add(e);
    }

    public void addserializableCountry(String s) {
        this.serializable.add(s);
    }

    public void comprobateInsertComand(String s) throws InexistentComandException {
        String[] arr = s.split("VALUES");
        String n = arr[0];
        String a = "";
        for (int i = 0; i < 18; i++) {
            a += n.charAt(i);
        }
        if (!a.equals("INSERT INTO cities") && !a.equals("INSERT INTO countr")) {
            throw new InexistentComandException(s);
        } else if (a.equals("INSERT INTO countries")) {
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

        if (a.equals("INSERT INTO cities")) {
            format = n.split("cities");
            String w = format[1];
            w = w.replace("(", "");
            w = w.replace(") ", "");
            String[] x = w.split(",");
            if (!x[0].equals("id") || !x[1].equals("name") || !x[2].equals("countryID") || !x[3].equals("population")) {
                throw new InexistentComandException(s);
            }
        } else {
            format = n.split("countries");
            String w = format[1];
            w = w.replace("(", "");
            w = w.replace(")", "");

            String[] x = w.split(",");

            for (int i = 0; i < x.length; i++) {
                String c = x[i];
                if (c.equals("id") || c.equals("name") || c.equals("population") || c.equals("countryCode ")) {

                } else {

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

    public void comprobateAtribute(String s, int option)throws NoAtributeException {
        if(option==1){
            if(!s.equals("id") && !s.equals("name") && !s.equals("population") && !s.equals("countryCode")){
                throw new NoAtributeException(s);
            }
        }else{
            if(!s.equals("id") && !s.equals("name") && !s.equals("population") && !s.equals("countryId")){
                throw new NoAtributeException(s);
            }
        }
    }

    public void comprobateIdCountry(String s) throws NoCountryIDException {

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

    public void comprobateSelectComand(String s, int option) throws InexistentComandException {
        String[] arr;
        if (option == 1) {
            arr = s.split("=");
            if (!arr[0].equals("SELECT*FROM cities WHERE name ")) {
                throw new InexistentComandException(s);
            }
        } else if (option == 2) {
            arr = s.split(">");
            if (!arr[0].equals("SELECT*FROM countries WHERE population ")) {
                throw new InexistentComandException(s);
            }
        } else if (option == 3) {
            arr = s.split("<");
            if (!arr[0].equals("SELECT*FROM countries WHERE population ")) {
                throw new InexistentComandException(s);
            }
        }else{
            arr= s.split("WHERE");
            if(!arr[0].equals("SELECT*FROM countries ")){
                throw new InexistentComandException(s);
            }
        }
    }

    public void comprobateOrderByComand(String s,  int option) throws InexistentComandException{
        String [] arr;
        if(option == 1){
            arr= s.split(" ");
            if(!arr[0].equals("SELECT*FROM") || !arr[1].equals("countries") || !arr[2].equals("WHERE") || !arr[3].equals("population") || (!arr[4].equals(">") && !arr[4].equals("<")) || !arr[6].equals("ORDER") || !arr[7].equals("BY")){
                throw new InexistentComandException(s);
            }
        }else{
            arr= s.split(" ");
            if(!arr[0].equals("SELECT*FROM") || !arr[1].equals("cities") || !arr[2].equals("WHERE") || !arr[3].equals("name") || !arr[4].equals("=") || !arr[6].equals("ORDER") || !arr[7].equals("BY")){
                throw new InexistentComandException(s);
            }
        }
    }

    public void addCity(String id, String name, String CountryCode, double population) throws IncorrectFormatExcepcion {

        City city = new City(id, name, CountryCode, population);

        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getId().equals(CountryCode)) {
                countries.get(i).addCity(city);
            }
        }


    }

    public boolean searchCountry(String s) {
        boolean flag = false;

        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getName().equals(s)) {
                flag = true;
            }
        }

        return flag;
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

    public ArrayList<String> searchMinPopulation(double p) {
        ArrayList<String> pop = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            if(countries.get(i).getPopulation()<=p)
                pop.add(countries.get(i).getName());
        }

        return pop;
    }

    public ArrayList<String> orderSelection(int option, String comand, double number,String name, String atribute){
        ArrayList<String> all= new ArrayList<>();
        if(option==1){
            ArrayList<Country> arr= new ArrayList<>();
            String[] comprobate= comand.split(" ");
            if(comprobate[4].equals("<")){
                for(int i=0; i<countries.size(); i++){
                    if(countries.get(i).getPopulation()<number){
                        arr.add(countries.get(i));
                    }
                }
            }else if(comprobate[4].equals(">")){
                for(int i=0; i<countries.size(); i++){
                    if(countries.get(i).getPopulation()>number){
                        arr.add(countries.get(i));
                    }
                }
            }

            if(atribute.equals("id")){
                Collections.sort(arr,(a,b)->{
                    return a.getId().compareTo(b.getId());
                });
            }else if(atribute.equals("name")){
                Collections.sort(arr,(a,b)->{
                    return a.getName().compareTo(b.getName());
                });
            }else if(atribute.equals("population")){
                Collections.sort(arr,(a,b)->{
                    if(a.getPopulation()<b.getPopulation()){
                        return -1;
                    }else{
                        return 1;
                    }
                });

            }else{
                Collections.sort(arr,(a,b)->{
                   return a.getCountryCode().compareTo(b.getCountryCode());
                });
            }

            for(int i=0; i<arr.size(); i++){
                String objetive= arr.get(i).getId()+", "+ arr.get(i).getName()+", "+arr.get(i).getPopulation()+", "+ arr.get(i).getCountryCode();
                all.add(objetive);
            }


        }else{
            ArrayList<City> arr= new ArrayList<>();
            for(int i=0; i<countries.size(); i++){
                for(int j=0; j<countries.get(i).getCities().size(); j++){
                    if(countries.get(i).getCities().get(j).getName().equals(name)){
                        //System.out.println();
                        arr.add(countries.get(i).getCities().get(j));
                    }
                }
            }

            if(atribute.equals("id")){
                Collections.sort(arr,(a,b)->{
                    return a.getId().compareTo(b.getId());
                });
            }else if(atribute.equals("name")){
                Collections.sort(arr,(a,b)->{
                    return a.getName().compareTo(b.getName());
                });
            }else if(atribute.equals("countryId")){
                Collections.sort(arr, (a,b)->{
                    return a.getCountryId().compareTo(b.getCountryId());
                });
            }else{
                Collections.sort(arr,(a,b)->{
                    if(a.getPopulation()<b.getPopulation()){
                        return -1;
                    }else{
                        return 1;
                    }
                });
            }

            for(int i=0; i<arr.size(); i++){
                String objetive= arr.get(i).getId()+", "+ arr.get(i).getName()+", "+ arr.get(i).getCountryId() +", "+arr.get(i).getPopulation();
                all.add(objetive);
            }

        }


        return all;
    }



    public ArrayList<String> getCountries() {
        return this.serializable;
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
                while ((line = reader.readLine()) != null) {
                    //COMPROBAMOS QUE EL ARCHIVO TENGA CONTENIDO EN LA LINEA Y QUE ESE CONTENIDO TENGA UNA INSTRUCCION VALIDA
                        comprobateInsertComand(line);

                        String [] separable= line.split("\\(");
                        if(separable[0].equals("INSERT INTO countries")){
                            String [] comand= line.split("VALUES");
                            String values= comand[1];
                            values= values.replace(" (", "");
                            values= values.replace(")","");
                            String[] atributes= values.split(",");
                            String id=atributes[0];
                            String name= atributes[1];
                            double population= Double.parseDouble(atributes[2]);
                            String code= atributes[3];
                            addCountry(id,name,population,code);

                        }else{
                            String [] comand= line.split("VALUES");
                            String values= comand[1];
                            values= values.replace(" (", "");
                            values= values.replace(")","");
                            String[] atributes= values.split(",");
                            comprobateIdCountry(atributes[2]);
                            String id= atributes[0];
                            String name= atributes[1];
                            String code= atributes[2];
                            double population= Double.parseDouble(atributes[3]);
                            addCity(id,name,code,population);

                        }

                    serializable.add(line);


                }
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
