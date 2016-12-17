package ro.edu.nt.cni.atestat.Mig2D;

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

/**
 * Created by Cristina on 4/12/2014.
 */
public class Spaceship extends Sprite {

    private ITextureRegion mLaserTextureRegion;

    public Spaceship(float pX, float pY, ITextureRegion pTextureRegion, ITextureRegion laserTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.mLaserTextureRegion= laserTextureRegion;

    }

    public void fire(final MainActivity pMainActivity, final Scene pScene, final BadSpaceshipsContainer badSpaceshipsContainer){
        if(this.isVisible()) {
            final Sprite laser = new Sprite(0, 0, mLaserTextureRegion, this.getVertexBufferObjectManager());
            laser.setHeight(50);
            laser.setWidth(15);
            laser.resetRotationCenter();
            laser.setRotation(90);

            laser.setPosition(this.getX() + this.getWidth() * 0.5f - laser.getWidth() * 0.5f, this.getY() + this.getHeight() * 0.5f - laser.getHeight() * 0.5f);

            laser.registerEntityModifier(new MoveXModifier(1, laser.getX(), -laser.getWidth(), new IEntityModifier.IEntityModifierListener() {
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
            }, EaseCircularOut.getInstance()));

            pMainActivity.runOnUpdateThread(new Runnable() {
                @Override
                public void run() {
                    pScene.attachChild(laser);
                }
            });

            pScene.registerUpdateHandler(new IUpdateHandler() {
                @Override
                public void onUpdate(float pSecondsElapsed) {
                    for (int i = 0; i < badSpaceshipsContainer.getColumns(); i++) {
                        for (int j = 0; j < badSpaceshipsContainer.getRows(); j++) {
                            if (!laser.isDisposed() &&
                                    badSpaceshipsContainer.getBadSpaceships()[i][j].collidesWith(laser) &&
                                    badSpaceshipsContainer.getBadSpaceships()[i][j].isVisible()) {
                                laser.clearEntityModifiers();
                                pMainActivity.runOnUpdateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pScene.detachChild(laser);
                                        if (!laser.isDisposed()) {
                                            laser.dispose();
                                        }
                                    }
                                });
                                badSpaceshipsContainer.getBadSpaceships()[i][j].setVisible(false);

                            }
                        }
                    }
                }

                @Override
                public void reset() {

                }
            });
        }
    }

}
