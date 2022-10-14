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
        System.out.println("************************************\n"+"Welcome to the best Geographic app\n"+"***********************************");
        System.out.println("1: INSERT INTO\n"+
                           "2: SELECT*FROM\n"+
                           "3: IMPORT FROM .SQL FILE");

        int option= Integer.parseInt(read.nextLine());

        return option;
    }

    public void executeMenu(int option){
        int option1;
        String ans;
        switch (option) {
            case 1:
                System.out.println("ingrese 1: añadir country\n" +
                        "ingrese 2: añadir city");
                option1 = read.nextInt();
                insertInto(option1);
                map.autoSave();
                break;
            case 2:
                System.out.println("ingrese 1: buscar country por name\n" +
                        "ingrese 2: buscar countries con poblacion mayor que 100\n" +
                        "ingrese 3: buscar countries con poblacion menor que 30\n" +
                        "ingrese 4: buscar todos los countries");
                option1 = Integer.parseInt(read.nextLine());
                selectFrom(option1);

                break;

            case 3:
                System.out.println("Please type the name of the .txt file (not incuding the \".txt\"");

                ans = read.nextLine();

                map.importSQL(ans,option);
                map.autoSave();
        }
    }

    public void insertInto(int option){
        try {
            if (option == 1) {

                System.out.println("Ingrese la informacion en el siguiente formato:\n" +
                        "INSERT INTO countries(id,name,population,countryCode) VALUES ('value','value',value,'value')");
                read.nextLine();
                String country = read.nextLine();
                String[] a = country.split("VALUES");
                map.comprobateInsertComand(country);
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
                        "INSERT INTO cities(id,name,population,countryCode) VALUES ('value','value',value,'value')");
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
        String comand="";
        try {
            if (option==1) {
                System.out.println("Ingrese la información en el siguiente formato\n"+ "SELECT*FROM cities WHERE name = 'Value'");
                comand= read.nextLine();
                map.comprobateSelectComand(comand,option);
                String[] country= comand.split("=");
                if(map.searchCountry(country[1])){
                    System.out.println("El pais no existe");
                }else{
                    System.out.println("El pais si existe");
                }
            }else if(option==2){
                System.out.println("ingrese la información en el siguiente formato\n"+"SELECT*FROM countries WHERE population > 100");
                comand= read.nextLine();
                map.comprobateSelectComand(comand,option);
                String []Population= comand.split(">");
                String w= Population[1];
                w= w.replace(" ","");
                double pop= Double.parseDouble(w);
                ArrayList<String> arr= map.searchBigPopulation(pop);
                if(arr.isEmpty()){
                    System.out.println("No existe algun pais con una poblacion mayor que 100 millones");
                }else{
                    for(int i=0; i<arr.size(); i++){
                        System.out.println(arr.get(i));
                    }
                }
            }else if(option==3){
                System.out.println("ingrese la información en el siguiente formato\n"+"SELECT*FROM countries WHERE population < 30");
                comand= read.nextLine();
                map.comprobateSelectComand(comand,option);
                String []Population= comand.split("<");
                String w= Population[1];
                w= w.replace(" ","");
                double minPop= Double.parseDouble(w);
                ArrayList<String> arr= map.searchMinPopulation(minPop);
                if(arr.isEmpty()){
                    System.out.println("No existe algun pais con una poblacion menor que 30 millones");
                }else{
                    for(int i=0; i<arr.size(); i++){
                        System.out.println(arr.get(i));
                    }
                }
            }
        }catch (InexistentComandException ex){
            ex.printStackTrace();
        }
    }
}
