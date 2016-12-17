package ro.edu.nt.cni.atestat.Mig2D;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseCircularOut;
import org.andengine.util.modifier.ease.EaseLinear;

/**
 * Created by Cristina on 4/12/2014.
 */

public class BadSpaceship extends Sprite {

    private ITextureRegion mLaserTextureRegion;

    public BadSpaceship( ITextureRegion pBadSpaceshipTextureRegion, ITextureRegion laserTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0,0, pBadSpaceshipTextureRegion, pVertexBufferObjectManager);
        this.mLaserTextureRegion = laserTextureRegion;

    }

    /*public void badSpaceshipFire(final MainActivity pMainActivity, final Scene pScene, final BadSpaceshipsContainer badSpaceshipsContainer) {

        final Sprite laser = new Sprite(0, 0, mLaserTextureRegion, this.getVertexBufferObjectManager());
        laser.setHeight(50);
        laser.setWidth(15);
        laser.resetRotationCenter();
        laser.setRotation(90);

    }*/

    public void fire(final MainActivity pMainActivity, final Scene pScene, Camera pCamera , final BadSpaceshipsContainer badSpaceshipsContainer, final Spaceship pGoodSpaceship){

        if(this.isVisible()&& pGoodSpaceship.isVisible()) {
            final Sprite laser = new Sprite(0, 0, mLaserTextureRegion, this.getVertexBufferObjectManager());
            laser.setHeight(50);
            laser.setWidth(15);
            laser.resetRotationCenter();
            laser.setRotation(90);


            float[] coordinates = badSpaceshipsContainer.convertLocalToSceneCoordinates(this.getX(), this.getY());
            laser.setPosition(coordinates[0] + this.getWidth() * 0.5f - laser.getWidth() * 0.5f, coordinates[1] + this.getHeight() * 0.5f - laser.getHeight() * 0.5f);

            laser.registerEntityModifier(new MoveXModifier(2, laser.getX(), pCamera.getWidth(), new IEntityModifier.IEntityModifierListener() {
                @Override
                public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

                }

                @Override
                public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                    pMainActivity.runOnUpdateThread(new Runnable() {
                        @Override
                        public void run() {
                            pScene.detachChild(laser);
                            if (!laser.isDisposed()) {
                                laser.dispose();
                            }
                        }
                    });
                }
            }, EaseLinear.getInstance()));

            pMainActivity.runOnUpdateThread(new Runnable() {
                @Override
                public void run() {
                    pScene.attachChild(laser);
                }
            });

        pScene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {

                if(!laser.isDisposed()&&pGoodSpaceship.collidesWith(laser) &&pGoodSpaceship.isVisible()){
                    laser.clearEntityModifiers();
                    pMainActivity.runOnUpdateThread(new Runnable() {
                        @Override
                        public void run() {
                            pScene.detachChild(laser);
                            if(!laser.isDisposed()) {
                                laser.dispose();
                            }
                        }
                    });
                    pGoodSpaceship.setVisible(false);

                }

            }

            @Override
            public void reset() {

            }
        });
        }

    }

}
