## Preface

A template Eclipse RCP project to demonstrate a project's layout to work and
build both with Eclipse Tycho and Eclipse PDE. The result is a simple MySQL 
client that shows a list of databases on a MySQL server.

![screen shot][screenshot.png]

## Building from source code
#### Prerequisites

1. JDK 8+ installed
2. Git installed

Now, clone this repository

```bash
git clone https://github.com/che4/eclipse-rcp-mysql-client.git
```
#### build with Maven (Eclipse Tycho)
Switch to the directory where you have just cloned this repository and run:

```bash
cd io.e4.releng
```

then being in `io.e4.releng` directory run:

```bash
mvnw
```

The compile program resides in `io.e4.product/target/io.e4.app/`
&mdash;choose your platform and architecture.

**NB!** Only *win32.x86_64* has been tested so far.

#### build with Eclipse IDE (Eclipse PDE)

###### Additional prerequisite
Install [Eclipse for RCP and RAP developers][eclipse for rcp], thus you avoid 
installing additional plugins into your IDE.

###### Import projects into Eclipse

Import projects with [Import][import-menu.png] &rarr;
[General &rarr; Projects from Foler or Archive][import-select.png] and
[select all projects in the directory][import-projects.png].

Then in Eclipse IDE double click `target-platform/target-platfom.target` file and wait
while it resolves dependencies from remote sites. After that click
[_Set as Target Platform_][set-target-platform.png]
in the right upper corner of the target file editor.

Now problems (red marked projects) should disappear and you have successfully installed projects in the IDE.

To launch our application&mdash;open `io.e4.product/io.e4.product` file, in the editor click the link [`Launch an Eclipse application`][run-product.png]!


[eclipse for rcp]: https://www.eclipse.org/downloads/packages/eclipse-rcp-and-rap-developers/oxygen3a
[screenshot.png]:
https://user-images.githubusercontent.com/9863699/53821279-7ef62300-3f7e-11e9-9f86-22d1638abdc6.png "MySQL client screenshot"
[import-menu.png]: https://user-images.githubusercontent.com/9863699/53820685-53266d80-3f7d-11e9-81fd-f2acdf03d75d.png
[import-select.png]: https://user-images.githubusercontent.com/9863699/53820905-caf49800-3f7d-11e9-8a7f-67b89d7cd523.png
[import-projects.png]: https://user-images.githubusercontent.com/9863699/53820978-f11a3800-3f7d-11e9-83f9-e9504e03fde4.png
[set-target-platform.png]: 
https://user-images.githubusercontent.com/9863699/53821147-3c344b00-3f7e-11e9-8b4c-06c8edf44a41.png
[run-product.png]:
https://user-images.githubusercontent.com/9863699/53821215-6128be00-3f7e-11e9-84d0-062dbc227cf5.png
