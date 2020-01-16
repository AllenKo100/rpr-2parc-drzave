package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

// Testovi DAO klase
// Pobrinite se najprije da prolaze testovi u klasi IspitDrzavaTest
public class IspitDAOTest {

    @Test
    void testDodajDrzavu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();

        GeografijaDAO dao = GeografijaDAO.getInstance();

        // Najprije moramo kreirati grad koji će biti glavni grad nove države
        Drzava francuska = dao.nadjiDrzavu("Francuska");
        Grad zagreb = new Grad(0, "Zagreb", 1000000, francuska);
        dao.dodajGrad(zagreb);

        // Uzimamo novu referencu da bi ona imala korektan id
        Grad zg2 = dao.nadjiGrad("Zagreb");
        Drzava hrvatska = new Republika(0, "Hrvatska", zg2);
        dao.dodajDrzavu(hrvatska);

        assertEquals(4, dao.drzave().size());

        Drzava hr2 = dao.nadjiDrzavu("Hrvatska");
        assertNotNull(hr2);
        assertEquals("Republika Hrvatska", hr2.getNaziv());
    }

    @Test
    void testIzmijeniDrzavu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();

        // Uzimamo državu preko glavnog grada
        GeografijaDAO dao = GeografijaDAO.getInstance();
        Grad pariz = dao.glavniGrad("Francuska");
        Drzava francuska = pariz.getDrzava();
        // Nećemo da Francuska bude unaprijed postavljena kao republika jer bi to bilo "varanje"
        assertFalse(francuska instanceof Republika);

        Drzava francuskaRepublika = new Republika(francuska.getId(), francuska.getNaziv(), francuska.getGlavniGrad());
        dao.izmijeniDrzavu(francuskaRepublika);

        assertEquals(3, dao.drzave().size());

        Grad p2 = dao.glavniGrad("Francuska");
        Drzava d2 = p2.getDrzava();
        assertTrue(d2 instanceof Republika);
        assertEquals("Republika Francuska", d2.getNaziv());
    }

    @Test
    void testIzmijeniDrzavuDvaput() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();

        GeografijaDAO dao = GeografijaDAO.getInstance();

        Drzava vb = dao.nadjiDrzavu("Velika Britanija");
        // Nećemo da Velika Britanija bude unaprijed postavljena kao kraljevina jer bi to bilo "varanje"
        assertFalse(vb instanceof Kraljevina);
        Drzava vb2 = new Kraljevina(vb.getId(), vb.getNaziv(), vb.getGlavniGrad());
        dao.izmijeniDrzavu(vb2);

        System.out.println("Drugi poziv nadjiDrzavu");
        Drzava vb3 = dao.nadjiDrzavu("Velika Britanija");
        assertEquals("Kraljevina Velika Britanija", vb3.getNaziv());
        assertTrue(vb3 instanceof Kraljevina);

        // Ponovo pozivamo izmijeni drzavu
        vb3.setNaziv("Velika Britanija i Sjeverna Irska");
        dao.izmijeniDrzavu(vb3);

        Drzava vb4 = dao.nadjiDrzavu("Velika Britanija i Sjeverna Irska");
        assertNotNull(vb4);
        assertEquals("Kraljevina Velika Britanija i Sjeverna Irska", vb4.getNaziv());

        // Da li se izmjena aplicirala i na grad
        Grad london = dao.nadjiGrad("London");
        Drzava vb5 = london.getDrzava();
        assertEquals("Kraljevina Velika Britanija i Sjeverna Irska", vb5.getNaziv());
    }

}
