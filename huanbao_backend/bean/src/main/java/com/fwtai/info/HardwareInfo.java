package com.fwtai.info;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * 服务器相关信息
*/
public class HardwareInfo{

    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * CPU相关信息
    */
    private CpuInfo cpu = new CpuInfo();

    /**
     * 內存相关信息
    */
    private MemInfo mem = new MemInfo();

    /**
     * JVM相关信息
    */
    private JvmInfo jvm = new JvmInfo();

    /**
     * 服务器相关信息
    */
    private SysInfo sys = new SysInfo();

    public CpuInfo getCpu(){
        return cpu;
    }

    public void setCpu(CpuInfo cpu){
        this.cpu = cpu;
    }

    public MemInfo getMem(){
        return mem;
    }

    public void setMem(MemInfo mem){
        this.mem = mem;
    }

    public JvmInfo getJvm(){
        return jvm;
    }

    public void setJvm(JvmInfo jvm){
        this.jvm = jvm;
    }

    public SysInfo getSys(){
        return sys;
    }

    public void setSys(SysInfo sys){
        this.sys = sys;
    }

    public List<SysFileInfo> getSysFiles(){
        return sysFiles;
    }

    public void setSysFiles(final List<SysFileInfo> sysFiles){
        this.sysFiles = sysFiles;
    }

    /**
     * 磁盘相关信息
    */
    private List<SysFileInfo> sysFiles = new LinkedList<>();

    public void copyTo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        setCpuInfo(hal.getProcessor());
        setMemInfo(hal.getMemory());
        setSysInfo();
        setJvmInfo();
        setSysFiles(si.getOperatingSystem());
    }

    /**
     * 设置CPU信息
    */
    private void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        final long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        final long[] ticks = processor.getSystemCpuLoadTicks();
        final long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        final long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        final long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        final long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        final long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        final long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        final long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        final long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        final long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }

    /**
     * 设置内存信息
    */
    private void setMemInfo(GlobalMemory memory) {
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
    }

    /**
     * 设置服务器信息
    */
    private void setSysInfo() {
        Properties props = System.getProperties();
        sys.setComputerName(IpInfo.getHostName());
        sys.setComputerIp(NetUtil.getLocalhostStr());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 设置Java虚拟机
    */
    private void setJvmInfo() {
        final Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
    }

    /**
     * 设置磁盘信息
    */
    private void setSysFiles(final OperatingSystem os) {
        final FileSystem fileSystem = os.getFileSystem();
        final OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFileInfo sysFile = new SysFileInfo();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            if (total == 0) {
                sysFile.setUsage(0);
            } else {
                sysFile.setUsage(NumberUtil.mul(NumberUtil.div(used, total, 4), 100));
            }
            sysFiles.add(sysFile);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
    */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}