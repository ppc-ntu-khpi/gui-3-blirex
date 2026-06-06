package com.mybank.gui;

import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.SavingsAccount;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXDemo extends Application {

    private Text title;
    private Text details;
    private ComboBox clients;

    @Override
    public void start(Stage primaryStage) {

        // ✅ ВАЖНО: загрузка клиентов
        Bank.load();

        BorderPane border = new BorderPane();

        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        addStackPane(hbox);

        Scene scene = new Scene(border, 300, 250);

        primaryStage.setTitle("MyBank Clients");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public VBox addVBox() {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        title = new Text("Client Name");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        vbox.getChildren().add(title);

        Line separator = new Line(10, 10, 280, 10);
        vbox.getChildren().add(separator);

        details = new Text("Account:\t\t#0\nAcc Type:\tChecking\nBalance:\t\t$0000");
        details.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        vbox.getChildren().add(details);

        return vbox;
    }

    public HBox addHBox() {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        ObservableList<String> items = FXCollections.observableArrayList();

        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            items.add(
                Bank.getCustomer(i).getLastName()
                + ", "
                + Bank.getCustomer(i).getFirstName()
            );
        }

        clients = new ComboBox(items);
        clients.setPrefSize(150, 20);
        clients.setPromptText("Click to choose...");

        Button buttonShow = new Button("Show");
        buttonShow.setPrefSize(100, 20);

        Button buttonReport = new Button("Report"); // ✅ ДОБАВИЛИ

        buttonShow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    if (clients.getValue() == null) return;

                    int custNo = clients.getItems().indexOf(clients.getValue());
                    int accNo = 0;

                    title.setText(clients.getValue().toString());

                    String accType =
                            Bank.getCustomer(custNo).getAccount(accNo)
                                    instanceof CheckingAccount
                                    ? "Checking"
                                    : "Savings";

                    details.setText(
                        "Account:\t\t#" + accNo +
                        "\nAcc Type:\t" + accType +
                        "\nBalance:\t\t$" +
                        Bank.getCustomer(custNo)
                            .getAccount(accNo)
                            .getBalance()
                    );

                } catch (Exception e) {

                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error getting client...");
                    alert.setHeaderText(null);

                    String msg = e.getMessage() != null
                            ? e.getMessage()
                            : "You need to choose a client first!";

                    alert.setContentText("Error details: " + msg);
                    alert.showAndWait();
                }
            }
        });

        // ✅ REPORT (5 БАЛЛОВ)
        buttonReport.setOnAction(e -> {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {

                var c = Bank.getCustomer(i);

                sb.append(c.getLastName())
                  .append(", ")
                  .append(c.getFirstName())
                  .append("\n");

                for (int j = 0; j < 10; j++) {
                    try {
                        var a = c.getAccount(j);

                        String type = (a instanceof CheckingAccount)
                                ? "Checking"
                                : "Savings";

                        sb.append("   ")
                          .append(type)
                          .append(" : $")
                          .append(a.getBalance())
                          .append("\n");

                    } catch (Exception ex) {
                        break;
                    }
                }

                sb.append("\n");
            }

            details.setText(sb.toString());
        });

        hbox.getChildren().addAll(clients, buttonShow, buttonReport);

        return hbox;
    }

    public void addStackPane(HBox hb) {

        StackPane stack = new StackPane();

        Rectangle helpIcon = new Rectangle(30.0, 25.0);

        helpIcon.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop[]{
                        new Stop(0, Color.web("#4977A3")),
                        new Stop(0.5, Color.web("#B0C6DA")),
                        new Stop(1, Color.web("#9CB6CF"))
                }
        ));

        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        helpIcon.setOnMouseClicked(t -> ShowAboutInfo());
        helpText.setOnMouseClicked(t -> ShowAboutInfo());

        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);

        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0));

        hb.getChildren().add(stack);

        HBox.setHgrow(stack, Priority.ALWAYS);
    }

    private void ShowAboutInfo() {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);

        alert.setContentText(
            "Just a simple JavaFX demo.\n" +
            "Copyright © 2019 Zubarev 'Bashmak' Ivan"
        );

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}