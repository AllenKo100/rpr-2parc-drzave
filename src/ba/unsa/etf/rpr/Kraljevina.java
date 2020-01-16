package ba.unsa.etf.rpr;

public class Kraljevina extends Drzava {
    public Kraljevina(int id, String naziv, Grad glavniGrad) {
        super(id, naziv, glavniGrad);
    }

    public Kraljevina() {
    }

    @Override
    public String getNaziv() { return "Kraljevina " + super.getNaziv(); }

    @Override
    public String toString() { return getNaziv(); }
}
