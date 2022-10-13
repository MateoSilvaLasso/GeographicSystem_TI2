package ui;

import model.GeographicController;

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
        System.out.println("************************************\n"+"Welcome to the best Geographic app\n"+"***********************************");
        System.out.println("1: INSERT INTO\n"+
                           "2: SELECT*FROM\n"+
                           "3: ");

        int option= read.nextInt();

        return option;
    }

    public void executeMenu(int option){
        int option1;
        switch (option){
            case 1:
                System.out.println("ingrese 1: añadir country\n"+
                                   "ingrese 2: añadir city");
                option1=read.nextInt();
                insertInto(option1);
                break;
            case 2:
                System.out.println("ingrese 1: buscar country por name\n"+
                                   "ingrese 2: buscar countries con poblacion mayor que 100\n" +
                                   "ingrese 3: buscar countries con poblacion menor que 30\n"+
                                   "ingrese 4: buscar todos los countries");
                option1= read.nextInt();
                selectFrom(option1);


        }
    }

    public void insertInto(int option){
        try {
            if (option == 1) {

                System.out.println("Ingrese la informacion en el siguiente formato:\n" +
                        "INSERT INTO countries(id,name,population,countryCode) VALUES ('value', 'value', value, 'value')");
                read.nextLine();
                String country = read.nextLine();
                String[] a = country.split("VALUES");
                map.comprobateInsertComand(a[0]);
                String w= a[1];
                w=w.replace(" (","");
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
                read.nextLine();
                String city = read.nextLine();
                String[] a = city.split("VALUES");
                map.comprobateInsertComand(a[0]);
                String w= a[1];
                w=w.replace(" (","");
                w=w.replace(")","");
                String []comprobate= w.split(",");
                System.out.println(comprobate[0].hashCode()+" "+"'e4aa04f6-3dd0-11ed-b878-0242ac120002'".hashCode());
                map.comprobateIdCountry(comprobate[2]);
                String id= comprobate[0];
                String name= comprobate[1];
                String code= comprobate[2];
                double population= Double.parseDouble(comprobate[3]);
                map.addCity(id,name,code,population);
                map.addserializableCountry(city);
            }
        }catch (InexistentComandException ex){
            ex.printStackTrace();
        }catch (IncorrectFormatExcepcion ex){
            ex.printStackTrace();
        }catch (NoCountryIDException ex){
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }

    public void selectFrom(int option){
        try {
            if (option==1) {
                System.out.println("Ingrese la información en el siguiente formato\n"+ "SELECT*FROM cities WHERE name = 'Value'");
                String comand= read.nextLine();
                map.comprobateSelectComand(comand);
            }
        }catch (InexistentComandException ex){
            ex.printStackTrace();
        }
    }
}
