package com.tac.guns.util.math;

public class MathUtil {
    public static double magnificationToFovMultiplier(double magnification, double currentFov){
        double currentTan = Math.tan(Math.toRadians(currentFov / 2));
        double newTan = currentTan / magnification;
        double newFov = Math.toDegrees(Math.atan(newTan)) * 2;
        return newFov / currentFov;
    }

    public static double fovToMagnification(double currentFov, double originFov){
        return Math.tan(Math.toRadians(originFov / 2)) / Math.tan(Math.toRadians(currentFov / 2));
    }

    public static double fovToSenMagnification(double currentFov, double originFov, double zoomMultiple){
        double multiple;
        if (zoomMultiple < 3.11) {
            multiple = zoomMultiple - 2.99 >= 0 ? 1 : 0.11 * zoomMultiple;
        } else
            multiple = zoomMultiple - 2;
        return (Math.tan(Math.toRadians(originFov / 2)) / Math.tan(Math.toRadians(currentFov / 2))) * Math.pow(multiple, 2);
    }
}