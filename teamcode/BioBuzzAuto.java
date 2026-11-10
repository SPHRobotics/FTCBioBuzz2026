package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;






@Autonomous(name="BioBuzzAuto=", group="Robot")


public class BioBuzzAuto extends LinearOpMode {




    // Drive motors
    private DcMotor RF;
    private DcMotor RB;
    private DcMotor LF;
    private DcMotor LB;

    // Intake motor
    private DcMotor IN;

    // Shooter motors
    private DcMotor SB;
    private DcMotor SM;

    // FP motor
    private DcMotor FP;

    // Ball gate servos
    private Servo SBG;
    private Servo BBG;
    private ElapsedTime     runtime = new ElapsedTime();




    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;
    private static final double SB_POWER = 0.445;
    private static final double SM_POWER = 0.47;
    // Small ball gate servo positions
    private static final double SBG_CLOSED = 0.80;
    private static final double SBG_OPEN = 0.30;

    // Big ball gate servo positions
    private static final double BBG_CLOSED = 0.62;
    private static final double BBG_OPEN = 0.90 ;


    @Override
    public void runOpMode() {




        RF = hardwareMap.get(DcMotor.class, "RF");
        RB = hardwareMap.get(DcMotor.class, "RB");
        LF = hardwareMap.get(DcMotor.class, "LF");
        LB = hardwareMap.get(DcMotor.class, "LB");

        IN = hardwareMap.get(DcMotor.class, "IN");

        SB = hardwareMap.get(DcMotor.class, "SB");
        SM = hardwareMap.get(DcMotor.class, "SM");

        FP = hardwareMap.get(DcMotor.class, "FP");

        SBG = hardwareMap.get(Servo.class, "SBG");
        BBG = hardwareMap.get(Servo.class, "BBG");


        // MOTOR DIRECTIONS
        RF.setDirection(DcMotorSimple.Direction.FORWARD);
        RB.setDirection(DcMotorSimple.Direction.FORWARD);

        LF.setDirection(DcMotorSimple.Direction.REVERSE);
        LB.setDirection(DcMotorSimple.Direction.REVERSE);

        IN.setDirection(DcMotorSimple.Direction.FORWARD);

        SB.setDirection(DcMotorSimple.Direction.REVERSE);
        SM.setDirection(DcMotorSimple.Direction.FORWARD);

        FP.setDirection(DcMotorSimple.Direction.FORWARD);



        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();




        //Function Names
            //forward
            //backward
            //right
            //left
            //spinLeft
            //spinRight
            //rearAxisTurn
            //frontAxisTurn
            //brake
            //smallBallGate
            //bigBallGate
        //Motor is on every function, ensure you describe whether the flywheel is on or not
        waitForStart();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        smallBallGate(0.1,false, false);
        bigBallGate(0.1,false, false);
        brake(5, true);
        smallBallGate(0.4,true, true);
        smallBallGate(0.5,false, true);
        smallBallGate(0.4,true, true);
        smallBallGate(1,false, true);
        smallBallGate(0.4,true, true);
        smallBallGate(1,false, true);
        smallBallGate(2,true, true);
        smallBallGate(0.5,false, true);
        brake(3, true);
        backward(0.5, 0.2, false);
        right(0.5, 1, false);
        backward(0.5, 2.5, false);
        right(0.5, 0.3, false);




    }

    //Functions, Please use discretion before editing these, as they can break the pathing
    //Only edit if the bot is moving in a direction contrary to its name
    //If you aren't sure, ask a programmer
    public void forward(double speed, double time, boolean flywheel) {




        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            RF.setPower(speed);
            LF.setPower(speed);
            RB.setPower(speed);
            LB.setPower(speed);

            if (flywheel) {
                SM.setPower(SM_POWER);
                SB.setPower(SB_POWER);

            }
        }
}
    public void backward(double speed, double time, boolean flywheel) {


        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {

            RF.setPower(-speed);
            LF.setPower(-speed);
            RB.setPower(-speed);
            LB.setPower(-speed);

            if (flywheel) {
                SM.setPower(SM_POWER);
                SB.setPower(SB_POWER);

            }
        }
    }

    public void right(double speed, double time, boolean flywheel) {


        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            RF.setPower(-speed);
            LF.setPower(speed);
            RB.setPower(speed);
            LB.setPower(-speed);

            if (flywheel) {
                SM.setPower(SM_POWER);
                SB.setPower(SB_POWER);

            }
        }
    }
    public void left(double speed, double time, boolean flywheel) {


        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            RF.setPower(speed);
            LF.setPower(-speed);
            RB.setPower(-speed);
            LB.setPower(speed);

            if (flywheel) {
                SM.setPower(SM_POWER);
                SB.setPower(SB_POWER);

            }
        }
    }
    public void spinRight(double speed, double time, boolean flywheel) {


        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            RF.setPower(-speed);
            LF.setPower(speed);
            RB.setPower(-speed);
            LB.setPower(speed);

            if (flywheel) {
                SM.setPower(SM_POWER);
                SB.setPower(SB_POWER);

            }
        }
    }
    public void spinLeft(double speed, double time, boolean flywheel) {


        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            RF.setPower(speed);
            LF.setPower(-speed);
            RB.setPower(speed);
            LB.setPower(-speed);

            if (flywheel) {
                SM.setPower(SM_POWER);
                SB.setPower(SB_POWER);

            }
        }
    }
    public void rearAxisTurn(double speed, double time, boolean flywheel) {


        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            RF.setPower(-speed);
            LF.setPower(speed);
            RB.setPower(0);
            LB.setPower(0);

            if (flywheel == true) {
                SM.setPower(SM_POWER);
                SB.setPower(SB_POWER);

            }
        }
    }
    public void frontAxisTurn(double speed, double time, boolean flywheel) {


        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            RF.setPower(0);
            LF.setPower(0);
            RB.setPower(speed);
            LB.setPower(-speed);

            if (flywheel == true) {
                SM.setPower(SM_POWER);
                SB.setPower(SB_POWER);

            }
        }

    }
    public void bigBallGate(double time, boolean bigGateOpen, boolean flywheel) {
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            if (bigGateOpen) {
            BBG.setPosition(BBG_OPEN);
        } else {
            BBG.setPosition(BBG_CLOSED);
        }
        if (flywheel == true) {
            SM.setPower(SM_POWER);
            SB.setPower(SB_POWER);

        }}
    }

    public void smallBallGate(double time, boolean smallGateOpen, boolean flywheel) {
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            if (smallGateOpen){
            SBG.setPosition(SBG_OPEN);
        }
        else {
            SBG.setPosition(SBG_CLOSED);
        }
        if (flywheel) {
            SM.setPower(SM_POWER);
            SB.setPower(SB_POWER);

        }}




    }
    public void brake(double time, boolean flywheel) {


        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            RF.setPower(0);
            LF.setPower(0);
            RB.setPower(0);
            LB.setPower(0);

            if (flywheel == true) {
                SM.setPower(SM_POWER);
                SB.setPower(SB_POWER);

            }
        }


        }
}
