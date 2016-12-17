package ro.edu.nt.cni.atestat.Mig2D;

import android.content.Intent;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.ease.EaseBackInOut;
import org.andengine.util.modifier.ease.EaseCircularInOut;
import org.andengine.util.modifier.ease.EaseLinear;

public class MainActivity extends SimpleBaseGameActivity implements MenuScene.IOnMenuItemClickListener{
    private static final int CAMERA_HEIGHT = 768;
    private static final int CAMERA_WIDTH = 1280;

    private Camera mCamera;
    private Scene mScene;
    private BitmapTextureAtlas mBitmapTextureAtlas;
    private ITextureRegion mSpaceshipTextureRegion;
    private ITextureRegion mBadSpaceshipTextureRegion;
    private ITextureRegion mLaserTextureRegion;

    private BitmapTextureAtlas mAutoParallaxBackgroundTexture;


    private ITextureRegion mParallaxLayerBack;
    private ITextureRegion mParallaxLayerMid;
    private ITextureRegion mParallaxLayerFront;

    private ITextureRegion mFaceTextureRegion;

    private ITextureRegion mPlayButton;
    private ITextureRegion mWinButton;
    private ITextureRegion mLooseButton;
    private ITextureRegion mResetButton;

    private Sprite[][] mBadSpaceships;
    private Spaceship spaceshipSprite;

    private WinLooseScene mMenuScene;

    BadSpaceshipsContainer badSpaceshipsContainer;

    private boolean mFireLaser;
    private boolean mGameOver;

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);

    }

    @Override
    protected void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        this.mBitmapTextureAtlas= new BitmapTextureAtlas(this.getTextureManager(),2048,2048, TextureOptions.BILINEAR);
        this.mSpaceshipTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "spaceship.png", 0, 0);
        this.mBadSpaceshipTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "badSpaceship.png", 0, 130);
        this.mLaserTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "laser.png", 0,430);
        this.mPlayButton= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas,this,"playButton.png",0,580);
        this.mWinButton= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas,this,"winButton.png",0,700);
        this.mLooseButton= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas,this,"looseButton.png",0,820);
        this.mResetButton= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas,this,"resetButton.png",0,940);

        this.mBitmapTextureAtlas.load();

        this.mAutoParallaxBackgroundTexture= new BitmapTextureAtlas(this.getTextureManager(),1024,1024);
        this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "parallax_background_layer_front.png", 0, 0);
        this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "parallax_background_layer_back.png", 0,188 );
        this.mParallaxLayerMid= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "parallax_background_layer_mid.png", 0,669  );
        this.mAutoParallaxBackgroundTexture.load();



    }

    @Override
    protected Scene onCreateScene() {
        this.mGameOver=false;
        this.mScene = new Scene();
        this.mScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

                return false;
            }
        });
        final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0,0,0,5);
        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();

        Sprite layer1= new Sprite(0, CAMERA_HEIGHT - this.mParallaxLayerBack.getHeight(), this.mParallaxLayerBack, vertexBufferObjectManager);
        Sprite layer2= new Sprite(0, 80, this.mParallaxLayerMid, vertexBufferObjectManager);
        Sprite layer3= new Sprite(0, CAMERA_HEIGHT - this.mParallaxLayerFront.getHeight(), this.mParallaxLayerFront, vertexBufferObjectManager);

//        layer1.setRotation(90);
//        layer2.setRotation(90);
//        layer3.setRotation(90);

        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(0.0f, layer1));
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(50.0f, layer2));
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(100.0f, layer3));

        mScene.setBackground(autoParallaxBackground);

        final float centerY = (CAMERA_HEIGHT - this.mSpaceshipTextureRegion.getHeight()) / 2;
        spaceshipSprite = new Spaceship(1150, centerY, this.mSpaceshipTextureRegion, this.mLaserTextureRegion , this.getVertexBufferObjectManager()) {

            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                this.setPosition(pSceneTouchEvent.getX() - this.getWidth()*0.5f, pSceneTouchEvent.getY() - this.getHeight()*0.5f);
                if(pSceneTouchEvent.isActionDown()){
                    mFireLaser = true;
                } else if(pSceneTouchEvent.isActionUp()){
                    mFireLaser = false;
                }
                return true;
            }
        };
        mFireLaser = false;



       badSpaceshipsContainer = new BadSpaceshipsContainer(this,4,4,100,mScene, mCamera,spaceshipSprite,mSpaceshipTextureRegion,mLaserTextureRegion, this.getVertexBufferObjectManager());
        badSpaceshipsContainer.setPosition(mCamera.getWidth()*0.25f-badSpaceshipsContainer.getWidth()*0.5f, 0);

        mScene.registerUpdateHandler(new TimerHandler(0.5f,true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                if(mFireLaser) {
                    spaceshipSprite.fire(MainActivity.this, mScene, badSpaceshipsContainer);
                }
            }
        }));



        badSpaceshipsContainer.registerEntityModifier(new LoopEntityModifier(
                new SequenceEntityModifier(
                        new MoveYModifier(2,badSpaceshipsContainer.getY(),mCamera.getHeight()-badSpaceshipsContainer.getHeight(), EaseLinear.getInstance()),
                        new MoveYModifier(2,mCamera.getHeight()-badSpaceshipsContainer.getHeight(),0, EaseLinear.getInstance())


                )
        ));


        mScene.attachChild(badSpaceshipsContainer);
        mScene.attachChild(spaceshipSprite);
        mScene.registerTouchArea(spaceshipSprite);
        mScene.setTouchAreaBindingOnActionDownEnabled(true);

        mMenuScene = new WinLooseScene(mCamera,mPlayButton,mWinButton,mLooseButton, mResetButton, this.getVertexBufferObjectManager());
        mMenuScene.setOnMenuItemClickListener(this);
        mMenuScene.setState(0);
        mScene.setChildScene(mMenuScene,false,true,true);

        mScene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if(!spaceshipSprite.isVisible()&&!mGameOver){
                    mGameOver=true;
                    mMenuScene.setState(2);
                    mScene.registerUpdateHandler(new TimerHandler(1,new ITimerCallback() {
                        @Override
                        public void onTimePassed(TimerHandler pTimerHandler) {
                            mScene.setChildScene(mMenuScene,false,false,true);

                        }


                    }));

                }
                int x=0;
                for(int i=0;i<badSpaceshipsContainer.getColumns();i++){
                    for(int j=0; j<badSpaceshipsContainer.getRows();j++){
                        if(badSpaceshipsContainer.getBadSpaceships()[i][j].isVisible()){
                            x=1;
                            return;
                        }

                    }
                }
                if(x==0){
                    mMenuScene.setState(1);
                    mScene.setChildScene(mMenuScene,false,false,true);
                }
            }

            @Override
            public void reset() {

            }
        });

        return mScene;
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        if(pMenuItem.getID()==1){
            mScene.clearChildScene();
            return true;
        }
        if(pMenuItem.getID()==4){
            this.mGameOver=false;
            spaceshipSprite.setVisible(true);
            for(int i=0;i<badSpaceshipsContainer.getColumns();i++){
                for(int j=0; j<badSpaceshipsContainer.getRows();j++){
                    badSpaceshipsContainer.getBadSpaceships()[i][j].setVisible(true);
                }
            }

            mScene.clearChildScene();
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            this.startActivity(intent);
//            this.finish();
            return true;
        }
        return false;
    }
}