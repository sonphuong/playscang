# play scala anorm angularjs example

# install
`git clone https://github.com/sonphuong/playscang`

#common errors 
If you get this error: _/javaxxmlbind/DatatypeConverter, shutting down JVM..._

Solution:

Make sure you have this line in your build.sbt 
`libraryDependencies += "javax.xml.bind" % "jaxb-api" % "2.1"`

If you get this error: _/Jquery: Refused to apply inline style because it violates the following_ 

Solution:

add this line to application.conf
`play.filters.headers.contentSecurityPolicy = "script-src 'self' 'unsafe-inline' jquery.min.js;"` 

#connect to a database
add those lines to build.sbt file
`libraryDependencies += jdbc
libraryDependencies += "com.h2database" % "h2" % "1.4.194"`

add those lines to application.conf
`db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play"`
 
#using evolutions
add those lines to build.sbt file
`libraryDependencies += evolutions `


