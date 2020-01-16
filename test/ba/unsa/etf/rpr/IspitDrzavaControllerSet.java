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


// Testovi forme drzava.fxml kod izmjene postojeće države
// Pobrinite se najprije da prolaze testovi u klasi IspitDrzavaTest
@ExtendWith(ApplicationExtension.class)
public class IspitDrzavaControllerSet {
    Stage theStage;
    DrzavaController ctrl;

    @Start
    public void start(Stage stage) throws Exception {
        GeografijaDAO dao = GeografijaDAO.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drzava.fxml"));

        Drzava vb = dao.nadjiDrzavu("Velika Britanija");
        Drzava vbk = new Kraljevina(vb.getId(), vb.getNaziv(), vb.getGlavniGrad());

        ctrl = new DrzavaController(vbk, dao.gradovi());
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
    public void testIspravneVrijednosti(FxRobot robot) {
        TextField fieldNaziv = robot.lookup("#fieldNaziv").queryAs(TextField.class);
        assertEquals("Velika Britanija", fieldNaziv.getText());

        RadioButton tglRepublika = robot.lookup("#tglRepublika").queryAs(RadioButton.class);
        RadioButton tglKraljevina = robot.lookup("#tglKraljevina").queryAs(RadioButton.class);
        RadioButton tglDrzava = robot.lookup("#tglDrzava").queryAs(RadioButton.class);
        assertFalse(tglRepublika.isSelected());
        assertTrue(tglKraljevina.isSelected());
        assertFalse(tglDrzava.isSelected());
    }

    @Test
    public void testPromjenaTipa(FxRobot robot) {
        robot.clickOn("#tglDrzava");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        Drzava vb2 = ctrl.getDrzava();
        assertFalse(vb2 instanceof Kraljevina);
        assertFalse(vb2 instanceof Republika);
    }


    @Test
    public void testPromjenaTipa2(FxRobot robot) {
        robot.clickOn("#tglRepublika");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        Drzava vb2 = ctrl.getDrzava();
        assertFalse(vb2 instanceof Kraljevina);
        assertTrue(vb2 instanceof Republika);
    }
}
