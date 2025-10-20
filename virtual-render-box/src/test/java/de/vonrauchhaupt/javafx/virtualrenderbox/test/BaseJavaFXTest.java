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
    AtomicReference<Throwable> caughtException = new AtomicReference<>();

    void startTest() throws Throwable {
        PlatformImpl.startup(this::doTestStartup);
        latch.await();
        if (caughtException.get() != null)
            throw caughtException.get();
    }

    private void doTestStartup() {
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                stage.setOnCloseRequest(x -> latch.countDown());
                stage.setTitle("Test " + this.getClass().getName());
                BorderPane rootPane = new BorderPane();
                T testableNode = createTestNode();
                rootPane.setCenter(new ScrollPane(testableNode));
                stage.setScene(new Scene(rootPane, 700, 700));
                stage.show();
                Platform.runLater(() -> {
                    try {
                        doTestOnTestNode(testableNode);
                        stopTest();
                    } catch (Throwable e) {
                        caughtException.set(e);
                        stopTest();
                    }
                });
            } catch (Throwable e) {
                caughtException.set(e);
            }
        });
    }

    abstract void doTestOnTestNode(T testableNode);

    void stopTest() {
        if (!"true".equalsIgnoreCase(System.getenv("TEST_STAY_OPEN")))
            latch.countDown();
    }

    @NotNull
    abstract T createTestNode() throws Exception;
}
