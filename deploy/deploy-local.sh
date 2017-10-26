cd /Users/Moe/Desktop/git/appraisalconfidence
npm install appraisalconfidence-app/src/main/resources/assets
browserify appraisalconfidence-app/src/main/resources/assets/app/js/app.js -o appraisalconfidence-app/src/main/resources/assets/distro/bundle.js
mvn package -DskipTests
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar appraisalconfidence-app/target/appraisalconfidence-app-1.0-SNAPSHOT.jar server config/local.yml