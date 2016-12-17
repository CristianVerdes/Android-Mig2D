package ro.edu.nt.cni.atestat.Mig2D;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Cristina on 4/13/2014.
 */
public class WinLooseScene extends MenuScene{
    private SpriteMenuItem mPlayButton;
    private SpriteMenuItem mWinButton;
    private SpriteMenuItem mLooseButton;
    private SpriteMenuItem mResetButton;

    private int mState;

    public WinLooseScene(Camera pCamera,
                         ITextureRegion pPlayButtonTextureRegion,
                         ITextureRegion pWinButtonTextureRegion,
                         ITextureRegion pLooseButtonTextureRegion,
                         ITextureRegion pResetButtonTextureRegion,
                         VertexBufferObjectManager pVertexBufferObjectManager){
        super(pCamera);
        this.mPlayButton= new SpriteMenuItem(1,pPlayButtonTextureRegion, pVertexBufferObjectManager);
        this.mWinButton= new SpriteMenuItem(2,pWinButtonTextureRegion, pVertexBufferObjectManager);
        this.mLooseButton= new SpriteMenuItem(3,pLooseButtonTextureRegion, pVertexBufferObjectManager);
        this.mResetButton= new SpriteMenuItem(4,pResetButtonTextureRegion, pVertexBufferObjectManager);

        this.mPlayButton.setPosition(pCamera.getWidth()*0.5f-mPlayButton.getWidth()*0.5f,pCamera.getHeight()*0.5f-mPlayButton.getHeight()*0.5f);
        this.mWinButton.setPosition(pCamera.getWidth()*0.5f-mWinButton.getWidth()*0.5f,pCamera.getHeight()*0.5f-mWinButton.getHeight());
        this.mLooseButton.setPosition(pCamera.getWidth()*0.5f-mLooseButton.getWidth()*0.5f,pCamera.getHeight()*0.5f-mLooseButton.getHeight());
        this.mResetButton.setPosition(pCamera.getWidth()*0.5f-mResetButton.getWidth()*0.5f,pCamera.getHeight()*0.5f);

        this.addMenuItem(mPlayButton);
        this.addMenuItem(mWinButton);
        this.addMenuItem(mLooseButton);
        this.addMenuItem(mResetButton);

        this.unregisterTouchArea(mWinButton);
        this.unregisterTouchArea(mLooseButton);
        this.setBackgroundEnabled(false);
        this.setAlpha(0.8f);
    }

    public void setState(int pState){
        // 0=play, 1=win, 2=loose
        this.mState= pState;
        switch(mState){
            case 0:
                mPlayButton.setVisible(true);
                mWinButton.setVisible(false);
                mLooseButton.setVisible(false);
                mResetButton.setVisible(false);

                this.registerTouchArea(mPlayButton);
                this.unregisterTouchArea(mResetButton);
                break;
            case 1:

                mPlayButton.setVisible(false);
                mWinButton.setVisible(true);
                mLooseButton.setVisible(false);
                mResetButton.setVisible(true);

                this.unregisterTouchArea(mPlayButton);
                this.registerTouchArea(mResetButton);

                break;
            case 2:
                mPlayButton.setVisible(false);
                mWinButton.setVisible(false);
                mLooseButton.setVisible(true);
                mResetButton.setVisible(true);

                this.unregisterTouchArea(mPlayButton);
                this.registerTouchArea(mResetButton);
                break;
            default:
                break;
        }
    }

}
