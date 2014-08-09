# FTP Crawler

The FTP Crawler (or just FTPC) is a very simple Java based desktop application file crawler for UNIX based FTP servers.

Its primary purpose is to recursively crawl through the directories of a (remote) FTP server, find images (or other
files) and download a preview image of each to be displayed. The images are arranged in an overview list and can be
checked and eventually downloaded to a local file.

Additional features are:
- Optional definition of remote include and exclude paths
- Filter for arbitrary file names (via regular expression)
- Filter for file size (min/max)
- Filter for file modification date (start/end)
- Support for printing the list of found preview images
- Fully internationalized and translated (English and German available as of version 1.12)

## Why?

The FTP Crawler was developed for the special needs of a client, but I extended and refreshed the code base in order
to release it as open source software.

It is basically intended for the following scenario:
- Looking for one ore more (image) files on a remote FTP server,
- which contains too many or too large files to download and
- which does not provide other efficient means of searching than FTP.

## License

The FTP Crawler is licensed under the GNU GPL v3.0 open source license.

## System requirements

The FTP Crawler requires **Java 1.7+** and should run on most platforms supporting the Java Runtime Environment.

This repository is for the Java source files. If you just want to *use* the program you can download compiled
and packed binaries from the [project website] (http://www.ep-3.de/projekte/ftpc).

The application is based on the following libraries (for developers):
- Spring Framework 4 (primarily for its *Dependency Injection Container*)
- Apache Commons Net 3.3 (primarily for its *FTPClient*)
- MigLayout 4.2
- JUnit 4 (for unit testing)

The application is built and managed with [Gradle] (http://www.gradle.org/).
Dependencies are managed with the Maven Central Repository.

The source code is version controlled with [Git] (http://git-scm.com/) and hosted at [GitHub] (https://github.com/).

## Bug reports, feature requests, ideas ...

The GitHub Issue Tracker is simply ideal for such things:

https://github.com/tkrebs/ftpc/issues