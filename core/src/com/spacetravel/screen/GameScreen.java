package com.spacetravel.screen;



import com.spacetravel.Game.Asteroid;
import com.spacetravel.Game.Bullet;
import com.spacetravel.Game.CollisionRect;
import com.spacetravel.Game.Explosion;
import com.spacetravel.SpaceTravel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {

	public static final float SPEED = 300;
	public static final float SHIP_ANIMATION_SPEED = 0.5f;
	public static final int SHIP_WIDTH_PIXEL = 17;
	public static final int SHIP_HEIGHT_PIXEL = 32;
	public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
	public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;

	public static final float ROLL_TIMER_SWITCH_TIME = 0.25f;
	public static final float SHOOT_WAIT_TIME = 0.3f;

	public static final float MIN_ASTEROID_SPAWN_TIME = 0.05f;
	public static final float MAX_ASTEROID_SPAWN_TIME = 0.1f;

	Animation[] rolls;

	float x;
	float y;
	int roll;
	float rollTimer;
	float stateTime;
	float shootTimer;
	float asteroidSpawnTimer;

	Random random;

	SpaceTravel game;

	ArrayList<Bullet> bullets;
	ArrayList<Asteroid> asteroids;
	ArrayList<Explosion> explosions;

	Texture blank;
	String selected;

	BitmapFont scoreFont;

	CollisionRect playerRect;

	float health = 1;//0 = dead, 1 = full health

	int score;

	boolean showControls = true;


	public GameScreen(SpaceTravel game,final String selected) {
		this.selected = selected;
		this.game = game;
		y = 15;
		bullets = new ArrayList<>();
		asteroids = new ArrayList<>();
		scoreFont = new BitmapFont(Gdx.files.internal("game/fonts/score.fnt"));

		playerRect = new CollisionRect(0, 0, SHIP_WIDTH, SHIP_HEIGHT);

		blank = new Texture("game/blank.png");


		score = 0;

		random = new Random();
		asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

		shootTimer = 0;

		roll = 2;
		rollTimer = 0;
		rolls = new Animation[5];

		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("game/ship.png"),
				SHIP_WIDTH_PIXEL,
				SHIP_HEIGHT_PIXEL);

		rolls[0] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]);//All left
		rolls[1] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]);
		rolls[2] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]);//No tilt
		rolls[3] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]);
		rolls[4] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]);//Right

		game.scrollingBackground.setSpeedFixed(false);
		explosions = new ArrayList<>();
		x =(float)( (SpaceTravel.WIDTH / 2) - (SHIP_WIDTH / 2));


	}

	@Override
	public void show () {

	}

	@Override
	public void render (float delta) {
		//Shooting code
		shootTimer += delta;
		if ((isRight() || isLeft()) && shootTimer >= SHOOT_WAIT_TIME) {
			shootTimer = 0;
			
			//Stop showing controls when a shot has been shot
			showControls = false;
			
			int offset = 4;
			if (roll == 1 || roll == 3)//Slightly tilted
				offset = 8;
			
			if (roll == 0 || roll == 4)//Fully tilted
				offset = 16;
			
			bullets.add(new Bullet(x + offset));
			bullets.add(new Bullet(x + SHIP_WIDTH - offset));
		}
		
		//Asteroid spawn code
		asteroidSpawnTimer -= delta;
		if (asteroidSpawnTimer <= 0) {
			asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
			asteroids.add(new Asteroid(random.nextInt(SpaceTravel.WIDTH - Asteroid.WIDTH)));
		}
		
		//Update asteroids
		ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
		for (Asteroid asteroid : asteroids) {
			asteroid.update(delta);
			if (asteroid.remove)
				asteroidsToRemove.add(asteroid);
		}
		
		//Update bullets
		ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
		for (Bullet bullet : bullets) {
			bullet.update(delta);
			if (bullet.remove)
				bulletsToRemove.add(bullet);
		}
		
		//Update explosions
		ArrayList<Explosion> explosionsToRemove = new ArrayList<>();
		for (Explosion explosion : explosions) {
			explosion.update(delta);
			if (explosion.remove)
				explosionsToRemove.add(explosion);
		}
		explosions.removeAll(explosionsToRemove);
		
		//Movement code
		if (isLeft()) {//Left
			x -= SPEED * Gdx.graphics.getDeltaTime();
			
			if (x < 0)
				x = 0;
			
			//Update roll if button just clicked
			if (isJustLeft() && !isRight() && roll > 0) {
				rollTimer = 0;
				roll--;
			}
			
			//Update roll
			rollTimer -= Gdx.graphics.getDeltaTime();
			if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
				rollTimer -= ROLL_TIMER_SWITCH_TIME;
				roll--;
			}
		} else {
			if (roll < 2) {
				//Update roll to make it go back to center
				rollTimer += Gdx.graphics.getDeltaTime();
				if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
					rollTimer -= ROLL_TIMER_SWITCH_TIME;
					roll++;
				}
			}
		}
		
		if (isRight()) {//Right
			x += SPEED * Gdx.graphics.getDeltaTime();
			
			if (x + SHIP_WIDTH > SpaceTravel.WIDTH)
				x = SpaceTravel.WIDTH - SHIP_WIDTH;
			
			//Update roll if button just clicked
			if (isJustRight() && !isLeft() && roll > 0) {
				rollTimer = 0;
				roll--;
			}
			
			//Update roll
			rollTimer += Gdx.graphics.getDeltaTime();
			if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
				rollTimer -= ROLL_TIMER_SWITCH_TIME;
				roll++;
			}
		} else {
			if (roll > 2) {
				//Update roll
				rollTimer -= Gdx.graphics.getDeltaTime();
				if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
					rollTimer -= ROLL_TIMER_SWITCH_TIME;
					roll--;
				}
			}
		}
		
		//After player moves, update collision rect
		playerRect.move(x, y);
		
		//After all updates, check for collisions
		for (Bullet bullet : bullets) {
			for (Asteroid asteroid : asteroids) {
				if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())) {
					bulletsToRemove.add(bullet);
					asteroidsToRemove.add(asteroid);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
					score += 100;
				}
				if(score == 6000){
					game.setScreen(new VictoryScreen(game,selected));
				}
			}
		}
		bullets.removeAll(bulletsToRemove);
		
		for (Asteroid asteroid : asteroids) {
			if (asteroid.getCollisionRect().collidesWith(playerRect)) {
				asteroidsToRemove.add(asteroid);
				health -= 0.1;
				
				//If health is depleted,
				if (health <= 0) {
					this.dispose();
					game.setScreen(new PlayScreen(game,selected));

				}
			}
		}
		asteroids.removeAll(asteroidsToRemove);
		
		stateTime += delta;

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		
		game.scrollingBackground.updateAndRender(delta, (SpriteBatch) game.batch);
		
		GlyphLayout scoreLayout = new GlyphLayout(scoreFont, " " + score);
		scoreFont.draw(game.batch,
				scoreLayout,
				(float) SpaceTravel.WIDTH / 2 - scoreLayout.width / 2,
				SpaceTravel.HEIGHT - scoreLayout.height - 10);
		
		for (Bullet bullet : bullets) {
			bullet.render((SpriteBatch) game.batch);
		}
		
		for (Asteroid asteroid : asteroids) {
			asteroid.render((SpriteBatch) game.batch);
		}
		
		for (Explosion explosion : explosions) {
			explosion.render((SpriteBatch) game.batch);
		}
		
		//Draw health
		if (health > 0.6f)
			game.batch.setColor(Color.GREEN);
		else if (health > 0.2f)
			game.batch.setColor(Color.ORANGE);
		else
			game.batch.setColor(Color.RED);
		
		game.batch.draw(blank, 0, 0, SpaceTravel.WIDTH * health, 5);
		game.batch.setColor(Color.WHITE);

		game.batch.draw((TextureRegion) rolls[roll].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);
		
		//Draw controls instructions

		
		game.batch.end();
	}
	
	private boolean isRight () {
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			return true;
		if (Gdx.input.isTouched())
			return game.cam.getInputInGameWorld().x >= (float) SpaceTravel.WIDTH / 2;
		return false;
	}
	
	private boolean isLeft () {
		return Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isTouched() && game.cam.getInputInGameWorld().x < (float)SpaceTravel.WIDTH / 2);
	}
	
	private boolean isJustRight () {
		return Gdx.input.isKeyJustPressed(Keys.RIGHT) || (Gdx.input.justTouched() && game.cam.getInputInGameWorld().x >= (float)SpaceTravel.WIDTH / 2);
	}
	
	private boolean isJustLeft () {
		return Gdx.input.isKeyJustPressed(Keys.LEFT) || (Gdx.input.justTouched() && game.cam.getInputInGameWorld().x < (float)SpaceTravel.WIDTH / 2);
	}
	
	@Override
	public void resize (int width, int height) {
		
	}

	@Override
	public void pause () {
		
	}

	@Override
	public void resume () {
		
	}

	@Override
	public void hide () {
		
	}

	@Override
	public void dispose () {
		
	}

}
