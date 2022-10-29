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
        System.out.println("************************************\n"+"Welcome to the best Geographic app\n"+"************************************");
        System.out.println("1: Insert Command\n"+
                           "2: IMPORT FROM .SQL FILE\n"+
                           "0: salir");

        int option= Integer.parseInt(read.nextLine());

        return option;
    }

    public void executeMenu(int option){
        int option1;
        String ans;
        switch (option) {
            case 1:
                /*System.out.println("ingrese 1: añadir country\n" +
                        "ingrese 2: añadir city");
                option1 = read.nextInt();
                insertInto(option1);
                map.autoSave();
                break;*/
                System.out.println("Type the command");
                String comand= read.nextLine();
                try {
                    if (comand.contains("INSERT")) {
                        if (comand.contains("countries")) {
                            insertInto(1, comand);
                        } else {
                            insertInto(2, comand);
                        }
                    } else if (comand.contains("SELECT * FROM") && !comand.contains("ORDER")) {

                        if (comand.contains("name")) {
                            selectFrom(1, comand);
                        } else if (comand.contains(">")) {
                            selectFrom(2, comand);
                        } else if (comand.contains("<")) {
                            selectFrom(3, comand);
                        } else if (!comand.contains("WHERE")) {
                            selectFrom(4, comand);
                        }

                    } else if (comand.contains("ORDER")) {
                        if (comand.contains(">")) {
                            orderBy(1, comand);
                        } else if (comand.contains("name")) {
                            orderBy(2, comand);
                        }
                    } else if (comand.contains("DELETE")) {
                        if (comand.contains("=")) {
                            delete(1, comand);
                        }else{
                            delete(2,comand);
                        }
                    } else {
                        throw new InexistentComandException(comand);
                    }
                }catch (InexistentComandException ex){
                    ex.printStackTrace();
                }
                break;
            case 2:
                System.out.println("Please type the name of the .txt file (not incuding the \".txt\"");

                ans = read.nextLine();

                map.importSQL(ans,option);
                map.autoSave();

                break;

            case 3:
                System.out.println("BYE");
                break;
            default:
                System.out.println("invalid option");
                break;
        }
    }

    public void insertInto(int option, String comand){
        try {
            if (option == 1) {
                String[] a = comand.split("VALUES");
                map.comprobateInsertComand(comand);
                String w= a[1];
                w=w.replace(" (","");
                w=w.replace(")","");
                w=w.replace(" ","");
                System.out.println(w);
                String []comprobate= w.split(",");
                String id=comprobate[0];
                String name= comprobate[1];
                double population= Double.parseDouble(comprobate[2]);
                String code= comprobate[3];
                map.addCountry(id,name,population,code);
                map.addserializableCountry(comand);

            } else {

                String[] a = comand.split("VALUES");
                map.comprobateInsertComand(a[0]);
                String w= a[1];
                w=w.replace(" (","");
                w=w.replace(")","");
                w=w.replace(" ","");
                String []comprobate= w.split(",");
                //System.out.println(comprobate[0].hashCode()+" "+"'e4aa04f6-3dd0-11ed-b878-0242ac120002'".hashCode());

                map.comprobateIdCountry(comprobate[2]);
                String id= comprobate[0];
                String name= comprobate[1];
                String code= comprobate[2];
                double population= Double.parseDouble(comprobate[3]);
                map.addCity(id,name,code,population);
                map.addserializableCountry(comand);
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

    public void selectFrom(int option, String comand){

        try {
            if (option==1) {

                map.comprobateSelectComand(comand,option);
                String[] country= comand.split("=");
                if(country[0].equals( "SELECT * FROM countries WHERE name ")){
                    String t= country[1];
                    t= t.replace(" ","");
                    if(map.searchCountry(t)){
                        System.out.println("El pais si existe");
                    }else{
                        System.out.println("El pais no existe");
                    }
                }else{
                    String t= country[1];
                    t= t.replace(" ","");
                    if(map.searchCity(t)){
                        System.out.println("la ciudad si existe");
                    }else{
                        System.out.println("la ciudad no existe");
                    }
                }

            }else if(option==2){

                map.comprobateSelectComand(comand,option);
                String []Population= comand.split(">");
                if(Population[0].equals("SELECT * FROM countries WHERE population ")){
                    String w = Population[1];
                    w = w.replace(" ", "");
                    double pop = Double.parseDouble(w);
                    ArrayList<String> arr = map.searchBigPopulation(pop);
                    if (arr.isEmpty()) {
                        System.out.println("No existe algun pais con una poblacion mayor que "+pop+" millones");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                }else{
                    String w = Population[1];
                    w = w.replace(" ", "");
                    double pop = Double.parseDouble(w);
                    ArrayList<String> arr = map.searchBigCPopulation(pop);
                    if (arr.isEmpty()) {
                        System.out.println("No existe alguna ciudad con una poblacion mayor que "+pop+" millones");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                }
            }else if(option==3){

                map.comprobateSelectComand(comand,option);
                String []Population= comand.split("<");
                if(Population[0].equals("SELECT * FROM countries WHERE population ")) {
                    String w = Population[1];
                    w = w.replace(" ", "");
                    double minPop = Double.parseDouble(w);
                    ArrayList<String> arr = map.searchMinPopulation(minPop);
                    if (arr.isEmpty()) {
                        System.out.println("No existe algun pais con una poblacion menor que "+minPop+" millones");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                }else{
                    String w = Population[1];
                    w = w.replace(" ", "");
                    double minPop = Double.parseDouble(w);
                    ArrayList<String> arr = map.searchMinCPopulation(minPop);
                    if (arr.isEmpty()) {
                        System.out.println("No existe alguna ciudad con una poblacion menor que "+minPop+" millones");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                }
            }else{

                map.comprobateSelectComand(comand,option);
                String []Population= comand.split("WHERE");
                if(Population[0].equals("SELECT * FROM countries")) {
                    ArrayList<String> arr = map.getCountries();
                    if (arr.isEmpty()) {
                        System.out.println("No existe algun pais");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                }else{
                    ArrayList<String> arr = map.getCities();
                    if (arr.isEmpty()) {
                        System.out.println("No existe alguna ciudadffgdx");
                    } else {
                        for (int i = 0; i < arr.size(); i++) {
                            System.out.println(arr.get(i));
                        }
                    }
                }
            }
        }catch (InexistentComandException ex){
            ex.printStackTrace();
        }
    }

    public void orderBy(int option, String comand){

        try {
            if (option == 1) {


                map.comprobateOrderByComand(comand, option);
                String[] atribute = comand.split("BY ");
                map.comprobateAtribute(atribute[1],option);
                String [] criteria= comand.split(" ");
                double cr= Double.parseDouble(criteria[7]);
                ArrayList<String> arr= map.orderSelection(option,comand,cr,"",atribute[1]);
                for(int i=0; i<arr.size(); i++){
                    System.out.println(arr.get(i));
                }

            }else{


                map.comprobateOrderByComand(comand,option);
                String[] atribute = comand.split("BY ");
                map.comprobateAtribute(atribute[1],option);
                String [] criteria= comand.split(" ");
                String name= criteria[7];
                ArrayList<String> arr= map.orderSelection(option,comand,0,name,atribute[1]);
                for(int i=0; i<arr.size(); i++){
                    System.out.println(arr.get(i));
                }

            }
        }catch (InexistentComandException ex){
            ex.printStackTrace();
        }catch (NoAtributeException ex){
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            ex.printStackTrace();
            ex.getMessage();
        }
    }

    public void delete(int option, String command){
        try {
            if (option == 1) {
                map.comprobateDelete(option, command);
                String[] comprobate = command.split(" ");
                if (comprobate[4].equalsIgnoreCase("population")) {
                    String pop = String.valueOf(comprobate[6]);
                    map.deletePlace(option, comprobate[2], comprobate[4], pop, 0,"");
                } else {
                    map.deletePlace(option, comprobate[2], comprobate[4], comprobate[6], 0,"");
                }
            }else{
                map.comprobateDelete(option,command);
                String [] comprobate= command.split(" ");
                double pop= Double.parseDouble(comprobate[6]);
                map.deletePlace(option, comprobate[2],"","",pop,comprobate[5]);
            }
        }catch (InexistentComandException ex){
            ex.printStackTrace();
        }catch (NoAtributeException ex){
            ex.printStackTrace();
        }
    }
}
