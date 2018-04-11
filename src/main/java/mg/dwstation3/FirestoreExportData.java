package mg.dwstation3;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

/**
 * Created by micha on 23.02.2018.
 */

public class FirestoreExportData {
    private Date date;
    private String CPU_TEMP;
    private String FREE_RAM;
    private String FREE_ROM;
    private String TOTAL_ROM;
    private String OS_VERSION;
    private String PM1_0;
    private String PM_25;
    private String PM_10;
    private String TEMPERATURE;
    private String HUMIDITY;
    private String PRESSURE;

    public FirestoreExportData() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCPU_TEMP() {
        return CPU_TEMP;
    }

    public void setCPU_TEMP(String CPU_TEMP) {
        this.CPU_TEMP = CPU_TEMP;
    }

    public String getFREE_RAM() {
        return FREE_RAM;
    }

    public void setFREE_RAM(String FREE_RAM) {
        this.FREE_RAM = FREE_RAM;
    }

    public String getFREE_ROM() {
        return FREE_ROM;
    }

    public void setFREE_ROM(String FREE_ROM) {
        this.FREE_ROM = FREE_ROM;
    }

    public String getTOTAL_ROM() {
        return TOTAL_ROM;
    }

    public void setTOTAL_ROM(String TOTAL_ROM) {
        this.TOTAL_ROM = TOTAL_ROM;
    }

    public String getOS_VERSION() {
        return OS_VERSION;
    }

    public void setOS_VERSION(String OS_VERSION) {
        this.OS_VERSION = OS_VERSION;
    }

    public String getPM1_0() {return PM1_0; }

    public void setPM1_0(String PM1_0) { this.PM1_0 = PM1_0; }

    public String getPM_25() {
        return PM_25;
    }

    public void setPM_25(String PM_25) {
        this.PM_25 = PM_25;
    }

    public String getPM_10() {
        return PM_10;
    }

    public void setPM_10(String PM_10) {
        this.PM_10 = PM_10;
    }

    public String getTEMPERATURE() {
        return TEMPERATURE;
    }

    public void setTEMPERATURE(String TEMPERATURE) {
        this.TEMPERATURE = TEMPERATURE;
    }

    public String getHUMIDITY() {
        return HUMIDITY;
    }

    public void setHUMIDITY(String HUMIDITY) {
        this.HUMIDITY = HUMIDITY;
    }

    public String getPRESSURE() {
        return PRESSURE;
    }

    public void setPRESSURE(String PRESSURE) {
        this.PRESSURE = PRESSURE;
    }
}
