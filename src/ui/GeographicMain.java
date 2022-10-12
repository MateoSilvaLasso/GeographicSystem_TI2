package ui;

import model.GeographicController;

import java.util.ArrayList;
import java.util.Scanner;

import Exception.*;

public class GeographicMain {

    private GeographicController map;

    private Scanner read;

    public GeographicMain(){
        map= new GeographicController();
        read= new Scanner(System.in);
    }
    public static void main(String[] args) {
        GeographicMain op= new GeographicMain();
        int option=0;
        do{
            option= op.menu();
            op.executeMenu(option);
        }while(option!=0);
    }

    public int menu(){
        System.out.println("************************************\n"+"Welcome to the best Geographic app\n"+"*****************************");
        System.out.println("1: INSERT INTO\n"+
                           "2: \n"+
                           "3: ");

        int option= read.nextInt();

        return option;
    }

    public void executeMenu(int option){
        switch (option){
            case 1:
                System.out.println("ingrese 1: añadir country\n"+
                                   "ingrese 2: añadir city");
                int option1=read.nextInt();
                insertInto(option1);
                ArrayList<String> arr= map.getCountries();
                for(int i=0; i<arr.size(); i++){
                    System.out.println(arr.get(i));
                }
                break;

        }
    }

    public void insertInto(int option){
        try {
            if (option == 1) {

                System.out.println("Ingrese la informacion en el siguiente formato:\n" +
                        "INSERT INTO countries(id, name, population, countryCode) VALUES ('value', 'value', value, 'value')");
                read.nextLine();
                String country = read.nextLine();
                String[] a = country.split("VALUES");
                map.comprobateComand(a[0]);
                String w= a[1];
                w=w.replace("(","");
                w=w.replace(")","");
                String []comprobate= w.split(",");
                String id=comprobate[0];
                String name= comprobate[1];
                double population= Double.parseDouble(comprobate[2]);
                String code= comprobate[3];
                map.addCountry(id,name,population,code);
                map.addserializableCountry(country);

            } else {
                System.out.println("Ingrese la informacion en el siguiente formato:\n" +
                        "INSERT INTO cities(id, name, population, countryCode) VALUES ('value', 'value', value, 'value')");
                String city = read.nextLine();
                String[] a = city.split("VALUES");
                map.comprobateComand(a[0]);
                String w= a[1];
                w=w.replace("(","");
                w=w.replace(")","");
                String []comprobate= w.split(",");
                map.comprobateIdCountry(comprobate[3]);
                String id= comprobate[0];
                String name= comprobate[1];
                String code= comprobate[3];
                double population= Double.parseDouble(comprobate[2]);
                map.addCity(id,name,code,population);
                map.addserializableCountry(city);
            }
        }catch (InexistentComandException ex){
            ex.printStackTrace();
        }catch (IncorrectFormatExcepcion ex){
            ex.printStackTrace();
        }catch (NoCountryIDException ex){
            ex.printStackTrace();
        }
    }
}
