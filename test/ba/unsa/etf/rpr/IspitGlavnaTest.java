package ba.unsa.etf.rpr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

// Testovi glavne forme
// Pobrinite se najprije da prolaze testovi u ostalim Ispit* klasama
@ExtendWith(ApplicationExtension.class)
public class IspitGlavnaTest {
    Stage theStage;
    GlavnaController ctrl;

    @Start
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavna.fxml"));
        ctrl = new GlavnaController();
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Grad");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();

        stage.toFront();

        theStage = stage;
    }

    @Test
    public void testDodajDrzavu(FxRobot robot) {
        ctrl.resetujBazu();

        // Otvaranje forme za dodavanje
        robot.clickOn("#btnDodajDrzavu");

        // Čekamo da dijalog postane vidljiv
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        // Postoji li fieldNaziv
        robot.clickOn("#fieldNaziv");
        robot.write("Hrvatska");

        robot.clickOn("#tglRepublika");

        // Klik na dugme Ok
        robot.clickOn("#btnOk");

        // Da li je Hrvatska dodana u bazu?
        GeografijaDAO dao = GeografijaDAO.getInstance();
        assertEquals(4, dao.drzave().size());

        Drzava hrvatska = null;
        for(Drzava drzava : dao.drzave())
            if (drzava.getNaziv().equals("Republika Hrvatska"))
                hrvatska = drzava;
        assertNotNull(hrvatska);
        assertTrue(hrvatska instanceof Republika);
    }

    @Test
    public void testIzmijeniDrzavu(FxRobot robot) {
        ctrl.resetujBazu();

        // Velika Britanija ne smije biti kraljevina jer je to "varanje"
        GeografijaDAO dao = GeografijaDAO.getInstance();
        Drzava vb = dao.nadjiDrzavu("Velika Britanija");
        assertFalse(vb instanceof Kraljevina);

        // Mijenjamo državu za grad London
        robot.clickOn("London");
        robot.clickOn("#btnIzmijeniDrzavu");

        // Čekamo da dijalog postane vidljiv
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        robot.clickOn("#tglKraljevina");

        // Klik na dugme Ok
        robot.clickOn("#btnOk");

        // Da li je Velika Britanija kraljevina?
        Drzava vb2 = dao.nadjiDrzavu("Velika Britanija");
        assertTrue(vb2 instanceof Kraljevina);
    }

    @Test
    public void testIzmijeniDrzavu2(FxRobot robot) {
        ctrl.resetujBazu();

        // Francuska ne smije biti republika jer je to "varanje"
        GeografijaDAO dao = GeografijaDAO.getInstance();
        Drzava francuska = dao.nadjiDrzavu("Francuska");
        assertFalse(francuska instanceof Republika);

        // Mijenjamo državu za grad Pariz
        robot.clickOn("Pariz");
        robot.clickOn("#btnIzmijeniDrzavu");

        // Čekamo da dijalog postane vidljiv
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        robot.clickOn("#tglRepublika");

        // Klik na dugme Ok
        robot.clickOn("#btnOk");

        // Da li je Francuska republika?
        Drzava francuska2 = dao.nadjiDrzavu("Francuska");
        assertTrue(francuska2 instanceof Republika);
    }

}
