package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thechinczyk.game.MyTheChinczyk;

public class dayParkMap implements Screen {

    MyTheChinczyk game;

    private Texture dayParkBackground;

    private TextureAtlas iceCreamAtlas;
    private Animation<TextureRegion> iceCreamAnim;
    private float iceCreamElapsedTime;

    public dayParkMap(MyTheChinczyk game){
        this.game = game;
    }

    @Override
    public void show() {
        dayParkBackground = new Texture("Map1/TC_Map1_Main.png");

        iceCreamAtlas = new TextureAtlas("Map1/IceCreamAnimationSheet/myIceCreamAnimationSheet.atlas");
        iceCreamAnim = new Animation<TextureRegion>(1f/30f, iceCreamAtlas.getRegions());
        iceCreamElapsedTime = 0f;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        //Wyświetlenie głównego tła planszy
        game.batch.draw(dayParkBackground, 0, 0, 1920, 1080);
        //Animacja rożka z lodem na planszy
        iceCreamElapsedTime += Gdx.graphics.getDeltaTime();
        game.batch.draw(iceCreamAnim.getKeyFrame(iceCreamElapsedTime, true), 0, 888, 179, 192);

        //Powrót do menu głównego
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.gameScreen = 3;
        }

        game.batch.end();
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
        iceCreamAtlas.dispose();

        dayParkBackground.dispose();
    }
}
