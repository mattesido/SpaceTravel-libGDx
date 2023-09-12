package com.spacetravel.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.spacetravel.SpaceTravel;


public class VictoryScreen implements Screen {

    final SpaceTravel game;
    Stage stage;
    OrthographicCamera camera;

    Skin skin;
    Texture victoryScreen;
    Sprite victorySprite;
    TextButton restartButton;


    public VictoryScreen(final SpaceTravel game, final String selected) {
        this.game = game;
        this.stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin(Gdx.files.internal(game.stylePath));
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        switch(selected){
            case "NASA":
                victoryScreen = new Texture(Gdx.files.internal("screen/landed.jpeg"));
                break;
            case "USSR":
                victoryScreen = new Texture(Gdx.files.internal("space agencies/ussr3.jpg"));
                break;
            case "CNSP":
                victoryScreen = new Texture(Gdx.files.internal("space agencies/china3.jpeg"));
                break;
            case "EASA":
                victoryScreen = new Texture(Gdx.files.internal("space agencies/easa2.jpeg"));
                break;
            case "SPACEX":
                victoryScreen = new Texture(Gdx.files.internal("space agencies/spacex2.jpeg"));
                break;
        }

        victorySprite = new Sprite(victoryScreen);
        victorySprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        restartButton = new TextButton("Restart Game", game.textButtonStyle);
        restartButton.setPosition((float) (Gdx.graphics.getWidth() / 3 ), (float) (Gdx.graphics.getHeight() / 4.5));
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


                game.setScreen(new WelcomeScreen(game,  selected));
            }
        });
        stage.addActor(restartButton);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        victorySprite.draw(game.batch);

        game.batch.end();

        stage.draw();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        camera.update();

        game.batch.begin();

        victorySprite.draw(game.batch, 1f);

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
        victoryScreen.dispose();
    }
}