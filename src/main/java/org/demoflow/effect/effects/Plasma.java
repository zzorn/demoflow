package org.demoflow.effect.effects;

import java.awt.Color;

import org.demoflow.effect.EffectBase;
import org.demoflow.effect.RenderContext;
import org.demoflow.parameter.calculator.CalculationContext;
import org.flowutils.random.RandomSequence;
import org.lwjgl.opengl.GL11;


/**
 * A simple plasma.
 */
public final class Plasma extends EffectBase<Object> {

	private double time = 0;
	private final float SQRSIZE = 0.02f;
	
    public Plasma() {

    }

    @Override protected void doSetup(Object preCalculatedData, RandomSequence randomSequence) {

    }

    @Override protected void doUpdate(CalculationContext calculationContext) {
        time = calculationContext.getSecondsFromDemoStart();
    }

    @Override protected void doRender(RenderContext renderContext) {
    	
		//Kauhee rainbowplasma
		GL11.glPushMatrix();
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glTranslatef(1, -1, -0.9f);
		GL11.glRotatef(90, 0, 0, 1);
		for(float xi=0; xi < 2; xi+=SQRSIZE){
			for(float yi=0; yi < 2; yi+=SQRSIZE){
				float at = (float) (time);
				float h = 0.4f;
				float d = (float) (Math.pow(xi*h+xi*h, Math.atan(yi+h)*Math.cos(yi*h+at))+Math.sin(at+xi*h+yi*h));
				Color C = Color.getHSBColor(d,1,1);
//				if( Math.atan(yi+h)*Math.cos(yi*h+at) < 0.2f && xi*h+xi*h < 0.3f){
//					GL11.glColor4f(0,0,0,0);
//				} else {
					GL11.glColor4f(C.getRed()/255.0f, C.getGreen()/255.0f, C.getBlue()/255.0f, 0.4f);
//				}
				
				draw2DRect(xi,yi,xi+SQRSIZE,yi+SQRSIZE,0);
			}
		}
		GL11.glPopMatrix();
    }

	private static void draw2DRect(float x0, float y0, float x1, float y1, float z){
		GL11.glBegin( GL11.GL_QUADS );
			GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0,0); GL11.glVertex3d(x0, y0, z);	
			GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0,0.9999999); GL11.glVertex3d(x0, y1, z);
			GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0.9999999,0.9999999); GL11.glVertex3d(x1, y1, z);
			GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0.9999999,0); GL11.glVertex3d(x1, y0, z);
		GL11.glEnd();
	}
    
    @Override protected void doShutdown() {
        
    }
}
