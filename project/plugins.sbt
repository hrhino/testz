resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("io.get-coursier"   % "sbt-coursier"    % "1.0.1")
addSbtPlugin("com.47deg"         % "sbt-microsites"  % "0.7.16")
addSbtPlugin("org.scoverage"     % "sbt-scoverage"   % "1.5.1")
addSbtPlugin("com.typesafe.sbt"  % "sbt-site"        % "1.3.2")
addSbtPlugin("com.eed3si9n"      % "sbt-unidoc"      % "0.4.1")
addSbtPlugin("org.wartremover"   % "sbt-wartremover" % "2.2.1")
addSbtPlugin("de.heikoseeberger" % "sbt-header"      % "5.0.0")
addSbtPlugin("org.tpolecat"      % "tut-plugin"      % "0.6.3")
