package com.asifhashmi.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Circle birdcircle=new Circle();
    Texture gameover;
    //ShapeRenderer shapeRenderer;
    Texture birds[];
    Rectangle rectangleTop[];
    Rectangle rectangleBot[];
    int flapState = 0;
    float posBirdY = 0;
    float velocity = 0;
    int gameState = 0;
    BitmapFont font;
    int score=0;
    int scoringTube=0;
    float gravity = 2;
    Texture topTube, bottomTube;
    float gap = 300;
    int numOfTubes = 4;
    float maxTubeOffset;
    Random random;
    float[] tubeOffset = new float[numOfTubes];
    float tubeVelocity = 4;
    float[] tubeX = new float[numOfTubes];
    float distanceBwTubes;


    @Override
    public void create() {


        font=new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameover=new Texture("gameover.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
       // shapeRenderer=new ShapeRenderer();
        posBirdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        random = new Random();
        distanceBwTubes = Gdx.graphics.getWidth()*.75f;
      rectangleBot=new Rectangle[numOfTubes];
rectangleTop=new Rectangle[numOfTubes];

startGame();
    }

    public void startGame(){

        posBirdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;
        for (int i = 0; i < numOfTubes; i++) {

            tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() +Gdx.graphics.getWidth()+ i * distanceBwTubes;

            rectangleTop[i]=new Rectangle();
            rectangleBot[i]=new Rectangle();

        }
    }

    @Override
    public void render() {

        batch = new SpriteBatch();
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
                score++;
                if(scoringTube<numOfTubes-1){
                    scoringTube++;
                    Gdx.app.log("score",String.valueOf(score));
                }else {
                    scoringTube=0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity = -30;

            }
            for (int i = 0; i < numOfTubes; i++) {
                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += numOfTubes * distanceBwTubes;
                } else {
                    tubeX[i] -= tubeVelocity;
                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - bottomTube.getHeight() - gap / 2 + tubeOffset[i]);

                rectangleTop[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
                rectangleBot[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - bottomTube.getHeight() - gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());

            }
            if (posBirdY > 0) {
                velocity += gravity;
                posBirdY -= velocity;
            }else{
                gameState=2;
            }

            if (flapState == 0) {
                flapState = 1;
            } else {
                flapState = 0;
            }

        } else if(gameState==0){
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }else if(gameState==2){
            batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
           posBirdY=0;
            font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()/2-50,Gdx.graphics.getHeight()/2-gameover.getHeight()/2-150);
            if (Gdx.input.justTouched()) {
                gameState = 1;
                startGame();
                score=0;
                scoringTube=0;
                velocity=0;
            }
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, posBirdY);
        font.draw(batch,String.valueOf(score),100,200);
        batch.end();
      //  shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      //  shapeRenderer.setColor(Color.RED);
        for (int i = 0; i < numOfTubes; i++) {
         //   shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
       //     shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - bottomTube.getHeight() - gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());

            if(Intersector.overlaps(birdcircle,rectangleBot[i]) || Intersector.overlaps(birdcircle,rectangleTop[i])){
                Gdx.app.log("Colllison", "detected");
                gameState=2;
            }
        }
        birdcircle.set(Gdx.graphics.getWidth() / 2,posBirdY+birds[flapState].getHeight() / 2,birds[flapState].getWidth() / 2);


     //   shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);
     //   shapeRenderer.end();

    }

    @Override
    public void dispose() {

    }
}
