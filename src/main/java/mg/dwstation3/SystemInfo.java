package mg.dwstation3;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by micha on 17.02.2018.
 */

public class SystemInfo {
    private Date date;
    private String CPU_TEMP;
    private String FREE_RAM;
    private String FREE_ROM;
    private String TOTAL_ROM;
    private String OS_VERSION;

    public SystemInfo(String CPU_TEMP, String FREE_RAM, String FREE_ROM, String TOTAL_ROM, String OS_VERSION) {
        date = Calendar.getInstance().getTime();
        this.CPU_TEMP = CPU_TEMP;
        this.FREE_RAM = FREE_RAM;
        this.FREE_ROM = FREE_ROM;
        this.TOTAL_ROM = TOTAL_ROM;
        this.OS_VERSION = OS_VERSION;
    }

    public String getCPU_TEMP() {
        return CPU_TEMP;
    }

    public String getFREE_RAM() {
        return FREE_RAM;
    }

    public String getFREE_ROM() {
        return FREE_ROM;
    }

    public String getTOTAL_ROM() {
        return TOTAL_ROM;
    }

    public String getOS_VERSION() {
        return OS_VERSION;
    }

    public Date getDate() {
        return date;
    }

}
