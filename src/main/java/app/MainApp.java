package app;

import controller.CafeController;
import database.DBConnection;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Porosia;

public class MainApp extends Application {

    /* ── Kontrolleri i biznesit ── */
    private final CafeController controller = new CafeController();

    /* ── Fusha të inputit ── */
    private TextField          tfTavolina;
    private ComboBox<String>   cbArtikulli;
    private ComboBox<String>   cbStatusi;
    private Spinner<Integer>   spSasia;
    private TableView<Porosia> tableView;

    /* ID e rreshtit të zgjedhur (0 = asnjë) */
    private int selectedId = 0;

    /* ════════════════════════════════════════════════════════════════
       ENTRY POINT
       ════════════════════════════════════════════════════════════════ */
    public static void main(String[] args) {
        launch(args);
    }

    /* ════════════════════════════════════════════════════════════════
       start() – ndërtimi i skenës
       ════════════════════════════════════════════════════════════════ */
    @Override
    public void start(Stage primaryStage) {

        // 1. Krijo / verifiko tabelën në SQLite
        DBConnection.krijoTabelen();

        // 2. Krijo komponentët
        VBox leftPanel  = buildLeftPanel();
        VBox rightPanel = buildRightPanel();

        // 3. Vendos tabela të dhënat e para
        tableView.setItems(controller.loadData());

        // 4. Layout kryesor
        HBox root = new HBox(20, leftPanel, rightPanel);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f4f8;");
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        // 5. Skenë dhe dritare
        Scene scene = new Scene(root, 950, 580);
        primaryStage.setTitle("☕  Café Order Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    /* ════════════════════════════════════════════════════════════════
       PANELI I MAJTË – input + butona
       ════════════════════════════════════════════════════════════════ */
    private VBox buildLeftPanel() {

        /* ── Titulli ── */
        Label title = new Label("Porosi e Re");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));

        /* ── Tavolina ── */
        Label lbTavolina = styledLabel("Tavolina Nr:");
        tfTavolina = new TextField();
        tfTavolina.setPromptText("p.sh. 5");
        tfTavolina.setMaxWidth(Double.MAX_VALUE);

        /* ── Artikulli ── */
        Label lbArtikulli = styledLabel("Artikulli:");
        cbArtikulli = new ComboBox<>(FXCollections.observableArrayList(
                "Kafe Espresso", "Kafe Americano", "Kafe Macchiato",
                "Çaj Jeshil", "Çaj Kamomil", "Çokollatë e Nxehtë",
                "Lëng Portokalli", "Ujë Mineral", "Kapuçino", "Latte"
        ));
        cbArtikulli.setPromptText("Zgjidh artikullin...");
        cbArtikulli.setMaxWidth(Double.MAX_VALUE);

        /* ── Sasia ── */
        Label lbSasia = styledLabel("Sasia:");
        SpinnerValueFactory<Integer> svf =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1);
        spSasia = new Spinner<>(svf);
        spSasia.setEditable(true);
        spSasia.setMaxWidth(Double.MAX_VALUE);

        /* ── Statusi ── */
        Label lbStatusi = styledLabel("Statusi:");
        cbStatusi = new ComboBox<>(FXCollections.observableArrayList(
                "Në pritje", "Duke u përgatitur", "Gati", "Dorëzuar", "Anuluar"
        ));
        cbStatusi.setValue("Në pritje");
        cbStatusi.setMaxWidth(Double.MAX_VALUE);

        /* ── Butoni SHTO ── */
        Button btnShto = styledButton("＋  Shto Porosi", "#27ae60", "#ffffff");
        btnShto.setMaxWidth(Double.MAX_VALUE);
        btnShto.setOnAction(e -> handleShto());

        /* ── Butoni NDRYSHO ── */
        Button btnNdrysho = styledButton("✎  Ndrysho Statusin", "#2980b9", "#ffffff");
        btnNdrysho.setMaxWidth(Double.MAX_VALUE);
        btnNdrysho.setOnAction(e -> handleNdrysho());

        /* ── Butoni FSHI ── */
        Button btnFshi = styledButton("✕  Fshi Porosinë", "#c0392b", "#ffffff");
        btnFshi.setMaxWidth(Double.MAX_VALUE);
        btnFshi.setOnAction(e -> handleFshi());

        /* ── Butoni PASTRO ── */
        Button btnPastro = styledButton("↺  Pastro Fushat", "#7f8c8d", "#ffffff");
        btnPastro.setMaxWidth(Double.MAX_VALUE);
        btnPastro.setOnAction(e -> pastroPushat());

        /* ── Ndërtimi i panelit ── */
        VBox panel = new VBox(10,
                title,
                new Separator(),
                lbTavolina, tfTavolina,
                lbArtikulli, cbArtikulli,
                lbSasia,     spSasia,
                lbStatusi,   cbStatusi,
                new Separator(),
                btnShto,
                btnNdrysho,
                btnFshi,
                btnPastro
        );
        panel.setPadding(new Insets(15));
        panel.setPrefWidth(240);
        panel.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; "
                     + "-fx-effect: dropshadow(gaussian, #c8d6e5, 8, 0, 0, 2);");
        return panel;
    }

    /* ════════════════════════════════════════════════════════════════
       PANELI I DJATHTË – TableView
       ════════════════════════════════════════════════════════════════ */
    @SuppressWarnings("unchecked")
    private VBox buildRightPanel() {

        Label title = new Label("Lista e Porosive");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));

        /* ── Kolonat ── */
        TableColumn<Porosia, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);
        colId.setStyle("-fx-alignment: CENTER;");

        TableColumn<Porosia, Integer> colTavolina = new TableColumn<>("Tavolina");
        colTavolina.setCellValueFactory(new PropertyValueFactory<>("tavolina"));
        colTavolina.setPrefWidth(80);
        colTavolina.setStyle("-fx-alignment: CENTER;");

        TableColumn<Porosia, String> colArtikulli = new TableColumn<>("Artikulli");
        colArtikulli.setCellValueFactory(new PropertyValueFactory<>("artikulli"));
        colArtikulli.setPrefWidth(160);

        TableColumn<Porosia, Integer> colSasia = new TableColumn<>("Sasia");
        colSasia.setCellValueFactory(new PropertyValueFactory<>("sasia"));
        colSasia.setPrefWidth(70);
        colSasia.setStyle("-fx-alignment: CENTER;");

        TableColumn<Porosia, String> colStatusi = new TableColumn<>("Statusi");
        colStatusi.setCellValueFactory(new PropertyValueFactory<>("statusi"));
        colStatusi.setPrefWidth(150);

        // Ngjyros qelizat e statusit
        colStatusi.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Gati"                -> setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                        case "Duke u përgatitur"   -> setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                        case "Anuluar"             -> setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
                        case "Dorëzuar"            -> setStyle("-fx-text-fill: #8e44ad; -fx-font-weight: bold;");
                        default                    -> setStyle("-fx-text-fill: #2c3e50;");
                    }
                }
            }
        });

        /* ── TableView ── */
        tableView = new TableView<>();
        tableView.getColumns().addAll(colId, colTavolina, colArtikulli, colSasia, colStatusi);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("Nuk ka porosi ende. Shto porosinë e parë!"));

        // Kur zgjidhet rreshti → mbush fushat
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) mbushFushat(newVal);
                }
        );

        VBox.setVgrow(tableView, Priority.ALWAYS);

        VBox panel = new VBox(10, title, new Separator(), tableView);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; "
                     + "-fx-effect: dropshadow(gaussian, #c8d6e5, 8, 0, 0, 2);");
        return panel;
    }

    /* ════════════════════════════════════════════════════════════════
       VEPRIMET E BUTONAVE
       ════════════════════════════════════════════════════════════════ */
    private void handleShto() {
        try {
            int    tavolina  = parseTavolina();
            String artikulli = cbArtikulli.getValue();
            int    sasia     = spSasia.getValue();
            String statusi   = cbStatusi.getValue();

            controller.shtoPorosi(tavolina, artikulli, sasia, statusi);
            pastroPushat();
            showInfo("Porosi u shtua me sukses!", "✅ Sukses");

        } catch (IllegalArgumentException e) {
            showError("Gabim në Input", e.getMessage());
        } catch (RuntimeException e) {
            showError("Gabim Databaze", e.getMessage());
        }
    }

    private void handleNdrysho() {
        if (selectedId == 0) {
            showError("Asnjë zgjedhje", "Ju lutem zgjidhni një porosi nga tabela.");
            return;
        }
        try {
            String statusiRi = cbStatusi.getValue();
            controller.ndryshoStatusPorosie(selectedId, statusiRi);
            pastroPushat();
            showInfo("Statusi u ndryshua me sukses!", "✅ Sukses");

        } catch (IllegalArgumentException e) {
            showError("Gabim në Input", e.getMessage());
        } catch (RuntimeException e) {
            showError("Gabim Databaze", e.getMessage());
        }
    }

    private void handleFshi() {
        if (selectedId == 0) {
            showError("Asnjë zgjedhje", "Ju lutem zgjidhni një porosi nga tabela.");
            return;
        }

        // Konfirmim para fshirjes
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmo Fshirjen");
        confirm.setHeaderText("A jeni të sigurt?");
        confirm.setContentText("Porosia me ID " + selectedId + " do të fshihet përgjithmonë.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    controller.fshiPorosi(selectedId);
                    pastroPushat();
                    showInfo("Porosia u fshi me sukses!", "✅ Sukses");
                } catch (RuntimeException e) {
                    showError("Gabim Databaze", e.getMessage());
                }
            }
        });
    }

    /* ════════════════════════════════════════════════════════════════
       HELPERA
       ════════════════════════════════════════════════════════════════ */
    private void mbushFushat(Porosia p) {
        selectedId = p.getId();
        tfTavolina.setText(String.valueOf(p.getTavolina()));
        cbArtikulli.setValue(p.getArtikulli());
        spSasia.getValueFactory().setValue(p.getSasia());
        cbStatusi.setValue(p.getStatusi());
    }

    private void pastroPushat() {
        selectedId = 0;
        tfTavolina.clear();
        cbArtikulli.setValue(null);
        spSasia.getValueFactory().setValue(1);
        cbStatusi.setValue("Në pritje");
        tableView.getSelectionModel().clearSelection();
    }

    private int parseTavolina() {
        String text = tfTavolina.getText().trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Numri i tavolinës nuk mund të jetë bosh.");
        }
        try {
            int val = Integer.parseInt(text);
            if (val <= 0) throw new NumberFormatException();
            return val;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Numri i tavolinës duhet të jetë një numër pozitiv.");
        }
    }

    private Label styledLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        lbl.setTextFill(Color.web("#34495e"));
        return lbl;
    }

    private Button styledButton(String text, String bgColor, String textColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        String base = String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; "
              + "-fx-background-radius: 6; -fx-padding: 8 12;", bgColor, textColor);
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
        btn.setOnMouseExited(e  -> btn.setOpacity(1.0));
        return btn;
    }

    private void showInfo(String message, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Njoftim");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gabim");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
