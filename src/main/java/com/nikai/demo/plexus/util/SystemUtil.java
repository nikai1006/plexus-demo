package com.nikai.demo.plexus.util;

import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class SystemUtil {
    public static final String DEFAULT_DOWNLOAD_DIR = System.getProperty("user.home") + "/.download";

    public static final File DEFAULT_M2_REPO = new File(System.getProperty("user.home"), "/.m2/repository");
    public static final String M2_HOME = "MAVEN_HOME";

    /**
     *
     * @return
     */
    public static String catchLocalRepository() {


        String maven_home = System.getenv(M2_HOME);
        try {
            String mavenRepo = readFromMavenHome(maven_home);
            return StringUtils.isEmpty(maven_home) ? DEFAULT_M2_REPO.getAbsolutePath()
                : (StringUtils.isNotEmpty(mavenRepo) ? mavenRepo
                    : DEFAULT_M2_REPO.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_M2_REPO.getAbsolutePath();
    }

    private static String readFromMavenHome(String maven_home) throws Exception {
        File mavenHome = new File(maven_home);
        if (mavenHome.exists()) {
            File settings = new File(mavenHome, "/conf/settings.xml");
            if (settings.exists()) {
                Element localRepository = DomUtil.readDom(settings).getRootElement().element("localRepository");
                if (null != localRepository) {
                    return localRepository.getTextTrim();
                }
            }
        }

        return null;
    }


}
