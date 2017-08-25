cd /Users/Moe/Desktop/git/appraisalconfidence
browserify appraisalconfidence-app/src/main/resources/assets/app/js/app.js -o appraisalconfidence-app/src/main/resources/assets/distro/bundle.js
mvn package
java -jar appraisalconfidence-app/target/appraisalconfidence-app-1.0-SNAPSHOT.jar server config/local.yml