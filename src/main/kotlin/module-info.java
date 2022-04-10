module by.varyvoda.matvey.automaticgeneration {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens by.varyvoda.matvey.automaticgeneration to javafx.fxml;
    exports by.varyvoda.matvey.automaticgeneration;
}