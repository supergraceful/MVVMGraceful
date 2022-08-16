package me.magical.mvvmgraceful.ui.loading;


import me.magical.mvvmgraceful.ui.loading.sprite.Sprite;
import me.magical.mvvmgraceful.ui.loading.style.ChasingDots;
import me.magical.mvvmgraceful.ui.loading.style.Circle;
import me.magical.mvvmgraceful.ui.loading.style.CubeGrid;
import me.magical.mvvmgraceful.ui.loading.style.DoubleBounce;
import me.magical.mvvmgraceful.ui.loading.style.FadingCircle;
import me.magical.mvvmgraceful.ui.loading.style.FoldingCube;
import me.magical.mvvmgraceful.ui.loading.style.MultiplePulse;
import me.magical.mvvmgraceful.ui.loading.style.MultiplePulseRing;
import me.magical.mvvmgraceful.ui.loading.style.Pulse;
import me.magical.mvvmgraceful.ui.loading.style.PulseRing;
import me.magical.mvvmgraceful.ui.loading.style.RotatingCircle;
import me.magical.mvvmgraceful.ui.loading.style.RotatingPlane;
import me.magical.mvvmgraceful.ui.loading.style.ThreeBounce;
import me.magical.mvvmgraceful.ui.loading.style.WanderingCubes;
import me.magical.mvvmgraceful.ui.loading.style.Wave;

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new MultiplePulseRing();
                break;
            default:
                break;
        }
        return sprite;
    }
}
