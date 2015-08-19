package org.demoflow.effect.effects;

import java.awt.Color;

import org.demoflow.effect.EffectBase;
import org.demoflow.effect.RenderContext;
import org.demoflow.parameter.Parameter;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.parameter.range.ranges.DoubleRange;
import org.demoflow.parameter.range.ranges.IntRange;
import org.flowutils.MathUtils;
import org.flowutils.random.RandomSequence;
import org.lwjgl.opengl.GL11;


/**
 * A simple plasma.
 */
public final class Trees extends EffectBase<Object> {

	public final Parameter<Double> gapSize;
	public final Parameter<Integer> treeDepth;

	private static final float SQRT_2 = (float) Math.sqrt(2);

	private double time = 0;

    public Trees() {
		gapSize = addParameter("gapSize", 0.0, DoubleRange.POSITIVE);
		treeDepth = addParameter("treeDepth", 10, IntRange.POSITIVE);
	}

    @Override protected void doSetup(Object preCalculatedData) {
    }

	@Override protected void doReset() {
	}

	@Override protected void doUpdate(CalculationContext calculationContext) {
        time = calculationContext.getSecondsFromDemoStart();
    }

    @Override protected void doRender(RenderContext renderContext) {

		//Kauhee rainbowplasma
		GL11.glPushMatrix();
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glTranslatef(0, 0, -0.9f);
		GL11.glRotatef(90, 0, 0, 1);
		//Todo trees <3
		GL11.glPopMatrix();
    }
    
    private static void drawBranch(){
		GL11.glBegin( GL11.GL_TRIANGLES );
		GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0,0);                 GL11.glVertex3d(0, 0, 0);
		GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0,0.9999999);         GL11.glVertex3d(0, 1, 0);
		GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0.9999999,0.9999999); GL11.glVertex3d(1, 1, 0);
		GL11.glEnd();    	
    }
    
    @Override protected void doShutdown() {
        
    }
}
