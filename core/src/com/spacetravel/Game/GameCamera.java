package com.spacetravel.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameCamera {
	
	final private OrthographicCamera cam;
	public final StretchViewport viewport;
	
	public GameCamera (int width, int height) {
		cam = new OrthographicCamera();
		viewport = new StretchViewport(width, height, cam);
		viewport.apply();
		cam.position.set((float)width / 2, (float)height / 2, 0);
		cam.update();
	}
	
	public Matrix4 combined() {
		return cam.combined;
	}

	public Vector2 getInputInGameWorld () {
		Vector3 inputScreen = new Vector3(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 0);
		Vector3 unprojected = cam.unproject(inputScreen);
		return new Vector2(unprojected.x, unprojected.y);
	}
	
}
