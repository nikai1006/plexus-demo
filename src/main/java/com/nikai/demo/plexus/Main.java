package com.nikai.demo.plexus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import org.apache.maven.archetype.ArchetypeGenerationRequest;
import org.apache.maven.archetype.ArchetypeManager;
import org.apache.maven.archetype.generator.ArchetypeGenerator;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.sonatype.aether.impl.internal.DefaultRemoteRepositoryManager;
import org.sonatype.aether.impl.internal.EnhancedLocalRepositoryManager;
import org.sonatype.aether.impl.internal.SimpleLocalRepositoryManager;
import org.sonatype.aether.util.DefaultRepositorySystemSession;
import org.sonatype.aether.util.listener.AbstractTransferListener;

public class Main {

    // archetypes prepared by antrun execution (see pom.xml) from src/test/archetypes
    private final static Archetype ARCHETYPE_BASIC = new Archetype("archetypes", "basic", "1.0");

    private final static Archetype ARCHETYPE_PARTIAL = new Archetype("archetypes", "partial", "1.0");

    private final static Archetype ARCHETYPE_SITE = new Archetype("archetypes", "site", "1.0");

    private final static Archetype ARCHETYPE_FILESET = new Archetype("archetypes", "fileset", "1.0");

    private final static Archetype ARCHETYPE_OLD = new Archetype("archetypes", "old", "1.0");

    private final static Archetype ARCHETYPE_FILESET_WITH_POSTCREATE_SCRIPT =
        new Archetype("archetypes", "fileset_with_postscript", "1.0");

    public static final Archetype NIKAI_ARCHETYPE = new Archetype("cn.net.nikai.archetype", "spring-boot-archetype",
        "1.0.0-SNAPSHOT");

    private final static Properties ADDITIONAL_PROPERTIES = new Properties();

    static {
        ADDITIONAL_PROPERTIES.setProperty("property-without-default-1", "file-value");
        ADDITIONAL_PROPERTIES.setProperty("property-without-default-2", "file-value");
        ADDITIONAL_PROPERTIES.setProperty("property-without-default-3", "file-value");
        ADDITIONAL_PROPERTIES.setProperty("property-without-default-4", "file-value");
        ADDITIONAL_PROPERTIES.setProperty("property-with-default-1", "file-value");
        ADDITIONAL_PROPERTIES.setProperty("property-with-default-2", "file-value");
        ADDITIONAL_PROPERTIES.setProperty("property-with-default-3", "file-value");
        ADDITIONAL_PROPERTIES.setProperty("property-with-default-4", "file-value");
        ADDITIONAL_PROPERTIES.setProperty("property_underscored_1", "prop1");
        ADDITIONAL_PROPERTIES.setProperty("property_underscored-2", "prop2");
    }

    ArtifactRepository localRepository;

    String remoteRepository;

    ArchetypeGenerator generator;

    String outputDirectory;

    File projectDirectory;

    public static void main(String[] args) throws Exception {
        PlexusContainer plexusContainer = new DefaultPlexusContainer();
        plexusContainer.dispose();
        UserService userService =
            (UserService) plexusContainer.lookup(UserService.ROLE, "nikai");
        System.out.println(userService.queryUserNanme(1));

//        Archetype archetype = new Archetype();
//        archetype.groupId("cn.net.nikai.archetype");
//        archetype.setArtifactId("spring-boot-archetype");
//        archetype.setVersion("1.0.0-SNAPSHOT");
//        archetype.setRepository("http://192.168.0.99:8081/nexus/content/groups/public");

        ArchetypeGenerationRequest request = new Main().createArchetypeGenerationRequest("hello", NIKAI_ARCHETYPE);
//            new ArchetypeGenerationRequest(archetype);
//        DefaultProjectBuildingRequest projectBuildingRequest = new DefaultProjectBuildingRequest();
//        projectBuildingRequest.setBuildStartTime(new Date());
//        ArrayList<ArtifactRepository> remoteRepositories = new ArrayList<ArtifactRepository>();
//        remoteRepositories
//            .add(new MavenArtifactRepository("releases", "http://192.168.0.99:8081/nexus/content/groups/public",
//                new DefaultRepositoryLayout(), new ArtifactRepositoryPolicy(), new ArtifactRepositoryPolicy()));
//        projectBuildingRequest.setRemoteRepositories(remoteRepositories);
//        request.setGroupId("com.nikai.archetype").
//            setArtifactId("demo").setPackage("com.nikai.archetype.demo").setVersion("1.0.0").setProjectBuildingRequest(
//            projectBuildingRequest);

        ArchetypeManager archetypeManager = (ArchetypeManager) plexusContainer.lookup(ArchetypeManager.ROLE);
        archetypeManager.generateProjectFromArchetype(request);
    }


    private ArchetypeGenerationRequest createArchetypeGenerationRequest(String project, Archetype archetype) {
        String repositories = new File("D:\\tools\\package\\repo").toURI().toString();

        localRepository =
            new DefaultArtifactRepository("local", repositories, new DefaultRepositoryLayout());

        remoteRepository = "http://192.168.0.99:8081/nexus/content/groups/public";
//            repositories + "/central";
        outputDirectory = getBasedir() + "/target/projects/" + project;

        projectDirectory = new File(outputDirectory, "demo");

        ArchetypeGenerationRequest request = new ArchetypeGenerationRequest();
        request.setLocalRepository(localRepository);
        request.setArchetypeRepository(remoteRepository);
        request.setOutputDirectory(outputDirectory);

        request.setArchetypeGroupId(archetype.groupId);
        request.setArchetypeArtifactId(archetype.artifactId);
        request.setArchetypeVersion(archetype.version);

        request.setGroupId("com.nikai.archetype");
        request.setArtifactId("demo");
        request.setVersion("1.0.0");
        request.setPackage("com.nikai.archetype.demo");

        request.setProperties(ADDITIONAL_PROPERTIES);

        ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest();
//        buildingRequest.setBuildStartTime(new Date());
        ArrayList<ArtifactRepository> remoteRepositories = new ArrayList<ArtifactRepository>();
        remoteRepositories
            .add(new MavenArtifactRepository("oeasy-nexus", "http://192.168.0.99:8081/nexus/content/groups/public",
                new DefaultRepositoryLayout(), new ArtifactRepositoryPolicy(), new ArtifactRepositoryPolicy()));
        buildingRequest.setRemoteRepositories(remoteRepositories);
//        buildingRequest.setLocalRepository(localRepository);

        MavenRepositorySystemSession repositorySession = new MavenRepositorySystemSession();
        repositorySession.setLocalRepositoryManager(new SimpleLocalRepositoryManager(localRepository.getBasedir()));
        buildingRequest.setRepositorySession(repositorySession);

        request.setProjectBuildingRequest(buildingRequest);


        return request;
    }

    private static class Archetype {

        public final String groupId;
        public final String artifactId;
        public final String version;

        public Archetype(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }
    }

    public static String getBasedir() {
        String basedir = null;
        if (basedir != null) {
            return basedir;
        }

        basedir = System.getProperty("basedir");

        if (basedir == null) {
            basedir = new File("").getAbsolutePath();
        }

        return basedir;
    }

}
