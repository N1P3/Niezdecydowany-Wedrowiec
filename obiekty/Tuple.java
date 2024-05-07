package niezdecydowanyWedrowiec.obiekty;

import java.util.Objects;

public class Tuple {

    public int getWartoscA() {
        return wartoscA;
    }

    public void setWartoscA(int wartoscA) {
        this.wartoscA = wartoscA;
    }

    public int getWartoscB() {
        return wartoscB;
    }

    public void setWartoscB(int wartoscB) {
        this.wartoscB = wartoscB;
    }
    int wartoscA;
    int wartoscB;

    public Tuple(int wartoscA, int wartoscB) {
        this.wartoscA = wartoscA;
        this.wartoscB = wartoscB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return wartoscA == tuple.wartoscA && wartoscB == tuple.wartoscB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wartoscA, wartoscB);
    }

    @Override
    public String toString() {
        return "(" + wartoscA + ", " + wartoscB + ")";
    }
}
