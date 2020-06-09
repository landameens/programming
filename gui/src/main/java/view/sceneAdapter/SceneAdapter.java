package view.sceneAdapter;

import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.LogManager;
import router.screen.Screen;
import router.screen.ScreenContext;
import router.screen.ScreenMemento;
import view.fxController.FXController;

public final class SceneAdapter implements Screen {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(SceneAdapter.class);


    private final Stage stage;
    private final Scene scene;
    private final FXController fxController;


    public SceneAdapter(Stage stage, Scene scene, FXController fxController) {
        this.stage = stage;
        this.scene = scene;
        this.fxController = fxController;
    }


    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void onStart(ScreenContext screenContext) {
        LOG_MANAGER.debug("OnStart of scene: " + scene.toString());
        stage.setScene(scene);
        fxController.setSceneAdapter(this);
        fxController.setScreenContext(screenContext);
        fxController.onStart();
    }

    @Override
    public void onActive(ScreenMemento screenMemento, ScreenContext screenContext) {
        throw new RuntimeException("Unsupported.");
    }

    @Override
    public ScreenMemento getState() {
        return null;
    }
}
