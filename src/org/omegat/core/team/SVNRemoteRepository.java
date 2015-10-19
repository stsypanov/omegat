/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2012 Alex Buloichik
               2014 Aaron Madlon-Kay
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
package org.omegat.core.team;

import java.io.File;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omegat.core.Core;
import org.omegat.util.Log;
import org.omegat.util.StringUtil;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.auth.SVNAuthentication;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.ISVNAuthStoreHandler;
import org.tmatesoft.svn.core.internal.wc.ISVNAuthenticationStorageOptions;
import org.tmatesoft.svn.core.internal.wc.ISVNGnomeKeyringPasswordProvider;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * SVN repository connection implementation.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Aaron Madlon-Kay
 */
public class SVNRemoteRepository implements IRemoteRepository {
    private static final Logger LOGGER = Logger.getLogger(SVNRemoteRepository.class.getName());

    /** Tests can disable show error. */
    public static boolean SHOW_UNKNOWN_ERRORS = true;

    File baseDirectory;
    SVNClientManager ourClientManager;
    boolean readOnly;

    public static boolean isSVNDirectory(File localDirectory) {
        File svnDir = new File(localDirectory, ".svn");
        return svnDir.exists() && svnDir.isDirectory();
    }

    public boolean isFilesLockingAllowed() {
        return true;
    }

    /**
     * Open working copy.
     */
    public SVNRemoteRepository(File localDirectory) throws Exception {
        this.baseDirectory = localDirectory;
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager();
        ourClientManager = SVNClientManager.newInstance(options, authManager);
    }

    public boolean isChanged(File file) throws Exception {
        SVNStatus status = null;
        try {
            status = ourClientManager.getStatusClient().doStatus(file, false);
        } catch (SVNException e) {
            Log.log(Level.SEVERE, "failed to get status of repository", e);
            if (e.getErrorMessage().getErrorCode().getCode()==155007) {
                //file is outside repository, so not under version control.
                return false;
            } else throw e;
        }
        //if file does not exist and not under version control, then return false.
        if (status == null) return false;
        SVNStatusType statusType = status.getContentsStatus();
        //hmm, if file not under version control, status is STATUS_NONE, and not STATUS_UNVERSIONED?
        return statusType != SVNStatusType.STATUS_NORMAL && statusType != SVNStatusType.STATUS_UNVERSIONED && statusType != SVNStatusType.STATUS_NONE;
    }

    public boolean isUnderVersionControl(File file) throws Exception {
        SVNStatus status = null;
        try {
            status = ourClientManager.getStatusClient().doStatus(file, false);
        } catch (SVNException e) {
            if (e.getErrorMessage().getErrorCode().getCode()==155007) {
                //file is outside repository, so not under version control.
                return false;
            } else throw e;
        }
        //if file does not exist and not under version control, then return false.
        if (status == null) return false;
        SVNStatusType statusType = status.getContentsStatus();
        //hmm, if file not under version control, status is STATUS_NONE, and not STATUS_UNVERSIONED?
        return statusType != SVNStatusType.STATUS_UNVERSIONED && statusType != SVNStatusType.STATUS_NONE;
    }

    public void setCredentials(Credentials credentials) {
        if (credentials == null) {
            return;
        }
        ourClientManager.dispose();

        DefaultSVNAuthenticationManager authManager = new DefaultSVNAuthenticationManager(null, true,
                credentials.username, new String(credentials.password));
        if (credentials.saveAsPlainText) {
            authManager.setAuthenticationStorageOptions(FORCE_SAVE_PLAIN_PASSWORD);
        }
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        ourClientManager = SVNClientManager.newInstance(options, authManager);
        readOnly = credentials.readOnly;
    }

    public void setReadOnly(boolean value) {
        readOnly = value;
    }

    public void updateFullProject() throws SocketException, Exception {
        Log.logInfoRB("SVN_START", "update");
        try {
            long rev = ourClientManager.getUpdateClient().doUpdate(baseDirectory, SVNRevision.HEAD, SVNDepth.INFINITY,
                    false, false);
            Log.logDebug(LOGGER, "SVN updated to revision {0}", rev);
            Log.logInfoRB("SVN_FINISH", "update");
        } catch (SVNAuthenticationException ex) {
            // authentication failed - need to ask username/password
            Log.logWarningRB("SVN_ERROR", "update", ex.getMessage());
            throw new AuthenticationException(ex);
        } catch (SVNException ex) {
            Log.logErrorRB("SVN_ERROR", "update", ex.getMessage());
            checkNetworkException(ex);
            throw ex;
        } catch (Exception ex) {
            Log.logErrorRB("SVN_ERROR", "update", ex.getMessage());
            throw ex;
        }
    }

    public void checkoutFullProject(String repositoryURL) throws Exception {
        Log.logInfoRB("SVN_START", "checkout");

        SVNURL url = SVNURL.parseURIDecoded(repositoryURL);
        try {
            long rev = ourClientManager.getUpdateClient().doCheckout(url, baseDirectory, SVNRevision.HEAD,
                    SVNRevision.HEAD, SVNDepth.INFINITY, false);
            Log.logDebug(LOGGER, "SVN checked out to revision {0}", rev);
            Log.logInfoRB("SVN_FINISH", "checkout");
        } catch (SVNAuthenticationException ex) {
            // authentication failed - need to ask username/password
            Log.logWarningRB("TEAM_WRONG_AUTHENTICATION");
            throw new AuthenticationException(ex);
        } catch (Exception ex) {
            Log.logErrorRB("SVN_ERROR", "checkout", ex.getMessage());
            throw ex;
        }
    }

    public String getBaseRevisionId(File file) throws Exception {
        SVNInfo info = ourClientManager.getWCClient().doInfo(file, SVNRevision.BASE);
        Log.logDebug(LOGGER, "SVN committed revision for file {0} is {1}", file, info.getCommittedRevision().getNumber());

        return Long.toString(info.getCommittedRevision().getNumber());
    }

    public void restoreBase(File[] files) throws Exception {
        ourClientManager.getWCClient().doRevert(files, SVNDepth.EMPTY, null);
        Log.logDebug(LOGGER, "SVN restore base for {0}", toList(files));
    }

    public void download(File[] files) throws SocketException, Exception {
        Log.logInfoRB("SVN_START", "download");
        try {
            long[] revs = ourClientManager.getUpdateClient().doUpdate(files, SVNRevision.HEAD, SVNDepth.INFINITY,
                    false, false);
            Log.logDebug(LOGGER, "SVN updated files {0} to revisions {1}", toList(files), toList(revs));
            Log.logInfoRB("SVN_FINISH", "download");
        } catch (SVNException ex) {
            Log.logErrorRB("SVN_ERROR", "download", ex.getMessage());
            checkNetworkException(ex);
            throw ex;
        } catch (Exception ex) {
            Log.logErrorRB("SVN_ERROR", "download", ex.getMessage());
            throw ex;
        }
    }

    public void reset() throws Exception {
        Log.logInfoRB("SVN_START", "reset");
        try {
            // not tested. Can anyone confirm this code?
            Log.logDebug(LOGGER, "SVN revert all files in {0}", baseDirectory);
            ourClientManager.getWCClient().doRevert(new File[] { baseDirectory }, SVNDepth.INFINITY,
                    null);
            Log.logInfoRB("SVN_FINISH", "reset");
        } catch (Exception ex) {
            Log.logErrorRB("SVN_ERROR", "reset", ex.getMessage());
            throw ex;
        }
    }

    public void upload(File file, String commitMessage) throws SocketException, Exception {
        if (readOnly) {
            // read-only - upload disabled
            Log.logInfoRB("SVN_READONLY");
            return;
        }

        Log.logInfoRB("SVN_START", "upload");
        try {
            SVNCommitInfo info = ourClientManager.getCommitClient().doCommit(new File[] { file }, false, commitMessage,
                    null, null, false, false, SVNDepth.INFINITY);
            Log.logDebug(LOGGER, "SVN committed file {0} into new revision {1}", file, info.getNewRevision());
            Log.logInfoRB("SVN_FINISH", "upload");
        } catch (SVNAuthenticationException ex) {
            // authentication failed - need to ask username/password
            Log.logWarningRB("SVN_ERROR", "update", ex.getMessage());
            throw new AuthenticationException(ex);
        } catch (SVNException ex) {
            if (ex.getErrorMessage().getErrorCode() == SVNErrorCode.FS_CONFLICT) {
                // Somebody else committed changes - it's normal. Will upload on next save.
                Log.logWarningRB("SVN_CONFLICT");
                return;
            } else {
                Log.logErrorRB("SVN_ERROR", "upload", ex.getMessage());
                checkNetworkException(ex);
            }
            if (SHOW_UNKNOWN_ERRORS) {
                Core.getMainWindow().showErrorDialogRB("SVN_UPLOAD_ERROR_TITLE", "SVN_UPLOAD_ERROR",
                        ex.getMessage());
            }
            throw ex;
        } catch (Exception ex) {
            Log.logErrorRB("SVN_ERROR", "upload", ex.getMessage());
            throw ex;
        }
    }

    List<File> toList(File[] files) {
        return Arrays.asList(files);
    }

    List<Long> toList(long[] arr) {
        List<Long> result = new ArrayList<>(arr.length);
        for (long v : arr) {
            result.add(v);
        }
        return result;
    }

    void checkNetworkException(Exception ex) throws NetworkException {
        if (ex.getCause() instanceof SocketException) {
            throw new NetworkException(ex.getCause());
        }
        if (ex instanceof SVNException) {
            SVNException se = (SVNException) ex;
            if (se.getErrorMessage().getErrorCode().getCategory() == SVNErrorCode.RA_DAV_CATEGORY) {
                throw new NetworkException(se);
            }
        }
    }

    static ISVNAuthenticationStorageOptions FORCE_SAVE_PLAIN_PASSWORD = new ISVNAuthenticationStorageOptions() {
        public boolean isNonInteractive() throws SVNException {
            return false;
        }

        public ISVNAuthStoreHandler getAuthStoreHandler() throws SVNException {
            return FORCE_SAVE_PLAIN_PASSWORD_HANDLER;
        }

        public boolean isSSLPassphrasePromptSupported() {
            return false;
        }

        public ISVNGnomeKeyringPasswordProvider getGnomeKeyringPasswordProvider() {
            return null;
        }
    };

    static ISVNAuthStoreHandler FORCE_SAVE_PLAIN_PASSWORD_HANDLER = new ISVNAuthStoreHandler() {
        public boolean canStorePlainTextPassphrases(String realm, SVNAuthentication auth) throws SVNException {
            return false;
        }

        public boolean canStorePlainTextPasswords(String realm, SVNAuthentication auth) throws SVNException {
            return true;
        }
    };
    
    /**
     * Determines whether or not the supplied URL represents a valid Subversion repository.
     * 
     * <p>Does the equivalent of <code>svn info <i>url</i></code>.
     * 
     * @param url URL of supposed remote repository
     * @return true if repository appears to be valid, false otherwise
     */
    public static boolean isSVNRepository(String url, Credentials credentials)
            throws AuthenticationException {
        // Heuristics to save some waiting time
        if (url.startsWith("git://")) {
            return false;
        }
        try {
            SVNURL svnurl = SVNURL.parseURIDecoded(url);
            SVNClientManager manager = SVNClientManager.newInstance();
            ISVNAuthenticationManager authManager;
            if (credentials != null) {
                DefaultSVNAuthenticationManager defaultManager = new DefaultSVNAuthenticationManager(null,
                        true, credentials.username, new String(credentials.password));
                if (credentials.saveAsPlainText) {
                    defaultManager.setAuthenticationStorageOptions(FORCE_SAVE_PLAIN_PASSWORD);
                }
                authManager = defaultManager;
            } else {
                authManager = SVNWCUtil.createDefaultAuthenticationManager();
            }
            manager.setAuthenticationManager(authManager);
            manager.getWCClient().doInfo(svnurl, SVNRevision.HEAD, SVNRevision.HEAD);
        } catch (SVNAuthenticationException ex) {
            throw new AuthenticationException(ex);
        } catch (SVNException ex) {
            return false;
        }
        return true;
    }

    public static String guessRepoName(String url) {
        url = StringUtil.stripFromEnd(url, "/", "/trunk", "/branches", "/tags", "/svn");
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
