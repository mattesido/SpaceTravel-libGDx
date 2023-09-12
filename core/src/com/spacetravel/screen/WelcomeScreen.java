package com.spacetravel.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.spacetravel.SpaceTravel;


public class WelcomeScreen implements Screen {

    final SpaceTravel game;
    Stage stage;
    OrthographicCamera camera;

    TextButton startButton;
    Skin skin;
    Texture backgroundScreen;
    Sprite backgroundSprite;

    SelectBox<String> selectBox;

    TextButton exitButton;
    public WelcomeScreen(final SpaceTravel game, final String selected) {
        this.game = game;
        this.skin = new Skin(Gdx.files.internal(game.stylePath));
        this.stage = new Stage();
        this.camera = new OrthographicCamera(1920, 1080);
        camera.setToOrtho(false, 1920, 1080);
        Gdx.input.setInputProcessor(stage);
switch(selected){
    case "NASA":
    backgroundScreen = new Texture(Gdx.files.internal("screen/moon.jpg"));
    break;
    case "USSR":
        backgroundScreen = new Texture(Gdx.files.internal("space agencies/ussr2.jpg"));
        break;
    case "CNSP":
        backgroundScreen = new Texture(Gdx.files.internal("space agencies/china2.jpeg"));
        break;
    case "EASA":
        backgroundScreen = new Texture(Gdx.files.internal("space agencies/easa1.jpg"));
        break;
    case "SPACEX":
        backgroundScreen = new Texture(Gdx.files.internal("space agencies/spacex3.jpeg"));
        break;
}

        backgroundSprite = new Sprite(backgroundScreen);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        selectBox = new SelectBox<>(game.selectBoxStyle);
        selectBox.setWidth(600);
        Array<String> classArray = new Array<>();
        classArray.addAll("Mercury", "Venus","Moon","Mars","Jupiter","Saturn","Uranus","Neptune");
        selectBox.setItems(classArray);
        selectBox.getList().getStyle().font = game.bigFont;
        selectBox.setPosition((float) (Gdx.graphics.getWidth()* 2/3), (float) (Gdx.graphics.getHeight() * 8.6/10));

        startButton = new TextButton(" START ", game.textButtonStyle);
        startButton.setPosition((float) (Gdx.graphics.getWidth() / 2.7 ), (float) (Gdx.graphics.getHeight() / 4.5));

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlanetScreen(game, selectBox.getSelected(),selected));
            }
        });

        exitButton = new TextButton("Exit", game.textButtonStyle);
        exitButton.setPosition((float) (Gdx.graphics.getWidth()/18), (float) (Gdx.graphics.getHeight() * 8.4/10));
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.exit(0);
            }
        });

        stage.addActor(startButton);
        stage.addActor(selectBox);
        stage.addActor(exitButton);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        backgroundSprite.draw(game.batch);

        game.batch.end();

        stage.draw();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        camera.update();

        game.batch.begin();

        backgroundSprite.draw(game.batch, 1f);

        game.batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundScreen.dispose();

    }
}
