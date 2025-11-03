package dat.daos;

import dat.Populator.PopulatorTemplate;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("IntegrationTest")
class DAOTestTemplate {
    static private EntityManagerFactory emf;
    static private IDAO dao;


    @BeforeAll
    static void setupOnce() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        // dao = new something dao(emf)
    }

    @BeforeEach
    void setup() {
        PopulatorTemplate.populate();
    }

    @AfterEach
        //delete all data
    void teardown() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        //delete everything
        em.createNativeQuery("TRUNCATE TABLE * RESTART IDENTITY CASCADE")
                .executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @AfterAll
    static void tearDownOnce() {
        if (emf != null) emf.close();
    }

    @Test
    void read() {
        //get objects with the same id
        Object expected = PopulatorTemplate.fetch().get(1);
        Object actual = dao.read(1);
        assertEquals(expected, actual);
    }

    @Test
    void readAll() {
        List<?> expected = PopulatorTemplate.fetch();
        List<?> actual = dao.readAll();
        assertThat((Iterable<?>) actual, containsInAnyOrder(expected.toArray()));
    }


    @Test
    void create() {
        Object expected = new Object();
        Object created = dao.create(expected);
        //createds id
        Optional<Object> actualinDB = dao.read(1);
        assertEquals(expected, actualinDB.get());
        assertEquals(created, actualinDB.get());
    }

    @Test
    void update() {
        int id = 1;
        Object expected = new Object();
        Object created = dao.update(id, expected);
        //expecteds id
        Optional<Object> actualinDB = dao.read(id);
        assertEquals(expected, actualinDB.get());
        assertEquals(created, actualinDB.get());
    }

    @Test
    void delete() {
        int id = 1;
        // Act
        boolean actual = dao.delete(id);
        Optional<Object> read = dao.read(id);
        // Assert
        assertTrue(actual);
        assertTrue(read.isEmpty());
    }
}
