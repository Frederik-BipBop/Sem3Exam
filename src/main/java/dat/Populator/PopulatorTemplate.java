package dat.Populator;

import dat.daos.IDAO;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class PopulatorTemplate{

    private static EntityManagerFactory EMF;
    private static IDAO iDao;
    private static List data = new ArrayList<>();

    public static void setEMF(EntityManagerFactory emf){
        EMF = emf;
        //this.iDao = new something something dao(emf) //create new dao based on emf
    }

    public static void populate() {
        //create a bunch of test objects that get persisted
        data.forEach(iDao::create);
    }

    public static List fetch() {
        return iDao.readAll();
    }
}
