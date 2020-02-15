
package frc.robot;

import java.util.List;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;



public class Robot extends TimedRobot {

  WPI_TalonSRX talon0 = new WPI_TalonSRX(0);
  WPI_TalonSRX talon1 = new WPI_TalonSRX(1);
  WPI_TalonSRX talon2 = new WPI_TalonSRX(2);
  WPI_TalonSRX talon5 = new WPI_TalonSRX(5);
  CANSparkMax sparkmax = new CANSparkMax(3, MotorType.kBrushless);
  VictorSPX victor4 = new VictorSPX(4);

  Joystick controller = new Joystick(0);
  List<SpeedController> leftMotors;
  List<SpeedController> rightMotors;
  List<IMotorController> leftFollowers;
  List<IMotorController> rightFollowers;

  Timer stalltimer1 = new Timer();
  Timer endtimer1 = new Timer();
  boolean talon0Stalling = false;


  private NetworkTableEntry motorPowerSlider =  Shuffleboard.getTab("boxBot").add("Motor Power Slider", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", -1, "max", 1)).withPosition(1,0).withSize(5,1).getEntry();
  private NetworkTableEntry resetButton = Shuffleboard.getTab("boxBot").add("Reset", false).withWidget(BuiltInWidgets.kToggleButton).withPosition(0,0).withSize(1,3).getEntry();

  private NetworkTableEntry talon1Power = Shuffleboard.getTab("boxBot").add("Talon 1 Power" , 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -1, "max", 1)).withPosition(1,1).withSize(1,1).getEntry();
  private NetworkTableEntry talon2Power = Shuffleboard.getTab("boxBot").add("Talon 2 Power" , 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -1, "max", 1)).withPosition(2,1).withSize(1,1).getEntry();
  private NetworkTableEntry talon3Power = Shuffleboard.getTab("boxBot").add("Talon 3 Power" , 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -1, "max", 1)).withPosition(3,1).withSize(1,1).getEntry();
  private NetworkTableEntry victorPower = Shuffleboard.getTab("boxBot").add("Victor Power" , 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -1, "max", 1)).withPosition(4,1).withSize(1,1).getEntry();
  private NetworkTableEntry sparkPower = Shuffleboard.getTab("boxBot").add("Sparkmax Power", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -1, "max", 1)).withPosition(5,1).withSize(1,1).getEntry();  

  private NetworkTableEntry talon1Selected = Shuffleboard.getTab("boxBot").add("Talon 1 Selected", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(1,2).withSize(1,1).getEntry();
  private NetworkTableEntry talon2Selected = Shuffleboard.getTab("boxBot").add("Talon 2 Selected", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(2,2).withSize(1,1).getEntry();
  private NetworkTableEntry talon3Selected = Shuffleboard.getTab("boxBot").add("Talon 3 Selected", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(3,2).withSize(1,1).getEntry();
  private NetworkTableEntry victorSelected = Shuffleboard.getTab("boxBot").add("Victor Selected", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(4,2).withSize(1,1).getEntry();
  private NetworkTableEntry sparkSelected = Shuffleboard.getTab("boxBot").add("Sparkmax Selected", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(5,2).withSize(1,1).getEntry();

  private NetworkTableEntry talon1Override = Shuffleboard.getTab("boxBot").add("Talon 1 Override", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(6,0).withSize(1,1).getEntry();   
  private NetworkTableEntry talon2Override = Shuffleboard.getTab("boxBot").add("Talon 2 Override", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(6,1).withSize(1,1).getEntry();   
  private NetworkTableEntry talon3Override = Shuffleboard.getTab("boxBot").add("Talon 3 Override", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(6,2).withSize(1,1).getEntry();   
  private NetworkTableEntry victorOverride = Shuffleboard.getTab("boxBot").add("Victor Override", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(6,3).withSize(1,1).getEntry();   
  private NetworkTableEntry sparkOverride = Shuffleboard.getTab("boxBot").add("Sparkmax Override", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(6,4).withSize(1,1).getEntry();

  private NetworkTableEntry talon1OverridePower = Shuffleboard.getTab("boxBot").add("Talon 1 Override Power", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", -1, "max", 1)).withPosition(7,0).withSize(3,1).getEntry();
  private NetworkTableEntry talon2OverridePower = Shuffleboard.getTab("boxBot").add("Talon 2 Override Power", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", -1, "max", 1)).withPosition(7,1).withSize(3,1).getEntry();
  private NetworkTableEntry talon3OverridePower = Shuffleboard.getTab("boxBot").add("Talon 3 Override Power", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", -1, "max", 1)).withPosition(7,2).withSize(3,1).getEntry();
  private NetworkTableEntry victorOverridePower = Shuffleboard.getTab("boxBot").add("Victor Override Power", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", -1, "max", 1)).withPosition(7,3).withSize(3,1).getEntry();
  private NetworkTableEntry sparkOverridePower = Shuffleboard.getTab("boxBot").add("Sparkmax Override Power", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", -1, "max", 1)).withPosition(7,4).withSize(3,1).getEntry();


  //controller robot tab

  private NetworkTableEntry talon1Left = Shuffleboard.getTab("boxBotDrive").add("Talon 1 Left", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(1,1).withSize(1,1).getEntry();
  private NetworkTableEntry talon1Right = Shuffleboard.getTab("boxBotDrive").add("Talon 1 Right", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(1,2).withSize(1,1).getEntry();
  private NetworkTableEntry talon2Left = Shuffleboard.getTab("boxBotDrive").add("Talon 2 Left", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(2,1).withSize(1,1).getEntry();
  private NetworkTableEntry talon2Right = Shuffleboard.getTab("boxBotDrive").add("Talon 2 Right", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(2,2).withSize(1,1).getEntry();
  private NetworkTableEntry talon3Left = Shuffleboard.getTab("boxBotDrive").add("Talon 3 Left", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(3,1).withSize(1,1).getEntry();
  private NetworkTableEntry talon3Right = Shuffleboard.getTab("boxBotDrive").add("Talon 3 Right", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(3,2).withSize(1,1).getEntry();
  private NetworkTableEntry victorLeft = Shuffleboard.getTab("boxBotDrive").add("Victor Left", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(4,1).withSize(1,1).getEntry();
  private NetworkTableEntry victorRight = Shuffleboard.getTab("boxBotDrive").add("Victor Right", false).withWidget(BuiltInWidgets.kToggleSwitch).withPosition(4,2).withSize(1,1).getEntry();
  private NetworkTableEntry twofourSelector = Shuffleboard.getTab("boxBotDrive").add("Select Motor Amount", 0).withPosition(3,1).withSize(2,1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min",0,"max",4,"block increment", 2)).getEntry();



  @Override
  public void robotInit() {
    
  }
  @Override
  public void teleopPeriodic() {

    if(resetButton.getBoolean(false) == true) { 
      motorPowerSlider.setDouble(0);
      talon1Power.setDouble(0);
      talon2Power.setDouble(0);
      talon3Power.setDouble(0);
      victorPower.setDouble(0);
      sparkPower.setDouble(0);
      talon1Selected.setBoolean(false);
      talon2Selected.setBoolean(false);
      talon3Selected.setBoolean(false);
      victorSelected.setBoolean(false);
      sparkSelected.setBoolean(false);
      talon1Override.setBoolean(false);
      talon2Override.setBoolean(false);
      talon3Override.setBoolean(false);
      victorOverride.setBoolean(false);
      sparkOverride.setBoolean(false);
      talon1OverridePower.setDouble(0);
      talon2OverridePower.setDouble(0);
      talon3OverridePower.setDouble(0);
      victorOverridePower.setDouble(0);
      sparkOverridePower.setDouble(0);
      
      resetButton.setBoolean(false);
      
      talon0.set(0);
      talon1.set(0);
      talon2.set(0);
      victor4.set(ControlMode.PercentOutput, 0);
      sparkmax.set(0);

      
    }
    //if override false -> if selected -> power | | else override true -> override power        x4
    
    if (talon1Override.getBoolean(false) == false) {
      if (talon1Selected.getBoolean(false) == true) {

        if (talon0Stalling == false) {talon0.set(motorPowerSlider.getDouble(0.0));}
        if (talon0Stalling == true) {talon0.set(-1 * motorPowerSlider.getDouble(0.0));}

        if (Math.abs(talon0.getStatorCurrent()) > 30) { 
          if (stalltimer1.get() == 0) {
            stalltimer1.start();
          }
          endtimer1.reset();
         }
        if (Math.abs(talon0.getStatorCurrent()) < 30) {
          if (endtimer1.get() == 0) {
            endtimer1.start();
          }   
        }

        if (stalltimer1.get() > 1.5) {
          stalltimer1.reset();
          talon0Stalling = true;
        }

        if (endtimer1.get() > 1) {
          endtimer1.reset();
          stalltimer1.reset();
          talon0Stalling = false;
        }

        System.out.println(talon0.getStatorCurrent());
      
      }
      else {
        talon0.set(0);
      }
    }
    else {
        talon0.set(talon1OverridePower.getDouble(0));
    }
    talon1Power.setDouble(talon0.get());

    if (talon2Override.getBoolean(false) == false) {
      if (talon2Selected.getBoolean(false) == true) {
        talon1.set(motorPowerSlider.getDouble(0.0));
      }
      else {
        talon1.set(0);
      }
    }
    else {
        talon1.set(talon2OverridePower.getDouble(0));
    }
    talon2Power.setDouble(talon1.get());

    if (talon3Override.getBoolean(false) == false) {
      if (talon3Selected.getBoolean(false) == true) {
        talon2.set(motorPowerSlider.getDouble(0.0));
      }
      else {
        talon2.set(0);
      }
    }
    else {
        talon2.set(talon3OverridePower.getDouble(0));
    }
    talon3Power.setDouble(talon2.get());

    if (victorOverride.getBoolean(false) == false) {
      if (victorSelected.getBoolean(false) == true) {        
        victor4.set(ControlMode.PercentOutput, motorPowerSlider.getDouble(0.0));
      }
      else {
        victor4.set(ControlMode.PercentOutput, 0);
      }
    }
    else {
        victor4.set(ControlMode.PercentOutput, talon1OverridePower.getDouble(0));
    }
    victorPower.setDouble(victor4.getMotorOutputPercent());

    if (sparkOverride.getBoolean(false) == false) {
      if (sparkSelected.getBoolean(false) == true) {
        sparkmax.set(motorPowerSlider.getDouble(0.0));
      }
      else {
        sparkmax.set(0);
      }
    }
    else {
      sparkmax.set(sparkOverridePower.getDouble(0));
    }
    sparkPower.setDouble(sparkmax.get());



    
  
    
    if (twofourSelector.getDouble(0) > 0) {
      
      
      if (talon1Left.getBoolean(false) == true) { leftMotors.add(talon0); leftFollowers.add(talon0); }
      if (talon1Right.getBoolean(false) == true) { rightMotors.add(talon0); rightFollowers.add(talon0); }

      if (talon2Left.getBoolean(false) == true) { leftMotors.add(talon1); leftFollowers.add(talon1); }
      if (talon2Right.getBoolean(false) == true) { rightMotors.add(talon1); rightFollowers.add(talon1); }

      if (talon3Left.getBoolean(false) == true) { leftMotors.add(talon2); leftFollowers.add(talon2); }
      if (talon3Right.getBoolean(false) == true) { rightMotors.add(talon2); rightFollowers.add(talon2); }

      if (victorLeft.getBoolean(false) == true) {  leftFollowers.add(victor4); }
      if (victorRight.getBoolean(false) == true) { rightFollowers.add(victor4); }

      CustomDifferentialDrive(twofourSelector.getDouble(0), leftMotors, rightMotors, leftFollowers, rightFollowers).arcadeDrive(controller.getRawAxis(0), controller.getRawAxis(1));

    }

    

  }

  private DifferentialDrive CustomDifferentialDrive(double motoramount,List<SpeedController> leftmotors, List<SpeedController> rightmotors, List<IMotorController> leftfollowers, List<IMotorController> rightfollowers) {

    
    DifferentialDrive customDrive = new DifferentialDrive(leftmotors.get(0), rightmotors.get(0));
    if (motoramount == 4) {
      leftfollowers.get(0).follow(leftfollowers.get(0));
      rightfollowers.get(0).follow(rightfollowers.get(0));
    }
    else {
      talon0.follow(talon0);
      talon1.follow(talon1);
      talon2.follow(talon2);
      talon5.follow(talon5);
      victor4.follow(victor4);
    }
    return customDrive;
  }
    
  
}
