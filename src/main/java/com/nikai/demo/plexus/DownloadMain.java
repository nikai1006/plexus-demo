package com.nikai.demo.plexus;

import com.nikai.demo.plexus.util.SystemUtil;
import java.io.File;
import java.util.ArrayList;
import org.apache.maven.archetype.downloader.Downloader;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.sonatype.aether.impl.internal.SimpleLocalRepositoryManager;

/**
 * plexus-demo com.nikai.demo.plexus
 *
 * @author: nikai
 * @Description:
 * @Date: Create in 23:00 2019/8/14
 * @Modified By:
 */
public class DownloadMain {

    public static void main(String[] args) throws Exception {
        PlexusContainer plexusContainer = new DefaultPlexusContainer();
        plexusContainer.dispose();
        Downloader downloader = plexusContainer.lookup(Downloader.class);
        MavenArtifactRepository remoteRepo = new MavenArtifactRepository("my-nexus",
            "http://nikai.net.cn/content/groups/public/", new DefaultRepositoryLayout(), new ArtifactRepositoryPolicy(),
            new ArtifactRepositoryPolicy());
        ArtifactRepository localRepository = new DefaultArtifactRepository("local",
            new File(SystemUtil.catchLocalRepository()).toURI().toString(), new DefaultRepositoryLayout());

        ArrayList<ArtifactRepository> remoteRepositories = new ArrayList<ArtifactRepository>();
        remoteRepositories.add(remoteRepo);

        DefaultProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest();
        buildingRequest.setRemoteRepositories(remoteRepositories);
        MavenRepositorySystemSession repositorySession = new MavenRepositorySystemSession();
        repositorySession.setLocalRepositoryManager(new SimpleLocalRepositoryManager(localRepository.getBasedir()));
        buildingRequest.setRepositorySession(repositorySession);
        File file = downloader.download("com.tomtom.speedtools", "guice", "3.2.17",
            remoteRepo, localRepository,
            remoteRepositories, buildingRequest);
        System.out.println(file.getAbsolutePath());
    }

}
