package test;

import exception.NoCountryIDException;
import model.GeographicController;
import exception.IdAlreadyUsedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeographicControllerTest {

    private GeographicController controller;

    public void setup1(){
        controller = new GeographicController();
    }

    public void setup2(){
        controller = new GeographicController();
        controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('01','Colombia',50.2,'+57')");
        controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('02','Mexico',27,'+52')");
    }

    public void setup3(){
        controller = new GeographicController();
        controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('01','Colombia',50.2,'+57')");
        controller.insertInto(2, "INSERT INTO cities(id, name, countryID, population) VALUES ('011','Cali','01',1.4)");
        controller.insertInto(2, "INSERT INTO cities(id, name, countryID, population) VALUES ('012','Bogota','01',3)");
        controller.insertInto(2, "INSERT INTO cities(id, name, countryID, population) VALUES ('013','Pasto','01',1.2)");
        controller.insertInto(2, "INSERT INTO cities(id, name, countryID, population) VALUES ('014','Medellin','01',5.5)");
    }

    public void setup4(){
        controller = new GeographicController();
        controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('01','Colombia',100.1,'+57')");
        controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('02','Mexico',99,'+52')");
        controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('03', 'Venezuela', 150, '+58')");
    }

    @Test
    public void checkIfInsertWorksCountryTest(){

        setup1();
        //adding the countries
        controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('01','Colombia',50.2,'+57')");
        controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('02','Mexico',27,'+52')");
        controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('03', 'Venezuela', 34.6, '+58')");
        //we are checking here if something was added to the country arraylist
        assertFalse(controller.getCountries().isEmpty());
        //we are checking here if Colombia was added
        assertEquals("'Colombia'", controller.getCountries().get(0));
        //we are checking here if Mexico was added
        assertEquals("'Mexico'", controller.getCountries().get(1));
        //we are checking here if Venezuela was added
        assertEquals("'Venezuela'", controller.getCountries().get(2));
    }

    @Test
    public void checkIfInsertWorksCityTest(){
        setup2();
        //adding the cities
        controller.insertInto(2, "INSERT INTO cities(id, name, countryID, population) VALUES ('011','Cali','01',1.4)");
        controller.insertInto(2, "INSERT INTO cities(id, name, countryID, population) VALUES ('012','Bogota','01',3)");
        controller.insertInto(2, "INSERT INTO cities(id, name, countryID, population) VALUES ('013','CDMX','02',4.3)");
        controller.insertInto(2, "INSERT INTO cities(id, name, countryID, population) VALUES ('014','Cancun','02',4)");
        //we are checking here if something was added to the cities arraylist
        assertFalse(controller.getCities().isEmpty());
        //we are checking here if Cali and Bogota were added to colombia (Colombia's id is '01')
        assertEquals("'Cali' '01'", controller.getCitiesCountry().get(0));
        assertEquals("'Bogota' '01'", controller.getCitiesCountry().get(1));
        //we are checking here if CDMX and Cancun were added to Mexico (Mexicos's id is '02')
        assertEquals("'CDMX' '02'", controller.getCitiesCountry().get(2));
        assertEquals("'Cancun' '02'", controller.getCitiesCountry().get(3));

    }

    @Test
    public void checkRepeatedIdTest(){
        setup1();
        //adding the countries
        try{
            controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('01','Colombia',50.2,'+57')");
            controller.insertInto(1, "INSERT INTO countries(id, name, population, countryCode) VALUES ('01','Mexico',27,'+52')");
        }catch (IdAlreadyUsedException e){
            //catching the exception
            System.out.println(e.toString());
        }
        //we are checking here if Colombia wasn't updated
        assertTrue(controller.searchCountryBool("'Colombia'"));
        //we are checking here if mexico wasn't added
        assertFalse(controller.searchCountryBool("'Mexico'"));

    }

    @Test
    public void checkIfCityCanBeAddedToAnNonexistentCountryIdTest(){
        setup1();
        try {
            //insert a city to a non-existent country
            controller.insertInto(2, "INSERT INTO cities(id, name, countryID, population) VALUES ('011','Cali','01',1.4)");
        }catch (NoCountryIDException e){
            //catch the exception
            System.out.println(e.toString());
        }
        //we are checking here if the city wasn't added
        assertFalse(controller.searchCityBool("'Cali'"));
    }

    @Test
    public void selectCountryCitiesTest(){
        setup3();
        controller.selectFrom(1, "SELECT * FROM cities WHERE countryId = '01'");
    }

    @Test
    public void nameOrderTest(){
        setup3();
    }

    @Test
    public void countriesWithPopulationGreaterThan100Test(){
        setup4();
    }
}