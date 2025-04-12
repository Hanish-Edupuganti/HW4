module HW1 {
	requires javafx.controls;
	requires java.sql;
	requires jdk.incubator.vector;
	requires org.junit.jupiter.api;
	
	opens application to javafx.graphics, javafx.fxml;
}
