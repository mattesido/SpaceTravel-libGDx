package com.spacetravel.Game;

import com.spacetravel.SpaceTravel;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Bullet {
	
	public static final int SPEED = 500;
	public static final int DEFAULT_Y = 40;
	public static final int WIDTH = 12;
	public static final int HEIGHT = 12;
	private static Texture texture;
	
	float x, y;
	CollisionRect rect;
	public boolean remove = false;
	
	public Bullet (float x) {
		this.x = x;
		this.y = DEFAULT_Y;
		this.rect = new CollisionRect(x, y, WIDTH, HEIGHT);
		
		if (texture == null)
			texture = new Texture("game/bullet.png");
	}
	
	public void update (float deltaTime) {
		y += SPEED * deltaTime;
		if (y > SpaceTravel.HEIGHT)
			remove = true;
		
		rect.move(x, y);
	}
	
	public void render (SpriteBatch batch) {
		batch.draw(texture, x, y);
	}
	
	public CollisionRect getCollisionRect () {
		return rect;
	}
	
}
