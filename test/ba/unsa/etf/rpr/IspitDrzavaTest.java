package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Osnovni test klasa Drzava, Kraljevina i Republika
class IspitDrzavaTest {
    @Test
    void testKraljevina() {
        Drzava vbr = new Kraljevina();
        vbr.setNaziv("Velika Britanija");
        Grad london = new Grad(1, "London", 8825000, vbr);
        vbr.setGlavniGrad(london);
        assertEquals("Kraljevina Velika Britanija", vbr.getNaziv());
        assertEquals("London", vbr.getGlavniGrad().getNaziv());
        assertEquals("Kraljevina Velika Britanija", london.getDrzava().getNaziv());
        assertFalse(vbr instanceof Republika);
    }

    @Test
    void testRepublika() {
        Grad pariz = new Grad(1, "Pariz", 2206488, null);
        Drzava francuska = new Republika(1, "Francuska", pariz);
        pariz.setDrzava(francuska);
        assertEquals("Republika Francuska", francuska.getNaziv());
        assertEquals("Pariz", francuska.getGlavniGrad().getNaziv());
        assertEquals("Republika Francuska", pariz.getDrzava().getNaziv());
        assertFalse(francuska instanceof Kraljevina);
    }

    @Test
    void testDrzava() {
        Drzava bih = new Drzava();
        bih.setNaziv("Bosna i Hercegovina");
        Grad sarajevo = new Grad(1, "Sarajevo", 500000, bih);
        bih.setGlavniGrad(sarajevo);
        assertEquals("Bosna i Hercegovina", bih.getNaziv());
        assertEquals("Sarajevo", bih.getGlavniGrad().getNaziv());
        assertEquals("Bosna i Hercegovina", sarajevo.getDrzava().getNaziv());
        assertFalse(bih instanceof Republika);
        assertFalse(bih instanceof Kraljevina);
    }

}