/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2014 Alex Buloichik
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.core.team2;

import gen.core.project.RepositoryDefinition;
import gen.core.project.RepositoryMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.omegat.core.data.ProjectProperties;
import org.omegat.util.FileUtil;

public class RemoteRepositoryProviderTest extends TestCase {
    String V;
    String VR;

    List<RepositoryDefinition> repos;
    List<String> files;
    VirtualRemoteRepositoryProvider provider;

    List<String> copyFrom = new ArrayList<String>();
    List<String> copyTo = new ArrayList<String>();
    int copyCheckedIndex;

    @Override
    protected void setUp() throws Exception {
        File dir = new File("build/testdata/repotest");
        FileUtil.deleteTree(dir);
        dir.mkdirs();
        V = dir.getAbsolutePath() + "/";
        VR = dir.getAbsolutePath() + "/.repositories/url/";

        repos = new ArrayList<RepositoryDefinition>();
        provider = new VirtualRemoteRepositoryProvider(repos);
        files = new ArrayList<String>();
    }

    void filesLocal() throws IOException {
        addFile(V + "dir/localfile");
        addFile(V + "dir/local/1.txt");
        addFile(V + "dir/local/1.txt.bak");
        addFile(V + "dir/local/1.jpg");
        addFile(V + "dir/local/2.xml");
        addFile(V + "dir/local/subdir/3.png");
        addFile(V + "otherdir/local/4.file");
    }

    void filesRemote() throws IOException {
        addFile(VR + "remotefile");
        addFile(VR + "remote/1.txt");
        addFile(VR + "remote/1.txt.bak");
        addFile(VR + "remote/1.jpg");
        addFile(VR + "remote/2.xml");
        addFile(VR + "remote/subdir/3.png");
        addFile(VR + "otherremote/4.file");
    }

    void mapping1() {
        addRepo("dir/localfile", "remotefile");
        // bak should be excluded, but png - no
        addRepo("dir/local/", "remote/", "/*.bak", "/*.png", "/1.jpg");
    }

    void mapping2() {
        addRepo("", "", "**/*.bak", "/*.png", "/dir/local/1.jpg", "/remote/1.jpg");
    }

    void mapping3() {
        addRepo("dir/", "", "**/*.bak", "/*.png", "/local/1.jpg", "/remote/1.jpg");
    }

    void mapping4() {
        addRepo("", "remote/", "**/*.bak", "/*.png", "/dir/local/1.jpg", "/1.jpg");
    }

    public void testNames() throws Exception {
        try {
            provider.copyFilesFromRepoToProject("/dir");
            fail();
        } catch (RuntimeException ex) {
        }
        provider.copyFilesFromRepoToProject("dir/");
        provider.copyFilesFromRepoToProject("file");
        try {
            provider.copyFilesFromProjectToRepo("/dir", null);
            fail();
        } catch (RuntimeException ex) {
        }
        provider.copyFilesFromProjectToRepo("dir/", null);
        provider.copyFilesFromProjectToRepo("file", null);
    }

    public void testCopyFilesFromRepoToProject11() throws Exception {
        filesRemote();
        mapping1();
        provider.copyFilesFromRepoToProject("dir/localfile");
        checkCopy(VR + "remotefile", V + "dir/localfile");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject12() throws Exception {
        filesRemote();
        mapping1();
        provider.copyFilesFromRepoToProject("dir/local/1.txt");
        checkCopy(VR + "remote/1.txt", V + "dir/local/1.txt");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject13() throws Exception {
        filesRemote();
        mapping1();
        provider.copyFilesFromRepoToProject("dir/");
        checkCopy(VR + "remotefile", V + "dir/localfile");
        checkCopy(VR + "remote/1.txt", V + "dir/local/1.txt");
        checkCopy(VR + "remote/2.xml", V + "dir/local/2.xml");
        checkCopy(VR + "remote/subdir/3.png", V + "dir/local/subdir/3.png");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject14() throws Exception {
        filesRemote();
        mapping1();
        provider.copyFilesFromRepoToProject("dir/local/subdir/");
        checkCopy(VR + "remote/subdir/3.png", V + "dir/local/subdir/3.png");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject15() throws Exception {
        filesRemote();
        mapping1();
        provider.copyFilesFromRepoToProject("dir/lo");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject16() throws Exception {
        filesRemote();
        mapping1();
        provider.copyFilesFromRepoToProject("dir/lo/");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject17() throws Exception {
        filesRemote();
        mapping1();
        provider.copyFilesFromRepoToProject("");
        checkCopy(VR + "remotefile", V + "dir/localfile");
        checkCopy(VR + "remote/1.txt", V + "dir/local/1.txt");
        checkCopy(VR + "remote/2.xml", V + "dir/local/2.xml");
        checkCopy(VR + "remote/subdir/3.png", V + "dir/local/subdir/3.png");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject21() throws Exception {
        filesRemote();
        mapping2();
        provider.copyFilesFromRepoToProject("");
        checkCopy(VR + "otherremote/4.file", V + "otherremote/4.file");
        checkCopy(VR + "remote/1.txt", V + "remote/1.txt");
        checkCopy(VR + "remote/2.xml", V + "remote/2.xml");
        checkCopy(VR + "remote/subdir/3.png", V + "remote/subdir/3.png");
        checkCopy(VR + "remotefile", V + "remotefile");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject22() throws Exception {
        filesRemote();
        mapping2();
        provider.copyFilesFromRepoToProject("otherremote/4.file");
        checkCopy(VR + "otherremote/4.file", V + "otherremote/4.file");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject31() throws Exception {
        filesRemote();
        mapping3();
        provider.copyFilesFromRepoToProject("");
        checkCopy(VR + "otherremote/4.file", V + "dir/otherremote/4.file");
        checkCopy(VR + "remote/1.txt", V + "dir/remote/1.txt");
        checkCopy(VR + "remote/2.xml", V + "dir/remote/2.xml");
        checkCopy(VR + "remote/subdir/3.png", V + "dir/remote/subdir/3.png");
        checkCopy(VR + "remotefile", V + "dir/remotefile");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject41() throws Exception {
        filesRemote();
        mapping4();
        provider.copyFilesFromRepoToProject("");
        checkCopy(VR + "remote/1.txt", V + "1.txt");
        checkCopy(VR + "remote/2.xml", V + "2.xml");
        checkCopy(VR + "remote/subdir/3.png", V + "subdir/3.png");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject51() throws Exception {
        filesRemote();
        mapping4();
        provider.copyFilesFromRepoToProject("", "/1.txt");
        checkCopy(VR + "remote/2.xml", V + "2.xml");
        checkCopy(VR + "remote/subdir/3.png", V + "subdir/3.png");
        checkCopyEnd();
    }

    public void testCopyFilesFromRepoToProject52() throws Exception {
        filesRemote();
        mapping1();
        provider.copyFilesFromRepoToProject("", "/**/localfile");
        checkCopy(VR + "remote/1.txt", V + "dir/local/1.txt");
        checkCopy(VR + "remote/2.xml", V + "dir/local/2.xml");
        checkCopy(VR + "remote/subdir/3.png", V + "dir/local/subdir/3.png");
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo11() throws Exception {
        filesLocal();
        mapping1();
        provider.copyFilesFromProjectToRepo("dir/localfile", null);
        checkCopy(V + "dir/localfile", VR + "remotefile");
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo12() throws Exception {
        filesLocal();
        mapping1();
        provider.copyFilesFromProjectToRepo("dir/local/1.txt", null);
        checkCopy(V + "dir/local/1.txt", VR + "remote/1.txt");
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo13() throws Exception {
        filesLocal();
        mapping1();
        provider.copyFilesFromProjectToRepo("dir/", null);
        checkCopy(V + "dir/localfile", VR + "remotefile");
        checkCopy(V + "dir/local/1.txt", VR + "remote/1.txt");
        checkCopy(V + "dir/local/2.xml", VR + "remote/2.xml");
        checkCopy(V + "dir/local/subdir/3.png", VR + "remote/subdir/3.png");
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo14() throws Exception {
        filesLocal();
        mapping1();
        provider.copyFilesFromProjectToRepo("dir/local/subdir/", null);
        checkCopy(V + "dir/local/subdir/3.png", VR + "remote/subdir/3.png");
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo15() throws Exception {
        filesLocal();
        mapping1();
        provider.copyFilesFromProjectToRepo("dir/lo", null);
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo16() throws Exception {
        filesLocal();
        mapping1();
        provider.copyFilesFromProjectToRepo("dir/lo/", null);
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo17() throws Exception {
        filesLocal();
        mapping1();
        provider.copyFilesFromProjectToRepo("", null);
        checkCopy(V + "dir/localfile", VR + "remotefile");
        checkCopy(V + "dir/local/1.txt", VR + "remote/1.txt");
        checkCopy(V + "dir/local/2.xml", VR + "remote/2.xml");
        checkCopy(V + "dir/local/subdir/3.png", VR + "remote/subdir/3.png");
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo21() throws Exception {
        filesLocal();
        mapping2();
        provider.copyFilesFromProjectToRepo("", null);
        checkCopy(V + "dir/local/1.txt", VR + "dir/local/1.txt");
        checkCopy(V + "dir/local/2.xml", VR + "dir/local/2.xml");
        checkCopy(V + "dir/local/subdir/3.png", VR + "dir/local/subdir/3.png");
        checkCopy(V + "dir/localfile", VR + "dir/localfile");
        checkCopy(V + "otherdir/local/4.file", VR + "otherdir/local/4.file");
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo22() throws Exception {
        filesLocal();
        mapping2();
        provider.copyFilesFromProjectToRepo("dir/localfile", null);
        checkCopy(V + "dir/localfile", VR + "dir/localfile");
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo31() throws Exception {
        filesLocal();
        mapping3();
        provider.copyFilesFromProjectToRepo("", null);
        checkCopy(V + "dir/local/1.txt", VR + "local/1.txt");
        checkCopy(V + "dir/local/2.xml", VR + "local/2.xml");
        checkCopy(V + "dir/local/subdir/3.png", VR + "local/subdir/3.png");
        checkCopy(V + "dir/localfile", VR + "localfile");
        checkCopyEnd();
    }

    public void testCopyFilesFromProjectToRepo41() throws Exception {
        filesLocal();
        mapping4();
        provider.copyFilesFromProjectToRepo("", null);
        checkCopy(V + "dir/local/1.txt", VR + "remote/dir/local/1.txt");
        checkCopy(V + "dir/local/2.xml", VR + "remote/dir/local/2.xml");
        checkCopy(V + "dir/local/subdir/3.png", VR + "remote/dir/local/subdir/3.png");
        checkCopy(V + "dir/localfile", VR + "remote/dir/localfile");
        checkCopy(V + "otherdir/local/4.file", VR + "remote/otherdir/local/4.file");
        checkCopyEnd();
    }

    void addRepo(String localPath, String repoPath, String... excludes) {
        RepositoryMapping m = new RepositoryMapping();
        m.setLocal(localPath);
        m.setRepository(repoPath);
        m.getExcludes().addAll(Arrays.asList(excludes));
        RepositoryDefinition def = new RepositoryDefinition();
        def.setUrl("url");
        def.getMapping().add(m);
        repos.add(def);
        provider.repositories.add(null);
    }

    void addFile(String path) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();
        f.createNewFile();
    }

    void checkCopy(String from, String to) {
        assertEquals("Wrong copy file from", from, copyFrom.get(copyCheckedIndex));
        assertEquals("Wrong copy file to", to, copyTo.get(copyCheckedIndex));
        copyCheckedIndex++;
    }

    void checkCopyEnd() {
        assertEquals("Wrong copy list", copyCheckedIndex, copyFrom.size());
    }

    public class VirtualRemoteRepositoryProvider extends RemoteRepositoryProvider {
        public VirtualRemoteRepositoryProvider(List<RepositoryDefinition> repositoriesDefinitions)
                throws Exception {
            super(new File(V), repositoriesDefinitions);
        }

        @Override
        protected void initializeRepositories() throws Exception {
            // disable initialize for testing
        }

        @Override
        protected void copyFile(File from, File to, String eolConversionCharset) throws IOException {
            copyFrom.add(from.getAbsolutePath());
            copyTo.add(to.getAbsolutePath());
        }

        @Override
        protected void addForCommit(IRemoteRepository2 repo, String path) throws Exception {
        }
    }

    /**
     * ProjectProperties successor for create project on the virtual directory with specific repositories
     * definitions.
     */
    protected class ProjectPropertiesTest extends ProjectProperties {
        public ProjectPropertiesTest(List<RepositoryDefinition> repositoriesDefinitions) {
            setProjectRoot(V);
            setRepositories(repositoriesDefinitions);
        }
    }
}
