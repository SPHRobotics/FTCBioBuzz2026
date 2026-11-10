package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Main", group = "TeleOp")
public class Main extends LinearOpMode {

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

    // Individual shooter motor powers
    private static final double SB_POWER = 0.45;
    private static final double SM_POWER = 0.47;

    // FP full power
    private static final double FP_POWER = 1.0;

    // Small ball gate servo positions
    private static final double SBG_CLOSED = 0.30;
    private static final double SBG_OPEN = 0.80;

    // Big ball gate servo positions
    private static final double BBG_CLOSED = 0.90;
    private static final double BBG_OPEN = 0.62;

    @Override
    public void runOpMode() throws InterruptedException {

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

        // INITIAL SERVO POSITIONS: CLOSED
        SBG.setPosition(SBG_CLOSED);
        BBG.setPosition(BBG_CLOSED);

        // TOGGLE VARIABLES
        boolean shootersOn = false;
        boolean previousA = false;

        boolean smallGateOpen = false;
        boolean previousX = false;

        boolean bigGateOpen = false;
        boolean previousB = false;

        boolean fpOn = false;
        boolean previousY = false;

        telemetry.addLine("Main initialized");
        telemetry.addLine("Shooters: 45% power");
        telemetry.addLine("FP: Full power toggle on Y");
        telemetry.addLine("Waiting for start...");
        telemetry.update();

        waitForStart();

        // MAIN TELEOP LOOP
        while (opModeIsActive()) {

            // MECANUM DRIVE
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            double rfPower = forward - strafe - rotate;
            double rbPower = forward + strafe - rotate;
            double lfPower = forward + strafe + rotate;
            double lbPower = forward - strafe + rotate;

            // NORMALIZE MOTOR POWERS
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

            RF.setPower(rfPower);
            RB.setPower(rbPower);
            LF.setPower(lfPower);
            LB.setPower(lbPower);

            // INTAKE
            // Left bumper = forward
            // Right bumper = backward
            // Both/neither = stop
            double intakePower = 0.0;

            if (gamepad1.left_bumper && !gamepad1.right_bumper) {
                intakePower = 1.0;
            } else if (
                    gamepad1.right_bumper
                            && !gamepad1.left_bumper
            ) {
                intakePower = -1.0;
            }

            IN.setPower(intakePower);

            // SHOOTER TOGGLE
            // A = toggle both flywheels ON/OFF
            boolean currentA = gamepad1.a;

            if (currentA && !previousA) {
                shootersOn = !shootersOn;
            }

            previousA = currentA;

            double sbPower = shootersOn ? SB_POWER : 0.0;
            double smPower = shootersOn ? SM_POWER : 0.0;

            SB.setPower(sbPower);
            SM.setPower(smPower);

            // FP MOTOR TOGGLE
            // Y = toggle FP ON/OFF at full power
            boolean currentY = gamepad1.y;

            if (currentY && !previousY) {
                fpOn = !fpOn;
            }

            previousY = currentY;

            FP.setPower(fpOn ? FP_POWER : 0.0);

            // SMALL BALL GATE
            // X = toggle open/closed
            boolean currentX = gamepad1.x;

            if (currentX && !previousX) {
                smallGateOpen = !smallGateOpen;
            }

            previousX = currentX;

            SBG.setPosition(
                    smallGateOpen ? SBG_OPEN : SBG_CLOSED
            );

            // BIG BALL GATE
            // B = toggle open/closed
            boolean currentB = gamepad1.b;

            if (currentB && !previousB) {
                bigGateOpen = !bigGateOpen;
            }

            previousB = currentB;

            BBG.setPosition(
                    bigGateOpen ? BBG_OPEN : BBG_CLOSED
            );

            // TELEMETRY
            telemetry.addData("Forward", "%.2f", forward);
            telemetry.addData("Strafe", "%.2f", strafe);
            telemetry.addData("Rotate", "%.2f", rotate);

            telemetry.addLine();

            telemetry.addData("RF Power", "%.2f", rfPower);
            telemetry.addData("RB Power", "%.2f", rbPower);
            telemetry.addData("LF Power", "%.2f", lfPower);
            telemetry.addData("LB Power", "%.2f", lbPower);

            telemetry.addLine();

            telemetry.addData("Intake Power", "%.2f", intakePower);

            telemetry.addLine();

            telemetry.addData(
                    "Shooters",
                    shootersOn ? "ON" : "OFF"
            );
            telemetry.addData("SB Power", "%.2f", SB.getPower());
            telemetry.addData("SM Power", "%.2f", SM.getPower());

            telemetry.addLine();

            telemetry.addData("FP Motor", fpOn ? "ON" : "OFF");
            telemetry.addData("FP Power", "%.2f", FP.getPower());

            telemetry.addLine();

            telemetry.addData(
                    "Small Gate",
                    smallGateOpen ? "OPEN" : "CLOSED"
            );
            telemetry.addData(
                    "SBG Position",
                    "%.2f",
                    SBG.getPosition()
            );

            telemetry.addData(
                    "Big Gate",
                    bigGateOpen ? "OPEN" : "CLOSED"
            );
            telemetry.addData(
                    "BBG Position",
                    "%.2f",
                    BBG.getPosition()
            );

            telemetry.update();
        }

        // STOP ALL MOTORS WHEN OPMODE ENDS
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
