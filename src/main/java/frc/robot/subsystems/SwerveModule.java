package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Constants.ModuleConstants;
@SuppressWarnings("all")

public class SwerveModule {
    private TalonFX m_drivingKraken;
    private TalonFX m_turningFalcon;

    private final AnalogInput m_absoluteEncoder;//check the port number
    private final boolean m_turningEncoderReversed;//check if the encoder is reversed
    private final double m_absoluteEncoderOffsetRadians;//check the offset value

    private final CANcoder m_turningEncoder;

    PIDController m_drivePIDController = new PIDController(0.1, 0, 0);//Double check the PID values
    PIDController m_turnPIDController = new PIDController(0.1, 0, 0);//same as above

    private SwerveModuleState m_moduleCurrentState;//contain the speed and angle of the module
    private SwerveModuleState m_moduleDesiredState;//contain the speed and angle of the module

    public SwerveModule(int drivingKrakenID, int turningFalconID, int absoluteEncoderID, double absoluteEncoderOffsetRadians, boolean turningEncoderReversed){ 
        m_drivingKraken = new TalonFX(drivingKrakenID);
        m_turningFalcon = new TalonFX(turningFalconID);

        m_turningEncoder = new CANcoder(m_turningFalcon.getDeviceID());//check if the ID is same as the turningFalconID

        m_moduleCurrentState = new SwerveModuleState();
        m_moduleDesiredState = new SwerveModuleState();

        m_absoluteEncoder = new AnalogInput(absoluteEncoderID);
        m_absoluteEncoderOffsetRadians = absoluteEncoderOffsetRadians;
        m_turningEncoderReversed = turningEncoderReversed;

        var slot0Configs = new Slot0Configs();
        slot0Configs.kS = ModuleConstants.kDrivingFF; // Add 0.25 V output to overcome static friction
        slot0Configs.kP = ModuleConstants.kDrivingP; // A position error of 2.5 rotations results in 12 V output
        slot0Configs.kI = ModuleConstants.kDrivingI; // no output for integrated error
        slot0Configs.kD = ModuleConstants.kDrivingD; // A velocity error of 1 rps results in 0.1 V output

        var currentConfig = new CurrentLimitsConfigs();
        currentConfig.StatorCurrentLimit = frc.robot.Constants.ModuleConstants.kDrivingMotorCurrentLimit;
        currentConfig.StatorCurrentLimitEnable = true;

        m_drivingKraken.getConfigurator().apply(slot0Configs);
        m_drivingKraken.getConfigurator().apply(currentConfig);


        //following are the turning motor configurations
        var turningSlot0Configs = new Slot0Configs();
        slot0Configs.kS = ModuleConstants.kTurningFF; // Add 0.25 V output to overcome static friction
        slot0Configs.kP = ModuleConstants.kTurningP; // A position error of 2.5 rotations results in 12 V output
        slot0Configs.kI = ModuleConstants.kTurningI; // no output for integrated error
        slot0Configs.kD = ModuleConstants.kTurningD; // A velocity error of 1 rps results in 0.1 V output
  
      
        var turningCurrentConfig = new CurrentLimitsConfigs();
        currentConfig.StatorCurrentLimit = ModuleConstants.kTurningMotorCurrentLimit;
        currentConfig.StatorCurrentLimitEnable = true;
  
        m_turningFalcon.getConfigurator().apply(slot0Configs);
        m_turningFalcon.getConfigurator().apply(currentConfig);
    }

    public SwerveModuleState setDesiredState(SwerveModuleState newState){
        m_moduleDesiredState = newState;
        return m_moduleDesiredState;
    }
    
    public SwerveModuleState getSwerveModuleState(){
        return m_moduleCurrentState;
    }

    public void periodic(){

    }
}
