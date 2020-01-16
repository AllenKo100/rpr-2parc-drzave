package ba.unsa.etf.rpr;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;


// Testovi forme drzava.fxml kod kreiranja nove države
// Pobrinite se najprije da prolaze testovi u klasi IspitDrzavaTest
@ExtendWith(ApplicationExtension.class)
public class IspitDrzavaControllerTest {
    Stage theStage;
    DrzavaController ctrl;

    @Start
    public void start(Stage stage) throws Exception {
        GeografijaDAO dao = GeografijaDAO.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drzava.fxml"));
        ctrl = new DrzavaController(null, dao.gradovi());
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Država");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
        theStage = stage;
    }


    @Test
    public void testPoljaPostoje(FxRobot robot) {
        RadioButton rb = robot.lookup("#tglRepublika").queryAs(RadioButton.class);
        assertNotNull(rb);
        RadioButton rb2 = robot.lookup("#tglKraljevina").queryAs(RadioButton.class);
        assertNotNull(rb2);
        RadioButton rb3 = robot.lookup("#tglDrzava").queryAs(RadioButton.class);
        assertNotNull(rb3);
        // Država treba biti selected by default
        assertTrue(rb3.isSelected());
    }

    @Test
    public void testPoljaSuRadio(FxRobot robot) {
        RadioButton tglRepublika = robot.lookup("#tglRepublika").queryAs(RadioButton.class);
        RadioButton tglKraljevina = robot.lookup("#tglKraljevina").queryAs(RadioButton.class);
        RadioButton tglDrzava = robot.lookup("#tglDrzava").queryAs(RadioButton.class);

        robot.clickOn("#tglRepublika");
        assertTrue(tglRepublika.isSelected());
        assertFalse(tglKraljevina.isSelected());
        assertFalse(tglDrzava.isSelected());

        robot.clickOn("#tglKraljevina");
        assertFalse(tglRepublika.isSelected());
        assertTrue(tglKraljevina.isSelected());
        assertFalse(tglDrzava.isSelected());

        robot.clickOn("#tglDrzava");
        assertFalse(tglRepublika.isSelected());
        assertFalse(tglKraljevina.isSelected());
        assertTrue(tglDrzava.isSelected());
    }

    @Test
    public void testVracanjeRepublike(FxRobot robot) {
        // Upisujemo državu
        robot.clickOn("#fieldNaziv");
        robot.write("Hrvatska");
        robot.clickOn("#choiceGrad");
        robot.clickOn("Pariz");

        robot.clickOn("#tglRepublika");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        Drzava hrvatska = ctrl.getDrzava();
        assertEquals("Republika Hrvatska", hrvatska.getNaziv());
        assertTrue(hrvatska instanceof Republika);
        assertEquals("Pariz", hrvatska.getGlavniGrad().getNaziv());
    }

    @Test
    public void testVracanjeKraljevine(FxRobot robot) {
        // Upisujemo državu
        robot.clickOn("#fieldNaziv");
        robot.write("Kongo");
        robot.clickOn("#choiceGrad");
        robot.clickOn("London");

        robot.clickOn("#tglKraljevina");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        Drzava kongo = ctrl.getDrzava();
        assertEquals("Kraljevina Kongo", kongo.getNaziv());
        assertTrue(kongo instanceof Kraljevina);
        assertEquals("London", kongo.getGlavniGrad().getNaziv());
    }

    @Test
    public void testVracanjeDrzave(FxRobot robot) {
        // Upisujemo državu
        robot.clickOn("#fieldNaziv");
        robot.write("Bosna i Hercegovina");
        robot.clickOn("#choiceGrad");
        robot.clickOn("Graz");

        robot.clickOn("#tglKraljevina");
        robot.clickOn("#tglRepublika");
        robot.clickOn("#tglDrzava");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        Drzava bih = ctrl.getDrzava();
        assertEquals("Bosna i Hercegovina", bih.getNaziv());
        assertFalse(bih instanceof Kraljevina);
        assertFalse(bih instanceof Republika);
        assertEquals("Graz", bih.getGlavniGrad().getNaziv());
    }
}