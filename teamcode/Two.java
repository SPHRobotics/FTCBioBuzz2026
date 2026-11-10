
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Two", group = "TeleOp")
public class Two extends LinearOpMode {

    // DRIVE MOTORS
    private DcMotor RF, RB, LF, LB;

    // MECHANISM MOTORS
    private DcMotor IN;
    private DcMotor SB, SM;
    private DcMotor FP;

    // GATE SERVOS
    private Servo SBG, BBG;

    // RGB INDICATOR
    private Servo GateLight;

    // MOTOR POWERS
    private static final double SB_POWER = 0.455;
    private static final double SM_POWER = 0.47;
    private static final double FP_POWER = 1.0;

    // GATE POSITIONS
    // REVERSED TO MATCH THE REQUESTED PHYSICAL MOVEMENT

    private static final double SBG_CLOSED = 0.80;
    private static final double SBG_OPEN = 0.30;

    private static final double BBG_CLOSED = 0.62;
    private static final double BBG_OPEN = 0.90;

    // LIGHT COLORS
    private static final double LIGHT_RED = 0.280;
    private static final double LIGHT_GREEN = 0.500;
    private static final double LIGHT_YELLOW = 0.388;

    // DRIVE SPEED
    private static final double NORMAL_SPEED = 1.0;
    private static final double SLOW_SPEED = 0.35;

    // RAPID FIRE SETTINGS
    private static final double GATE_OPEN_TIME = 0.55;
    private static final double TRIGGER_THRESHOLD = 0.5;

    @Override
    public void runOpMode() {

        // HARDWARE MAPPING

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

        GateLight = hardwareMap.get(Servo.class, "GateLight");

        // MOTOR DIRECTIONS

        RF.setDirection(DcMotorSimple.Direction.FORWARD);
        RB.setDirection(DcMotorSimple.Direction.FORWARD);

        LF.setDirection(DcMotorSimple.Direction.REVERSE);
        LB.setDirection(DcMotorSimple.Direction.REVERSE);

        IN.setDirection(DcMotorSimple.Direction.FORWARD);

        SB.setDirection(DcMotorSimple.Direction.REVERSE);
        SM.setDirection(DcMotorSimple.Direction.FORWARD);

        FP.setDirection(DcMotorSimple.Direction.FORWARD);

        // ZERO POWER BEHAVIOR

        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        IN.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FP.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        SB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        SM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // INITIAL MOTOR POWERS

        RF.setPower(0);
        RB.setPower(0);
        LF.setPower(0);
        LB.setPower(0);

        IN.setPower(0);
        SB.setPower(0);
        SM.setPower(0);
        FP.setPower(0);

        // INITIAL STATES

        boolean shootersOn = false;
        boolean fpOn = false;

        boolean smallGateOpen = false;
        boolean bigGateOpen = false;

        boolean smallGatePulse = false;
        boolean bigGatePulse = false;

        // BUTTON EDGE DETECTION

        boolean previousA = false;
        boolean previousY = false;
        boolean previousX = false;
        boolean previousB = false;

        boolean previousRightTrigger = false;
        boolean previousLeftTrigger = false;

        // GATE TIMERS

        ElapsedTime smallGateTimer = new ElapsedTime();
        ElapsedTime bigGateTimer = new ElapsedTime();

        // =================================
        // INITIALIZE GATES CLOSED
        // =================================

        SBG.setPosition(SBG_CLOSED);
        BBG.setPosition(BBG_CLOSED);

        GateLight.setPosition(LIGHT_RED);

        telemetry.addLine("TWO DRIVER TELEOP");
        telemetry.addLine("GATES INITIALIZED CLOSED");
        telemetry.addLine("B = Small Gate Toggle");
        telemetry.addLine("Right Trigger = Small Gate Fire");
        telemetry.addLine("X = Big Gate Toggle");
        telemetry.addLine("Left Trigger = Big Gate Fire");
        telemetry.update();

        waitForStart();

        // MAIN LOOP

        while (opModeIsActive()) {

            // =================================
            // DRIVER 1: MECANUM DRIVE
            // =================================

            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            double rfPower = forward - strafe - rotate;
            double rbPower = forward + strafe - rotate;
            double lfPower = forward + strafe + rotate;
            double lbPower = forward - strafe + rotate;

            double maxPower = Math.max(
                    Math.max(Math.abs(rfPower), Math.abs(rbPower)),
                    Math.max(Math.abs(lfPower), Math.abs(lbPower))
            );

            if (maxPower > 1.0) {
                rfPower /= maxPower;
                rbPower /= maxPower;
                lfPower /= maxPower;
                lbPower /= maxPower;
            }

            double driveSpeed = gamepad1.right_bumper
                    ? SLOW_SPEED : NORMAL_SPEED;

            RF.setPower(rfPower * driveSpeed);
            RB.setPower(rbPower * driveSpeed);
            LF.setPower(lfPower * driveSpeed);
            LB.setPower(lbPower * driveSpeed);

            // =================================
            // INTAKE
            // =================================

            double intakePower = 0;

            if (gamepad2.right_bumper && !gamepad2.left_bumper) {
                intakePower = 1.0;
            } else if (gamepad2.left_bumper
                    && !gamepad2.right_bumper) {
                intakePower = -1.0;
            }

            IN.setPower(intakePower);

            // =================================
            // SHOOTERS
            // =================================

            boolean currentA = gamepad2.a;

            if (currentA && !previousA) {
                shootersOn = !shootersOn;
            }

            previousA = currentA;

            SB.setPower(shootersOn ? SB_POWER : 0);
            SM.setPower(shootersOn ? SM_POWER : 0);

            // =================================
            // FP MOTOR
            // =================================

            boolean currentY = gamepad2.y;

            if (currentY && !previousY) {
                fpOn = !fpOn;
            }

            previousY = currentY;

            FP.setPower(fpOn ? FP_POWER : 0);

            // =================================
            // SMALL GATE
            // B = MANUAL TOGGLE
            // RIGHT TRIGGER = OPEN THEN CLOSE
            // =================================

            boolean currentX = gamepad2.b;

            if (currentX && !previousX) {
                smallGateOpen = !smallGateOpen;
            }

            previousX = currentX;

            boolean currentRightTrigger =
                    gamepad2.right_trigger > TRIGGER_THRESHOLD;

            if (currentRightTrigger && !previousRightTrigger) {
                smallGatePulse = true;
                smallGateTimer.reset();
            }

            previousRightTrigger = currentRightTrigger;

            if (smallGatePulse) {

                if (smallGateTimer.seconds() < GATE_OPEN_TIME) {

                    SBG.setPosition(SBG_OPEN);

                } else {

                    SBG.setPosition(SBG_CLOSED);
                    smallGatePulse = false;
                }

            } else {

                SBG.setPosition(
                        smallGateOpen ? SBG_OPEN : SBG_CLOSED
                );
            }

            // =================================
            // BIG GATE
            // X = MANUAL TOGGLE
            // LEFT TRIGGER = OPEN THEN CLOSE
            // =================================

            boolean currentB = gamepad2.x;

            if (currentB && !previousB) {
                bigGateOpen = !bigGateOpen;
            }

            previousB = currentB;

            boolean currentLeftTrigger =
                    gamepad2.left_trigger > TRIGGER_THRESHOLD;

            if (currentLeftTrigger && !previousLeftTrigger) {
                bigGatePulse = true;
                bigGateTimer.reset();
            }

            previousLeftTrigger = currentLeftTrigger;

            if (bigGatePulse) {

                if (bigGateTimer.seconds() < GATE_OPEN_TIME) {

                    BBG.setPosition(BBG_OPEN);

                } else {

                    BBG.setPosition(BBG_CLOSED);
                    bigGatePulse = false;
                }

            } else {

                BBG.setPosition(
                        bigGateOpen ? BBG_OPEN : BBG_CLOSED
                );
            }

            // =================================
            // GATE STATUS LIGHT
            // =================================

            boolean smallGateActuallyOpen =
                    smallGatePulse ? smallGateTimer.seconds() < GATE_OPEN_TIME
                            : smallGateOpen;

            boolean bigGateActuallyOpen =
                    bigGatePulse ? bigGateTimer.seconds() < GATE_OPEN_TIME
                            : bigGateOpen;

            if (smallGateActuallyOpen && bigGateActuallyOpen) {

                GateLight.setPosition(LIGHT_GREEN);

            } else if (!smallGateActuallyOpen
                    && !bigGateActuallyOpen) {

                GateLight.setPosition(LIGHT_RED);

            } else {

                GateLight.setPosition(LIGHT_YELLOW);
            }

            // =================================
            // TELEMETRY
            // =================================

            telemetry.addLine("=== DRIVER 1 ===");

            telemetry.addData("Forward", "%.2f", forward);
            telemetry.addData("Strafe", "%.2f", strafe);
            telemetry.addData("Rotate", "%.2f", rotate);

            telemetry.addData("Drive Speed",
                    driveSpeed == SLOW_SPEED ? "SLOW" : "NORMAL");

            telemetry.addLine();
            telemetry.addLine("=== DRIVER 2 ===");

            telemetry.addData("Intake", "%.2f", intakePower);
            telemetry.addData("Shooters",
                    shootersOn ? "ON" : "OFF");
            telemetry.addData("FP",
                    fpOn ? "ON" : "OFF");

            telemetry.addLine();
            telemetry.addLine("=== GATES ===");

            telemetry.addData("Small Gate",
                    smallGateActuallyOpen ? "OPEN" : "CLOSED");

            telemetry.addData("Big Gate",
                    bigGateActuallyOpen ? "OPEN" : "CLOSED");

            telemetry.addData("Small Gate Pulse",
                    smallGatePulse ? "ACTIVE" : "INACTIVE");

            telemetry.addData("Big Gate Pulse",
                    bigGatePulse ? "ACTIVE" : "INACTIVE");

            telemetry.addData("Light Status",
                    smallGateActuallyOpen && bigGateActuallyOpen
                            ? "GREEN - BOTH OPEN"
                            : !smallGateActuallyOpen
                            && !bigGateActuallyOpen
                              ? "RED - BOTH CLOSED"
                              : "YELLOW - MIXED");

            telemetry.update();
        }

        // STOP ALL MOTORS

        RF.setPower(0);
        RB.setPower(0);
        LF.setPower(0);
        LB.setPower(0);

        IN.setPower(0);

        SB.setPower(0);
        SM.setPower(0);

        FP.setPower(0);
    }
}
