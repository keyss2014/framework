package cn.keyss.common.utilities;

import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public abstract class PathHelper {
    /**
     * 缺省根目录
     */
    private static String defaultBaseDirectory;

    /**
     * 初始化块
     */
    static {
        try {
            URL url = ClassUtils.getDefaultClassLoader().getResource("");
            if (url != null){
                File file = new File(url.getPath());
                defaultBaseDirectory = file.getCanonicalFile().getPath();
            }else {
                //可执行jar包的获取方式
                url = PathHelper.class.getProtectionDomain().getCodeSource().getLocation();
                File file = new File(url.getPath());
                defaultBaseDirectory =  file.getParent();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取应用程序路径
     *
     * @param paths 路径
     * @return 格式化后的路径
     */
    public static String getApplicationPath(String... paths) {
        File result = new File(paths[paths.length - 1]);

        if (!result.isAbsolute()) {
            for (int i = paths.length - 2; i > -1; i--) {
                if (paths[i]!=null && !paths[i].trim().equals("")) {
                    result = new File(paths[i], result.getPath());
                    if (result.isAbsolute()) {
                        break;
                    }
                }
            }
        }
        if (!result.isAbsolute()) {
            result = new File(defaultBaseDirectory, result.getPath());

        }
        return result.getPath();
    }

    public static void setDefaultBaseDirectory(String defaultBaseDirectory){
        PathHelper.defaultBaseDirectory = defaultBaseDirectory;
    }

    public static String getDefaultBaseDirectory(){
        return PathHelper.defaultBaseDirectory;
    }

    private static boolean probePathInParent(String probeFilePath){
        //检验当前路径下是否有探测文件
        File probeFile = new File(defaultBaseDirectory,probeFilePath);
        System.out.println("探测路径：" + probeFile);
        if (probeFile.exists())
            return true;
        //探测上级目录
        File currBaseFile = new File(defaultBaseDirectory);
        System.out.println("探测路径：" + new File(currBaseFile.getParent(), probeFilePath));
        if (new File(currBaseFile.getParent(),probeFilePath).exists()) {
            defaultBaseDirectory = currBaseFile.getParent();
            return true;
        }
        return false;
    }

    /**
     * 探测传入的配置文件，设置baseDir
     * @param probeFilePath
     */
    public static boolean probePath(String probeFilePath){
        if (probePathInParent(probeFilePath))
            return true;
        //探测文件不存在，尝试使用当前jar包定位
        File jarFile = new File(PathHelper.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        if (jarFile.exists() && jarFile.getParentFile().exists()) {
            defaultBaseDirectory = jarFile.getParent();
            return probePathInParent(probeFilePath);
        }
        return false;
    }
}