package ro.edu.nt.cni.atestat.Mig2D;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

/**
 * Created by Cristina on 4/12/2014.
 */


public class BadSpaceshipsContainer extends Rectangle {
    private BadSpaceship[][] mBadSpaceships;

    private int mColumns;
    private int mRows;

    public BadSpaceshipsContainer (final MainActivity mainActivity, int rows, int columns, float delta,
                                   final Scene pScene, final Camera pCamera, final Spaceship pGoodSpaceship,
                                   ITextureRegion badSpaceshipTextureRegion,ITextureRegion laserTextureRegion  , VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0,0,0,0, pVertexBufferObjectManager);
        this.setAlpha(0);
        this.mBadSpaceships= new BadSpaceship[columns][rows];
        this.mColumns = columns;
        this.mRows = rows;

        this.setWidth(columns*delta);
        this.setHeight(rows*delta);

        for(int i=0; i<columns; i++ ) {
            for (int j = 0; j <rows; j++){
                this.mBadSpaceships[i][j]= new BadSpaceship( badSpaceshipTextureRegion, laserTextureRegion, this.getVertexBufferObjectManager());
                this.mBadSpaceships[i][j].setWidth(delta);
                this.mBadSpaceships[i][j].setHeight(delta);
                this.mBadSpaceships[i][j].resetRotationCenter();
                this.mBadSpaceships[i][j].setRotation(180);
                this.mBadSpaceships[i][j].setPosition(i*delta,j*delta);
                final int finali=i;
                final int finalj=j;
                float time = 5+new Random().nextFloat()*5;
                this.mBadSpaceships[i][j].registerUpdateHandler(new TimerHandler(time,true,new ITimerCallback() {
                    @Override
                    public void onTimePassed(TimerHandler pTimerHandler) {
                        mBadSpaceships[finali][finalj].fire(mainActivity, pScene, pCamera, BadSpaceshipsContainer.this,pGoodSpaceship  );
                    }
                }));
                this.attachChild(mBadSpaceships[i][j]);


            }
        }

    }
    public BadSpaceship[][] getBadSpaceships(){
        return this.mBadSpaceships;
    }
    public int getColumns(){
        return mColumns;
    }
    public int getRows(){
        return mRows;
    }
}
