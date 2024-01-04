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

//    public static double fovToSenMagnification(double currentFov, double originFov){
//        double a1 = Math.atan(0.75 * Math.tan(Math.toRadians(vToH(currentFov) / 2))) / Math.atan(0.75 * Math.tan(Math.toRadians(vToH(originFov) / 2)));
//        double a2 = (originFov / currentFov) * Math.atan(0.75 * Math.tan(Math.toRadians(vToH(currentFov) / 2))) / Math.atan(0.75 * Math.tan(Math.toRadians(vToH(originFov) / 2)));
//        return Math.atan(0.75 * Math.tan(Math.toRadians(vToH(currentFov) / 2))) / Math.atan(0.75 * Math.tan(Math.toRadians(vToH(originFov) / 2)));
//    }
//
//    private static double vToH(double v) {
//        return Math.atan(Math.tan(Math.toRadians(v / 2)) * 16 / 9) * 360 / Math.PI;
//    }
}