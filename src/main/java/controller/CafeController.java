package controller;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Porosia;

import java.sql.*;

public class CafeController {

    private final ObservableList<Porosia> listaPorosi = FXCollections.observableArrayList();

    /* ════════════════════════════════════════
       SELECT – ngarko të gjitha porositë
       ════════════════════════════════════════ */
    public ObservableList<Porosia> loadData() {
        listaPorosi.clear();
        String sql = "SELECT id, tavolina, artikulli, sasia, statusi FROM porosite ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaPorosi.add(new Porosia(
                        rs.getInt("id"),
                        rs.getInt("tavolina"),
                        rs.getString("artikulli"),
                        rs.getInt("sasia"),
                        rs.getString("statusi")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë leximit të të dhënave: " + e.getMessage(), e);
        }
        return listaPorosi;
    }

    /* ════════════════════════════════════════
       INSERT – shto porosi të re
       ════════════════════════════════════════ */
    public void shtoPorosi(int tavolina, String artikulli, int sasia, String statusi) {
        valido(tavolina, artikulli, sasia, statusi);

        String sql = "INSERT INTO porosite (tavolina, artikulli, sasia, statusi) VALUES (?, ?, ?, ?)";

        try (Connection conn          = DBConnection.getConnection();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tavolina);
            pstmt.setString(2, artikulli.trim());
            pstmt.setInt(3, sasia);
            pstmt.setString(4, statusi.trim());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë shtimit të porosisë: " + e.getMessage(), e);
        }
        loadData();
    }

    /* ════════════════════════════════════════
       UPDATE – ndrysho statusin e porosisë
       ════════════════════════════════════════ */
    public void ndryshoStatusPorosie(int id, String statusiRi) {
        if (statusiRi == null || statusiRi.isBlank()) {
            throw new IllegalArgumentException("Statusi i ri nuk mund të jetë bosh.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID e porosisë nuk është e vlefshme.");
        }

        String sql = "UPDATE porosite SET statusi = ? WHERE id = ?";

        try (Connection conn         = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statusiRi.trim());
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("Nuk u gjet asnjë porosi me ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë ndryshimit të statusit: " + e.getMessage(), e);
        }
        loadData();
    }

    /* ════════════════════════════════════════
       DELETE – fshi porosinë
       ════════════════════════════════════════ */
    public void fshiPorosi(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID e porosisë nuk është e vlefshme.");
        }

        String sql = "DELETE FROM porosite WHERE id = ?";

        try (Connection conn         = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("Nuk u gjet asnjë porosi me ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë fshirjes: " + e.getMessage(), e);
        }
        loadData();
    }

    /* ════════════════════════════════════════
       VALIDIM i brendshëm
       ════════════════════════════════════════ */
    private void valido(int tavolina, String artikulli, int sasia, String statusi) {
        if (tavolina <= 0) {
            throw new IllegalArgumentException("Numri i tavolinës duhet të jetë më i madh se 0.");
        }
        if (artikulli == null || artikulli.isBlank()) {
            throw new IllegalArgumentException("Artikulli nuk mund të jetë bosh.");
        }
        if (sasia <= 0) {
            throw new IllegalArgumentException("Sasia duhet të jetë të paktën 1.");
        }
        if (statusi == null || statusi.isBlank()) {
            throw new IllegalArgumentException("Statusi nuk mund të jetë bosh.");
        }
    }

    public ObservableList<Porosia> getListaPorosi() {
        return listaPorosi;
    }
}
