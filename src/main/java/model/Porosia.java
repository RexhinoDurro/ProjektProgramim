package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Porosia {

    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty tavolina;
    private final SimpleStringProperty  artikulli;
    private final SimpleIntegerProperty sasia;
    private final SimpleStringProperty  statusi;

    public Porosia(int id, int tavolina, String artikulli, int sasia, String statusi) {
        this.id        = new SimpleIntegerProperty(id);
        this.tavolina  = new SimpleIntegerProperty(tavolina);
        this.artikulli = new SimpleStringProperty(artikulli);
        this.sasia     = new SimpleIntegerProperty(sasia);
        this.statusi   = new SimpleStringProperty(statusi);
    }

    /* ── ID ── */
    public int getId()                        { return id.get(); }
    public void setId(int val)                { id.set(val); }
    public SimpleIntegerProperty idProperty() { return id; }

    /* ── TAVOLINA ── */
    public int getTavolina()                         { return tavolina.get(); }
    public void setTavolina(int val)                 { tavolina.set(val); }
    public SimpleIntegerProperty tavolinaProperty()  { return tavolina; }

    /* ── ARTIKULLI ── */
    public String getArtikulli()                      { return artikulli.get(); }
    public void setArtikulli(String val)              { artikulli.set(val); }
    public SimpleStringProperty artikulliProperty()   { return artikulli; }

    /* ── SASIA ── */
    public int getSasia()                        { return sasia.get(); }
    public void setSasia(int val)                { sasia.set(val); }
    public SimpleIntegerProperty sasiaProperty() { return sasia; }

    /* ── STATUSI ── */
    public String getStatusi()                     { return statusi.get(); }
    public void setStatusi(String val)             { statusi.set(val); }
    public SimpleStringProperty statusiProperty()  { return statusi; }

    @Override
    public String toString() {
        return "Porosia{id=" + getId() + ", tavolina=" + getTavolina()
               + ", artikulli='" + getArtikulli() + "', sasia=" + getSasia()
               + ", statusi='" + getStatusi() + "'}";
    }
}
