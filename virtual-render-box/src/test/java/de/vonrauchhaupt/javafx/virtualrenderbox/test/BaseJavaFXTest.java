package de.vonrauchhaupt.javafx.virtualrenderbox.test;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

abstract class BaseJavaFXTest<T extends Node> {

    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Exception> caughtException = new AtomicReference<>();

    void startTest() throws Exception {
        PlatformImpl.startup(this::doTestStartup);
        latch.await();
        if (caughtException.get() != null)
            throw caughtException.get();
    }

    private void doTestStartup() {
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                stage.setOnCloseRequest(x -> stopTest());
                stage.setTitle("Test " + this.getClass().getName());
                BorderPane rootPane = new BorderPane();
                T testableNode = createTestNode();
                rootPane.setCenter(new ScrollPane(testableNode));
                stage.setScene(new Scene(rootPane, 700, 700));
                stage.show();
                Platform.runLater(() -> {
                    doTestOnTestNode(testableNode);
                    if (!"true".equalsIgnoreCase(System.getProperty("TEST_STAY_OPEN")))
                        stopTest();
                });
            } catch (Exception e) {
                caughtException.set(e);
            }
        });
    }

    abstract void doTestOnTestNode(T testableNode);

    void stopTest() {
        latch.countDown();
    }

    @NotNull
    abstract T createTestNode() throws Exception;
}
