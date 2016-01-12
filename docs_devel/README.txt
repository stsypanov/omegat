Building OmegaT
===============

OmegaT is built with Apache Ant. Run `ant` from the top level.

OmegaT will run on the latest Java, but is required to be compatible with Java
1.6. Further, JREs other than Oracle's are not officially supported. You are
thus recommended to build with Oracle JDK 1.6.

Eclipse and NetBeans are recommended IDEs for working with OmegaT source
code. NetBeans is required to modify *.form-based GUI layouts.

Check the other files in this directory for documentation. You can
produce Javadoc by running `ant javadoc`, or browse online:
    Trunk:    https://omegat.ci.cloudbees.com/job/omegat-trunk/javadoc/
    Releases: https://omegat.ci.cloudbees.com/job/omegat-javadoc/
              (Open desired version and click "Javadoc" link on left)


Contributing to OmegaT
======================

Main development is performed in the `trunk` branch of the SourceForge-hosted
Subversion repository:
    https://sourceforge.net/p/omegat/svn/HEAD/tree/trunk/

Git mirrors are also provided (svn trunk is synced to git master):
    SourceForge: https://sourceforge.net/p/omegat/code/ci/master/tree/
    GitHub:      https://github.com/OmegaT-Project/OmegaT

If you would like to submit a patch, the recommended procedure is as follows:

0. Consider opening a discussion on the developers' list first:
     http://lists.sourceforge.net/lists/listinfo/omegat-development

1. Open a ticket for your change. If it's a bug fix, create a Bug ticket:
     https://sourceforge.net/p/omegat/bugs/

   If it's a new feature or enhancement, open a Request For Enhancement (RFE):
     https://sourceforge.net/p/omegat/feature-requests/

2. If you prefer to work with file-based patches, attach your patch to the
   ticket.  If you prefer to work with pull requests, fork the OmegaT project on
   GitHub and open a pull request.

3. Patches/PRs will be reviewed by a core developer. If accepted, the changes
   will be squashed and committed to trunk by the reviewer.



About Files in This Directory
=============================

Multiple translation fields for filter.ods

This documents how different filters identify "alternate" translations. You can
view it with LibreOffice or any ODF-compatible viewer.

OmegaT developer's guide.odt

This describes the overall architecture of OmegaT as well as some technical
details of its implementation. You can view it with LibreOffice or any
ODF-compatible viewer.

OmegaT.vpp

This document is the source for the UML diagrams in the developer's guide; it
contains no content not also viewable within the developer's guide.  Visual
Paradigm Community Edition (free) is required to edit the file
(http://www.visual-paradigm.com)
