package com.example.photocube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by TimmosQuadros on 17-04-2017.
 */

public class PhotoCube {
    private FloatBuffer vertexBuffer;
    private FloatBuffer textBuffer;

    private int numFaces = 6;
    private int[] imageFileIDs = {
            R.drawable.dice_one,
            R.drawable.dice_two,
            R.drawable.dice_six,
            R.drawable.dice_five,
            R.drawable.dice_four,
            R.drawable.dice_three
    };
    private int[] textureIds = new int[numFaces];
    private Bitmap[] bitmap = new Bitmap[numFaces];
    private float cubeHalfSize = 0.9991f;

    public PhotoCube(Context context){
        ByteBuffer vbb = ByteBuffer.allocateDirect(12*4*numFaces);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer=vbb.asFloatBuffer();

        for (int face = 0;face < numFaces; face++){
            bitmap[face] = BitmapFactory.decodeStream(context.getResources().openRawResource(imageFileIDs[face]));
            int imgWidth = bitmap[face].getWidth();
            int imgHeight = bitmap[face].getHeight();
            float faceWidth = 2.0f;
            float faceheight = 2.0f;

            if(imgWidth>imgHeight){
                faceheight=faceheight*imgHeight/imgWidth;
            }else{
                faceWidth = faceWidth*imgWidth/imgHeight;
            }

            float faceLeft = -faceWidth/2;
            float faceRight = -faceLeft;
            float faceTop = faceheight/2;
            float faceBottom = -faceTop;

            float[] vertices = {
                    faceLeft, faceBottom,0.0f,
                    faceRight,faceBottom,0.0f,
                    faceLeft,faceTop,0.0f,
                    faceRight,faceTop,0.0f
            };

            vertexBuffer.put(vertices);
        }
        vertexBuffer.position(0);

        float[] texCoords = {
                0.0f,1.0f,
                1.0f,1.0f,
                0.0f,0.0f,
                1.0f,0.0f
        };
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length*4*numFaces);
        tbb.order(ByteOrder.nativeOrder());
        textBuffer = tbb.asFloatBuffer();
        for(int face = 0; face < numFaces; face++){
            textBuffer.put(texCoords);
        }
        textBuffer.position(0);
    }

    public void draw(GL10 gl){
        gl.glFrontFace(GL10.GL_CCW);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBuffer);
        gl.glTexCoordPointer(2,GL10.GL_FLOAT,0,textBuffer);



        //front
        gl.glPushMatrix();
        gl.glTranslatef(0f,0f,cubeHalfSize);
        gl.glBindTexture(GL10.GL_TEXTURE_2D,textureIds[0]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0,4);
        gl.glPopMatrix();

        //left
        gl.glPushMatrix();
        gl.glRotatef(270.0f,0f,1f,0f);
        gl.glTranslatef(0f,0f,cubeHalfSize);
        gl.glBindTexture(GL10.GL_TEXTURE_2D,textureIds[1]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,4,4);
        gl.glPopMatrix();

        //back
        gl.glPushMatrix();
        gl.glRotatef(180.0f,0f,1f,0f);
        gl.glTranslatef(0f,0f,cubeHalfSize);
        gl.glBindTexture(GL10.GL_TEXTURE_2D,textureIds[2]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,8,4);
        gl.glPopMatrix();

        //right
        gl.glPushMatrix();
        gl.glRotatef(90.0f,0f,1f,0f);
        gl.glTranslatef(0f,0f,cubeHalfSize);
        gl.glBindTexture(GL10.GL_TEXTURE_2D,textureIds[3]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,12,4);
        gl.glPopMatrix();

        //top
        gl.glPushMatrix();
        gl.glRotatef(270.0f,1f,0f,0f);
        gl.glTranslatef(0f,0f,cubeHalfSize);
        gl.glBindTexture(GL10.GL_TEXTURE_2D,textureIds[4]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,16,4);
        gl.glPopMatrix();

        //top
        gl.glPushMatrix();
        gl.glRotatef(90.0f,1f,0f,0f);
        gl.glTranslatef(0f,0f,cubeHalfSize);
        gl.glBindTexture(GL10.GL_TEXTURE_2D,textureIds[5]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,20,4);
        gl.glPopMatrix();

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    public void loadTexture(GL10 gl){
        gl.glGenTextures(6,textureIds,0);

        for(int face = 0; face < numFaces; face++){
            gl.glBindTexture(GL10.GL_TEXTURE_2D,textureIds[face]);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D,0,bitmap[face],0);
            bitmap[face].recycle();
        }
    }
}
