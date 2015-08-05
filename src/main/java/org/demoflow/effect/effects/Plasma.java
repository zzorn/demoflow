package org.demoflow.effect.effects;

import java.awt.Color;

import org.demoflow.effect.EffectBase;
import org.demoflow.effect.RenderContext;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.calculator.CalculationContext;
import org.demoflow.parameter.range.ranges.DoubleRange;
import org.demoflow.parameter.range.ranges.IntRange;
import org.flowutils.MathUtils;
import org.flowutils.random.RandomSequence;
import org.lwjgl.opengl.GL11;


/**
 * A simple plasma.
 */
public final class Plasma extends EffectBase<Object> {

	public final Parameter<Double> squareSize;
	public final Parameter<Double> squareAspect;
	public final Parameter<Double> gapSize;
	public final Parameter<Integer> squareCountX;
	public final Parameter<Integer> squareCountY;

	private static final float SQRT_2 = (float) Math.sqrt(2);

	private double time = 0;

    public Plasma() {
		squareSize = addParameter("squareSize", 0.01, DoubleRange.POSITIVE);
		squareAspect = addParameter("squareAspect", 1.0, DoubleRange.ZERO_TO_ONE);
		gapSize = addParameter("gapSize", 0.0, DoubleRange.POSITIVE);
		squareCountX = addParameter("squareCountX", 100, IntRange.POSITIVE);
		squareCountY = addParameter("squareCountY", 100, IntRange.POSITIVE);
	}

    @Override protected void doSetup(Object preCalculatedData, RandomSequence randomSequence) {

    }

    @Override protected void doUpdate(CalculationContext calculationContext) {
        time = calculationContext.getSecondsFromDemoStart();
    }

    @Override protected void doRender(RenderContext renderContext) {

		// Center of plasma effect.  0,0 = center of screen
		final float centerX = 0;
		final float centerY = 0;

		// Number of squares along x and y axis.
		int xCount = squareCountX.get();
		int yCount = squareCountY.get();

		// Size of the side of each square, and the gap between squares
		final float aspectRatio = (float)(double)this.squareAspect.get();
		final float baseSquareSize = 2f * (float)(double)this.squareSize.get();
		final float squareSizeX = baseSquareSize * SQRT_2 * (float) Math.abs(Math.cos((0.5 + aspectRatio) / 4 * MathUtils.Tau));
		final float squareSizeY = baseSquareSize * SQRT_2 * (float) Math.abs(Math.sin((0.5 + aspectRatio) / 4 * MathUtils.Tau));
		final float gapSize = (float)(double)this.gapSize.get();
		final float stepSizeX = squareSizeX + gapSize;
		final float stepSizeY = squareSizeY + gapSize;

		if (squareSizeX <= 0.0 || squareSizeY <= 0.0) return; // Infinitely thin squares, nothing to render.

		// Offset
		float xOffset = -0.5f * (xCount - 1) * stepSizeX + centerX;
		float yOffset = -0.5f * (yCount - 1) * stepSizeY + centerY;


		//Kauhee rainbowplasma
		GL11.glPushMatrix();
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glTranslatef(0, 0, -0.9f);
		GL11.glRotatef(90, 0, 0, 1);

		for (int xi = 0; xi < xCount; xi++) {

			final float x = xi * stepSizeX + xOffset;
			final float plasmaX = (float) xi / xCount;

			for (int yi = 0; yi < yCount; yi++) {

				final float y = yi * stepSizeY + yOffset;
				final float plasmaY = (float) yi / yCount;

				float at = (float) (time);
				float h = 0.4f;
				float d = (float) (Math.pow(plasmaX *h+ plasmaX *h, Math.atan(plasmaY +h)*Math.cos(plasmaY *h+at))+Math.sin(at+ plasmaX *h+ plasmaY *h));
				Color C = Color.getHSBColor(d,1,1);
//				if( Math.atan(y+h)*Math.cos(y*h+at) < 0.2f && x*h+x*h < 0.3f){
//					GL11.glColor4f(0,0,0,0);
//				} else {
					GL11.glColor4f(C.getRed()/255.0f, C.getGreen()/255.0f, C.getBlue()/255.0f, 0.4f);
//				}
				
				draw2DRect(x - squareSizeX/2, y - squareSizeY/2,
						   x + squareSizeX/2, y + squareSizeY/2,
						   0);
			}
		}
		GL11.glPopMatrix();
    }

	private static void draw2DRect(float x0, float y0, float x1, float y1, float z){
		GL11.glBegin( GL11.GL_QUADS );
			GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0,0);                 GL11.glVertex3d(x0, y0, z);
			GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0,0.9999999);         GL11.glVertex3d(x0, y1, z);
			GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0.9999999,0.9999999); GL11.glVertex3d(x1, y1, z);
			GL11.glNormal3f(0,0,1); GL11.glTexCoord2d(0.9999999,0);         GL11.glVertex3d(x1, y0, z);
		GL11.glEnd();
	}
    
    @Override protected void doShutdown() {
        
    }
}
